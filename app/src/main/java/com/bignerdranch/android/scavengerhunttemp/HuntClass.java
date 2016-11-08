package com.bignerdranch.android.scavengerhunttemp;

/**
 * Created by Brendon on 11/8/16.
 */

public class HuntClass {

    String hunt;
    String item;
    long latitude;
    long longitude;

    @Override
    public String toString() {
        return "HuntClass{" +
                "hunt='" + hunt + '\'' +
                ", item='" + item + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }
}
