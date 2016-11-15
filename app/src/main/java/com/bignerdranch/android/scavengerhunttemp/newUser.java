package com.bignerdranch.android.scavengerhunttemp;

/**
 * Created by Brendon on 11/15/16.
 */

public class NewUser {

    String userName;
    String currentHunt;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentHunt() {
        return currentHunt;
    }

    public void setCurrentHunt(String currentHunt) {
        this.currentHunt = currentHunt;
    }

    @Override
    public String toString() {
        return "newUser{" +
                "userName='" + userName + '\'' +
                ", currentHunt='" + currentHunt + '\'' +
                '}';
    }
}
