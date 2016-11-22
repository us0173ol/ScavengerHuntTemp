package com.bignerdranch.android.scavengerhunttemp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by miked on 11/8/2016.
 */
public class GeoFenceService extends IntentService {

    LocalStorage mLocalStorage;

    //TODO check that you've registed this IntentService in AndroidManifest.xml

    private static final String TAG = "GeoFenceService";

    //Constructor required
    public GeoFenceService() {
        super(TAG);
        Log.d(TAG, "GeoFenceService object created");
    }



    @Override
    protected void onHandleIntent(Intent intent) {



        //Log event. For a real app, could save to a Firebase DB?
        //The Geofence notifications may happen when user is not viewing the app so
        //it's not recommended to send them to an Activity or Fragment. Instead, send them
        //to an IntentService class that can start in the background and handle the event, by saving it somewhere
        //or issuing a notification, as in the Google example.

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);


        Log.d(TAG, "Geofence event received");

        //todo check for errors

        //If more than one GeoFence used, figure out which GeoFence was entered/exited

        List<Geofence> listOfGeoFences = geofencingEvent.getTriggeringGeofences();

        Log.d(TAG, "The request IDs of all GeoFences triggered:");

        int isfound = 0;// 0=false

        for (Geofence geofence : listOfGeoFences) {

            Log.d(TAG, "Geofence ID: " + geofence.getRequestId());  //this is configured in MainActivity

            int transition = geofencingEvent.getGeofenceTransition();

            String transitionString = "";

            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                transitionString = "entered";

                mLocalStorage = new LocalStorage(this);
                Firebase firebase = new Firebase(mLocalStorage);
                firebase.updateLocationFound(geofence.getRequestId());
                onCreate();//TODO only toasts when onCreate is called, active hunt screen only updates when Mainactivity is rotated



            } else if (transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                transitionString = "is dwelling in";
            } else if (transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                transitionString = "exited";
            }

            Date currentTime = new Date();
            String eventMessage = "Device " + transitionString + " GeoFence with tag " + geofence.getRequestId() + " at " + currentTime ;
            // e.g. "Device entered GeoFence with tag MCTC_Geofence at November 8 2016 15.12:12..."
            Toast.makeText(this, eventMessage, Toast.LENGTH_LONG).show();
            mLocalStorage = new LocalStorage(this);

            Firebase firebase = new Firebase(mLocalStorage);
            firebase.addGeoFenceEvent(eventMessage);   //TODO an object to store more detail about event




        }


        //You'd probably need some more logic here to do whatever you need to do when a particular GeoFence is triggered.


    }
}



