package com.example.tugasakhir.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferences sharedPref;

    public SharedPreferencesManager(Context context) {
        this.sharedPref = context.getSharedPreferences(
                "shared-preferences", Context.MODE_PRIVATE
        );
    }

    private static final String KEY_IS_LOGGED_IN = ("key-is-logged-in");
    private static final String KEY_USER_ID = "key-user-id";
    private static final String KEY_KERETA_ID = "key-kereta-id";
    private static final String KEY_KERETA_FIREBASE = "key-kereta-fb-id";
    private static final String KEY_KERETA_ANIN = "key-kereta-anin-id";

    public void setIsLoggedIn(boolean login) {
        sharedPref.edit()
                .putBoolean((KEY_IS_LOGGED_IN), login)
                .apply();
    }

    public Boolean getIsLoggedIn() {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public void setUserId(Integer userId) {
        sharedPref.edit()
                .putInt(KEY_USER_ID, userId)
                .apply();
    }

    public Integer getUserId() {
        return sharedPref.getInt(KEY_USER_ID, -1);
    }

    public Integer getIdKereta() {
        return sharedPref.getInt(KEY_KERETA_ID, -1);
    }

    public void setIdKereta(Integer value) {
        sharedPref.edit()
                .putInt(KEY_KERETA_ID, value)
                .apply();
    }

    public void setKeretaFirebaseId(String value) {
        sharedPref.edit()
                .putString(KEY_KERETA_FIREBASE, value)
                .apply();
    }

    public String getKeretaFirebaseId() {
        return sharedPref.getString(KEY_KERETA_FIREBASE, "");
    }

    public void setKeretaAninId(Integer value) {
        sharedPref.edit()
                .putInt(KEY_KERETA_ANIN, value)
                .apply();
    }

    public int getKeretaAninId() {
        return sharedPref.getInt(KEY_KERETA_ANIN, -1);
    }
}
