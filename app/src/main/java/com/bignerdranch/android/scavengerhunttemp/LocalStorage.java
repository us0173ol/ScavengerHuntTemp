package com.bignerdranch.android.scavengerhunttemp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Brendon on 11/15/16.
 */

public class LocalStorage {

    private static final String USERNAME_KEY = "username";
    private static final String USER_HUNT = "userHunt";


    private Context mContext;


    LocalStorage(Context context) {

        this.mContext = context;

    }

    protected void writeUsername(String username) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.edit().putString(USERNAME_KEY, username).apply();

    }


    protected String fetchUsername() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(USERNAME_KEY, null);

    }

    protected void writeUserHunt(String currentHunt) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        preferences.edit().putString(USER_HUNT, currentHunt).apply();

    }


    protected String fetchUserHunt() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        return preferences.getString(USER_HUNT, null);

    }


}
