package com.bignerdranch.android.scavengerhunttemp;

import java.util.List;

/**
 * Created by Brendon on 11/10/16.
 */

public class ScavengerHunt {


    String huntName;

    List<Item> places;

    public String getHuntName() {
        return huntName;
    }

    public void setHuntName(String huntName) {
        this.huntName = huntName;
    }

    public List<Item> getPlaces() {
        return places;
    }

    public void setPlaces(List<Item> places) {
        this.places = places;
    }

    ScavengerHunt() {} // Need to figure out what this does.


    @Override
    public String toString() {
        return "ScavengerHunt{" +
                "huntName='" + huntName + '\'' +
                ", places=" + places +
                '}';
    }
}
