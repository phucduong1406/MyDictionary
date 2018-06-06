package it15110278.vn.edu.hcmute.vn.mydictionary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASUS on 6/7/2018.
 */

public class Global {
    public static void saveState(Activity activity, String key, String value){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getState(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }
}
