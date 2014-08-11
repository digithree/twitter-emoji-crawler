/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package twitteremoji;

import java.util.prefs.Preferences;

/**
 *
 * @author simonkenny
 */
public class TokenPref {
    private static final String TOKENPREF_TOKEN = "TOKENPREF_TOKEN";
    private static final String TOKENPREF_SECRET = "TOKENPREF_SECRET";
    
    public static boolean hasToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenPref.class);
        String token = prefs.get(TOKENPREF_TOKEN, null);
        String secret = prefs.get(TOKENPREF_SECRET, null);
        return token != null || secret != null;
    }
    
    public static void putToken(String token) {
        Preferences prefs = Preferences.userNodeForPackage(TokenPref.class);
        prefs.put(TOKENPREF_TOKEN, token);
    }
    
    public static String getToken() {
        Preferences prefs = Preferences.userNodeForPackage(TokenPref.class);
        return prefs.get(TOKENPREF_TOKEN, null);
    }
    
    public static void putSecret(String secret) {
        Preferences prefs = Preferences.userNodeForPackage(TokenPref.class);
        prefs.put(TOKENPREF_SECRET, secret);
    }
    
    public static String getSecret() {
        Preferences prefs = Preferences.userNodeForPackage(TokenPref.class);
        return prefs.get(TOKENPREF_SECRET, null);
    }
}
