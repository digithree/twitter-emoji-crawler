/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitteremoji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author simonkenny
 */
public class FXMLDocumentController implements Initializable {
    
    // emoji links list
    @FXML
    private TableView linksTableView;
    @FXML
    private Button importLinksButton;
    @FXML
    private Button exportLinksButton;
    // link crawler
    @FXML
    private TableView crawlerTableView;
    @FXML
    private Button crawlButton;
    @FXML
    private Button stopButton;
    @FXML
    private Label statusLabel;
    private String STATUS_PREFIX = "Status: ";
    private String STATUS_DISCONNECTED = "Disconnected";
    private String STATUS_CONNECTED = "Connected";
    private String STATUS_ERROR = "Error";
    private String STATUS_CRAWLING = "Crawling...";
    
    // twitter objects
    private Twitter twitter;
    private final String key = "kw7vAK9UXvweweys0CF09MkKh";
    private final String secret = "Q4dzY6WBQC3FKNWarEXBeK3qlRyKoMupwUaX3vGOoFkxr4Uko3";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // nothing!
    }
    
    public void setup(Stage stage) {
        try {
            // start twitter auth process
            twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(key, secret);
            RequestToken requestToken = twitter.getOAuthRequestToken();
            // check if we have a token for the user, if not authenticate and store in prefs
            if( !TokenPref.hasToken() ) {
                System.out.println("has no prefs!");
                Dialogs dialog = Dialogs.create()
                    .owner(null)
                    .title("Grant account access")
                    .masthead("You must grant TwitterEmoji access to your account")
                    .message("You only have to do this once. Go to Twitter authorisation webpage (opens default browser)?")
                    .actions(Dialog.Actions.NO,Dialog.Actions.YES);
                Action response = dialog.showConfirm();
                if( response.textProperty().getValue().contains("No") ) {
                    Platform.exit();
                    return;
                }
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    Platform.exit();
                    return;
                } catch (URISyntaxException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    Platform.exit();
                    return;
                }
                // get pin
                Optional<String> responsePin = Dialogs.create()
                        .owner(stage)
                        .title("Authentication")
                        .masthead("Authorise TwitterEmoji to get pin")
                        .message("Please enter pin:")
                        .showTextInput();
                // One way to get the response value.
                String pin = null;
                if (responsePin.isPresent()) {
                    pin = responsePin.get();
                }
                AccessToken accessToken = null;
                if( pin == null ) {
                    accessToken = twitter.getOAuthAccessToken();
                } else {
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                }
                // assume got, otherwise TwitterException thrown
                // store tokens
                TokenPref.putToken(accessToken.getToken());
                TokenPref.putSecret(accessToken.getTokenSecret());
            } else {
                System.out.println("has prefs!");
                /*
                Dialogs dialog = Dialogs.create()
                        .owner(null)
                        .title("Information")
                        .masthead("Application has Twitter token/secret")
                        .message("Token: "+TokenPref.getToken()+"\nSecret: "+TokenPref.getSecret());
                dialog.showInformation();
                */
                //Status status = twitter.updateStatus("testing api");
                AccessToken accessToken = new AccessToken(TokenPref.getToken(),TokenPref.getSecret());
                twitter.setOAuthAccessToken(accessToken);
                Dialogs dialog = Dialogs.create()
                        .owner(null)
                        .title("Information")
                        .masthead("Twitter authentication")
                        .message("Connected to Twitter");
                dialog.showInformation();
            }
        } catch (TwitterException ex) {
            Dialogs.create()
                    .owner(null)
                    .title("Error")
                    .masthead(null)
                    .message("Twitter authentication could not be completed")
                    .showError();
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
            return;
        }
        statusLabel.setText(STATUS_PREFIX+STATUS_CONNECTED);
        /*
        // add close behaviour to window to shut down twitter
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                // TODO : check if this is triggered just by window minimisation
                // stop system
                System.out.println("--- Twitter client stopped");
            }
        });
        */
        /*
        // add message reader
        (new Thread() {
            public void run() {
                // do what?
            }
        }).start();
                */
    }
    
    // event handlers
    @FXML
    private void handleCrawlAction(ActionEvent event) {
        // will make thread for this but just a test for the moment
        // TODO : should check twitter status?
        Platform.runLater(new Runnable() {
            @Override public void run() {
                statusLabel.setText(STATUS_PREFIX+STATUS_CRAWLING);
            }
        });
        try {
            Query query = new Query("Cocaine");
            QueryResult result;
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {
                System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
            }
        } catch (TwitterException te) {
            statusLabel.setText(STATUS_PREFIX+STATUS_ERROR);
        }
    }
}
