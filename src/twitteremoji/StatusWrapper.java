/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitteremoji;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author simonkenny
 */
public class StatusWrapper {
    private final SimpleStringProperty aquiredProperty;
    private final SimpleStringProperty userProperty;
    private final SimpleStringProperty tweetProperty;
    private final SimpleStringProperty emojiProperty;
    
    public StatusWrapper(String aquiredP, String userP, String tweetP, String emojiP) {
        aquiredProperty = new SimpleStringProperty(aquiredP);
        userProperty = new SimpleStringProperty(userP);
        tweetProperty = new SimpleStringProperty(tweetP);
        emojiProperty = new SimpleStringProperty(emojiP);
    }
    
    public String getAquired() {
        return aquiredProperty.get();
    }
    public void setAquired(String text) {
        aquiredProperty.set(text);
    }
    
    public String getUser() {
        return userProperty.get();
    }
    public void setUser(String text) {
        userProperty.set(text);
    }
    
    public String getTweet() {
        return tweetProperty.get();
    }
    public void setTweet(String text) {
        tweetProperty.set(text);
    }
    
    public String getEmoji() {
        return emojiProperty.get();
    }
    public void setEmoji(String text) {
        emojiProperty.set(text);
    }
}
