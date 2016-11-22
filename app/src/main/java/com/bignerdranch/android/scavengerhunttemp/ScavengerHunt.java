package com.bignerdranch.android.scavengerhunttemp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Brendon on 11/10/16.
 */

public class ScavengerHunt implements Parcelable {


    String huntName;

    List<Item> places;


    protected ScavengerHunt(Parcel in) {
        huntName = in.readString();
        places = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<ScavengerHunt> CREATOR = new Creator<ScavengerHunt>() {
        @Override
        public ScavengerHunt createFromParcel(Parcel in) {
            return new ScavengerHunt(in);
        }

        @Override
        public ScavengerHunt[] newArray(int size) {
            return new ScavengerHunt[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(huntName);
        parcel.writeTypedList(places);
    }

}
