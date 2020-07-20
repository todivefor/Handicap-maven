/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.todivefor.handicap.process;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import org.todivefor.handicap.HandicapMain;

/**
 *
 * @author peterream
 */
public class PrefsProc
{
    static Preferences preferences;

/**
 * This method returns the node  
 * @param node
 * @return 
 */
    public static Preferences prefsNode(String node)
    {
        /*
         * 
         * Preferences 
         * MAC OS X
         * 		~/library/Preferences/com.apple.java.util.prefs.plist
         * Windows
         * 		Registry - HKEY_CURRENT_USER\SOFTWARE\JavaSoft\Prefs
         * 			It produces a warning
         * 
         * 
         */
        preferences = Preferences.userRoot().node(node);                            // Preferences node
        return preferences;
    }
    
/**
 * This method returns the preference (String)
 * @param prefKey - key to get value
 * @param prefDefault - default if no key
 * @return 
 */
    public static String getPref(String prefKey, String prefDefault)
    {
        String value = preferences.get(prefKey, "nokey");                           // Value from preference
        if (value.equals("nokey"))                                                  // Key exist?
            value = prefDefault;                                                    // No, value = default no value
        return value;
    }

/**
 * This method writes the preference (String)
 * @param prefKey - key to write value
 * @param prefValue - value to write
 * @return 
 */
    public static void putPref(String prefKey, String prefValue)
    {
        preferences.put(prefKey, prefValue);
        return;
    }

/**
 * This method returns the preference (boolean)
 * @param prefKey - key to get value
 * @param prefDefault - default if no key
 * @return 
 */
    public static boolean getBooleanPref(String prefKey, boolean prefDefault)
    {
        boolean value = preferences.getBoolean(prefKey, false);                     // Value from preference
        if (!value)                                                                 // Key exist?
            value = prefDefault;                                                    // No, value = default no value
        return value;
    }

/**
 * This method writes the preference (boolean)
 * @param prefKey - key to write value
 * @param prefValue - value to write
 */
    public static void putBooleanPref(String prefKey, boolean prefValue)
    {
        preferences.putBoolean(prefKey, prefValue);                         // Key from preference
//        return;
    }
    
/**
 * This method removes the key
 * @param prefKey - key to remove
 */
    public static void removePref(String prefKey)
    {
        preferences.remove(prefKey);                                                // Key from preference
//        return;
    }

/**
 * This method removes the node
 */
    public static void removeNodePref()
    {
        try
        {
            preferences.removeNode();                                               // Key from preference
        }
        catch (BackingStoreException ex)
        {
            Logger.getLogger(PrefsProc.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }
//        return;
    }
  
/**
 * This method makes all changes permanent
 */
    public static void flushPref()
    {
        try                              
        {
            preferences.flush();                                                    // Make all preferences changes permanent
        }
        catch (BackingStoreException ex)
        {
            Logger.getLogger(HandicapMain.class.getName()).log(Level.SEVERE, 
                    null, ex);
        }
    }

/**
 * This method returns preference
 * @return - preference
 */    
    public static Preferences getPreferences()
    {
        return preferences;
    }
}
