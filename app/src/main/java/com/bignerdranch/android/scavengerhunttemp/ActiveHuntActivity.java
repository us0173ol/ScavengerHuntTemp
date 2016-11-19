package com.bignerdranch.android.scavengerhunttemp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ActiveHuntActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>, Firebase.GeoFenceEventCallback {

    Firebase mFirebase;
    GoogleApiClient mGoogleApiClient;
    LocalStorage mLocalStorage;

    HashMap mUserHuntInfo;

    int REQUEST_LOCATION_PERMISSION = 0;

    private static final String TAG = "Active hunt activity";

    ListView mUserListView;
    Button mUserDeleteButton;
    Button mUserCheatButton;

    private List<Item> mUserPlaceData;
    private String mHuntName;
    private int mHuntScore;

    String tag;
    double lat;
    double lon;
    float radius = 500;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_hunt);

        mUserListView = (ListView) findViewById(R.id.user_hunt_list);
        mUserDeleteButton = (Button) findViewById(R.id.delete_hunt);
        mUserCheatButton = (Button) findViewById(R.id.cheat_button);


        mLocalStorage = new LocalStorage(this);
        mFirebase = new Firebase(mLocalStorage);

        mUserHuntInfo = new HashMap();

        Intent intent = getIntent();

        mUserHuntInfo = (HashMap) intent.getSerializableExtra("hashMap"); //TODO iterate over this and pull the information out.


        /*so each key has an arraylist of values so this was the only way I could think of to pull out all values of each
        * arraylist(place), main problem is that this way each hunt can only have one place otherwise variables just get overwritten and
        * then sent to configureGeofence().  However, this does work and I was able to get Log.d messages in my log for everything necessary
        * but I couldnt get it to make the Toast that I entered the geofence, just the Log message.*/
        Set keys = mUserHuntInfo.keySet();
        for(Iterator i = keys.iterator(); i.hasNext();){
            String kEy = (String) i.next();
            ArrayList<Item> value = (ArrayList<Item>) mUserHuntInfo.get(kEy);//breaks
            for(Item item: value){

                double lat1 = item.getLat();
                double lon1 = item.getLon();
                String tag1 = item.getPlaceName();

                tag = tag1;
                lat = lat1;
                lon = lon1;
            }
            Toast.makeText(this, "lat=" + lat + " lon=" + lon + " tag=" + tag, Toast.LENGTH_LONG).show();
            Log.d(TAG, kEy + " = " + value);

        }

//        Log.d(TAG, hunt.toString());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }






        /*
        ScavengerHunt hunt = savedInstanceState.getParcelable("HUNT");
        Intent launchIntent = getIntent();
        ScavengerHunt hunt = launchIntent.getParcelableExtra("HUNT");

        Log.d(TAG, hunt.toString());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //Listen to Firebase database, where GeoFence events are stored
        Firebase firebase = new Firebase(mLocalStorage);
        firebase.beNotifiedOfGeoFenceEvents(this);

        */

        //set up geofences

        //monitor geofences

        //Use geofence *service* that updates the Firebase DB as locations are found
        //send update to Firebase every time a place is found - for this user- for this hunt

        //from this Activity, run a query that monitors the users scavenger hunt
        //if user finds all places, do something special
        //Callback for GoogleApiClient. If connected to GoogleAPIclient successfully, set up GeoFences.
    }




        @Override
        public void onConnected (@Nullable Bundle bundle){
            Log.d(TAG, "onConnected");
            configureGeoFence();
        }


        //Callback for GoogleApiClient
        @Override
        public void onConnectionSuspended ( int i){
            Log.d(TAG, "onConnectionSuspended" + i);
        }

        //Callback for GoogleApiClient
        @Override
        public void onConnectionFailed (@NonNull ConnectionResult connectionResult){
            Log.d(TAG, "onConnectionFailed " + connectionResult);
        }


        // Connect and disconnect from GoogleAPIClient as app starts and stops.
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void configureGeoFence() {//(double lat, double lon, float radius, String tag)
        //Create a new GeoFence. Configure the GeoFence using a Builder. See documentation for setting options
        //Can create many GeoFences, differentiate by the requestId String. The lat+lon could be replaced with user input.






            Geofence geoFence = new Geofence.Builder()
                    .setRequestId(tag)    //identifies your GeoFence, put a unique String here
                    .setCircularRegion(
                            lat,        // latitude of MCTC
                            lon,     // longitude of MCTC
                            radius                 //radius of circle, in meters. Documentation recommends 100 meters as a minimum.
                    )
                    .setExpirationDuration(60 * 60 * 1000)      //Valid for an hour
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)  // Want to be notified when user enters and exits the GeoFence
                    .build();




        // Create a GeoFencingRequest to contain the GeoFence. Later, you'll use this to request LocationServices monitor your GeoFence.

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)  // this defines what should happen when the GeoFenceRequest is registered. If INITIAL_TRIGGER_ENTER is set, then a GeoFence event will be created if the device is currently inside the GeoFence.
                .addGeofence(geoFence)
                .build();

        //What happens when user arrives in the GeoFence? Need an Intent to start a service in your app that will respond to this event
        //We've used Intents to start Activities. But, that's not recommended here because user may or may not have a particular Activity open when
        //the GeoFence event happens. Better to start a Service, which is like a background task that belongs to this app, that will receive a process the GeoFence event
        //for example, save it to a database, or create a notification. If the Service saves the GeoFence event to Firebase,
        // then your app's Activity or Fragment can have a Firebase database listener that gets called when the data in the
        // Firebase DB is updated, and if the app Activity is running, then it can update in response.

        //Create a regular intent
        Intent geoFenceIntent = new Intent(this, GeoFenceService.class);
        //Wrap it in a pending intent - an intent that can be used to send the Intent at a later time. The Geofence will use this to start the Service
        PendingIntent geoFencePendingIntent = PendingIntent.getService(this, 0, geoFenceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Required to double-check location permission
        int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {

            //If we have permission, request Locationservices monitors this GeoFence.

            //And then can add the GeoFence to location services. Provide the details of the GeoFence and the pending intent
            //LocationServices will invoke the PendingIntent if the device enters the GeoFence.
            // setResultCallback is for success or failure of adding the request to monitor the GeoFence. A GeoFence may not be added if the device does not have location enabled.

            Log.d(TAG, "Adding GeoFence to LocationServices");

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    geofencingRequest,
                    geoFencePendingIntent
            ).setResultCallback(this);        //What object to call when GeoFence has been added (or error when adding?)

        } else {

            //This app has not been granted permission. On newer Androids, this will cause a dialog box to open to request permission.

            Log.d(TAG, "app requires location permission");

            //Request permission from user

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        }

    }

    //Callback for adding a GeoFence.

    @Override
    public void onResult(@NonNull Status status) {
        if (status.isSuccess()) {
            Log.d(TAG, "GeoFence added");
        } else {
            Log.d(TAG, "Error adding GeoFence status " + status.getStatusCode());
        }
    }


    //Callback for requesting location permissions.

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureGeoFence();
            }
        }

    }


    //Callback from Firebase. In real app, you probably just want to get the most recent, or you'd want to filter in some way.
    @Override
    public void newGeoFenceEventMessages(ArrayList<ScavengerHunt> messages) {

        for (ScavengerHunt message : messages) {

            //real app - do something more useful
            //Toast.makeText(this, , Toast.LENGTH_LONG).show();
            Log.d(TAG, message.toString());
        }

    }


}

