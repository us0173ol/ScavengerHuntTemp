package com.bignerdranch.android.scavengerhunttemp;

/**
 * Created by Brendon on 11/10/16.
 */

// This class builds locations for the Scavenger Hunts.
public class Item {

    String placeName;
    double lat;
    double lon;


    // Used in Firebase
    Item() {}


    // Used to build locations for the hunts.
    public Item(String placeName, double lat, double lon) {

        this.placeName = placeName;
        this.lat = lat;
        this.lon = lon;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "Item{" +
                "placeName='" + placeName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
