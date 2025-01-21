package com.anime.application.base;

import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;


public class SharedPref {

    private static SharedPref singleton;
    private EncryptedSharedPreferences sharedPreferences;
    private static final String PREF_NAME = "prefname";
    private static final String TOKEN_KEY = "auth_token";
    private static final String NIGHT_MODE_KEY = "night_mode";  // Key for storing Night Mode preference
    private static final String FIRST_START_KEY = "first_start"; // Key for storing first start preference


    // Constructor that initializes SharedPreferences with the context from BaseApp
    private SharedPref(Context context) {
        try {
            // Use the app context from BaseApp
            context = BaseApp.get().getApplicationContext();

            // Generate or get the MasterKey for encryption
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Initialize EncryptedSharedPreferences
            sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,  // For key encryption
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM  // For value encryption
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Singleton instance to access SharedPref
    public static synchronized SharedPref getInstance() {
        if (singleton == null) {
            singleton = new SharedPref(BaseApp.get()); // Use context from BaseApp
        }
        return singleton;
    }

    // Save the token securely
    public void saveToken(String token) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
    }
    // Retrieve the stored token
    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    // Remove the token
    public void removeToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply();
    }

    // Save the Night Mode preference securely
    public void saveNightMode(boolean isNightMode) {
        sharedPreferences.edit().putBoolean(NIGHT_MODE_KEY, isNightMode).apply();
    }

    // Retrieve the stored Night Mode preference
    public boolean getNightMode() {
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, true);  // Default is true (Night Mode ON)
    }

    // Save the First Start preference securely
    public void saveFirstStart(boolean isFirstStart) {
        sharedPreferences.edit().putBoolean(FIRST_START_KEY, isFirstStart).apply();
    }

    // Retrieve the stored First Start preference
    public boolean isFirstStart() {
        return sharedPreferences.getBoolean(FIRST_START_KEY, false);  // Default is false (not the first start)
    }

    // Clear the Night Mode preference
    public void clearNightMode() {
        sharedPreferences.edit().remove(NIGHT_MODE_KEY).apply();
    }

    // Clear the First Start preference
    public void clearFirstStart() {
        sharedPreferences.edit().remove(FIRST_START_KEY).apply();
    }



    // Clear all data (if necessary)
    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }
}
