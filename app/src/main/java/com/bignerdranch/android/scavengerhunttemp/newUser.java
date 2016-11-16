package com.bignerdranch.android.scavengerhunttemp;

/**
 * Created by Brendon on 11/15/16.
 */

public class NewUser {

    String userName;
    String currentHunt;
    double userScore;


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

    public double getUserScore() {
        return userScore;
    }

    public void setUserScore(double userScore) {
        this.userScore = userScore;
    }


    @Override
    public String toString() {
        return "NewUser{" +
                "currentHunt='" + currentHunt + '\'' +
                ", userScore=" + userScore +
                '}';
    }
}
