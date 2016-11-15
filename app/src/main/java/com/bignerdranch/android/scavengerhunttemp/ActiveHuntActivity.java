package com.bignerdranch.android.scavengerhunttemp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ActiveHuntActivity extends AppCompatActivity {

    private static final String TAG = "Active hunt activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_hunt);

        ScavengerHunt hunt = savedInstanceState.getParcelable("HUNT");

        Log.d(TAG, hunt.toString());

        //set up geofences

        //monitor geofences

        //Use geofence *service* that updates the Firebase DB as locations are found
        //send update to Firebase every time a place is found - for this user- for this hunt

        //from this Activity, run a query that monitors the users scavenger hunt
        //if user finds all places, do something special
    }
}
