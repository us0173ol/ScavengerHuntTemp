package com.bignerdranch.android.scavengerhunttemp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brendon on 11/10/16.
 */

// This class builds locations for the Scavenger Hunts.
public class Item implements Parcelable{

    String placeName;
    double lat;
    double lon;
    String locationFound;


    // Used in Firebase
    Item() {}


    // Used to build locations for the hunts.
    public Item(String placeName, double lat, double lon, String locationFound) {

        this.placeName = placeName;
        this.lat = lat;
        this.lon = lon;
        this.locationFound = locationFound;

    }

    protected Item(Parcel in) {
        placeName = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        locationFound = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public String getLocationFound() {
        return locationFound;
    }

    public void setLocationFound(String locationFound) {
        this.locationFound = locationFound;
    }

    @Override
    public String toString() {
        return "Item{" +
                "placeName='" + placeName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                " locationFound=" + locationFound +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
        parcel.writeString(locationFound);
    }
}
