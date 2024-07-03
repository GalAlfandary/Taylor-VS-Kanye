package com.example.taylorvskanye.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesManagerV3 {

    private static volatile SharePreferencesManagerV3 instance = null;
    private SharedPreferences sharedPref;
    private static final String SP_FILE = "SP_FILE";


    private SharePreferencesManagerV3(Context context) {
        sharedPref = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharePreferencesManagerV3 getInstance() {
        return instance;
    }

    public static SharePreferencesManagerV3 init(Context context) {
        if (instance == null) {
            synchronized (SharePreferencesManagerV3.class){
                if (instance == null){
                    instance = new SharePreferencesManagerV3(context);
                }
            }
        }
        return getInstance();
    }

    public void putString(String key, String value) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return sharedPref.getString(key, defValue);
    }
}
