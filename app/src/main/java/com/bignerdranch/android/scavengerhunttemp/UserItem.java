package com.bignerdranch.android.scavengerhunttemp;

/**
 * Created by Brendon on 11/15/16.
 */

public class UserItem {

    String placeName;
    double lat;
    double lon;
    String foundPlace;


    // Used in Firebase
    UserItem() {}


    // Used to build locations for the hunts.
    public UserItem(String placeName, double lat, double lon, String foundPlace) {

        this.placeName = placeName;
        this.lat = lat;
        this.lon = lon;
        this.foundPlace = foundPlace;

    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getFoundPlace() {
        return foundPlace;
    }

    public void setFoundPlace(String foundPlace) {
        this.foundPlace = foundPlace;
    }


    @Override
    public String toString() {
        return "UserItem{" +
                "placeName='" + placeName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", foundPlace='" + foundPlace + '\'' +
                '}';
    }
}
