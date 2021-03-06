package com.bignerdranch.android.scavengerhunttemp;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
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


public class ActiveHuntActivity extends ListActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>, Firebase.GeoFenceEventCallback, Firebase.getUserHuntList {

    Firebase mFirebase;
    GoogleApiClient mGoogleApiClient;
    LocalStorage mLocalStorage;

    HashMap mUserHuntInfo;

    ArrayList<ScavengerHunt> mhuntListTemp;


    int REQUEST_LOCATION_PERMISSION = 0;

    private static final String TAG = "Active hunt activity";


    ListView mUserListView;
    Button mUserDeleteButton;
    Button mUserCheatButton;
    TextView mUserScoreViewer;


    private ArrayList<Item> mUserPlaceData;
    private String mHuntName;
    private int mHuntScore;
    int isFound;
    private String userSelection;
    private String userSelectionName; // For cheating purposes.

    private String scoreText = "Your current score is: ";

    private ArrayList mUserAllLocations;

    private static final float radius = 500;


    String tag;
    double lat;
    double lon;
    String found;

    int mUserScore; // current score

    int mUserMaxScore; // max score

    String mScoreTextCombined; // This is the score for the User's textview, it allows it to be modified on the fly.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_hunt);

        mUserListView = getListView();
        mUserListView.setChoiceMode(mUserListView.CHOICE_MODE_MULTIPLE);
        mUserDeleteButton = (Button) findViewById(R.id.delete_hunt);
        mUserCheatButton = (Button) findViewById(R.id.cheat_button);
        mUserScoreViewer = (TextView) findViewById(R.id.user_hunt_score);


        mLocalStorage = new LocalStorage(this);
        mFirebase = new Firebase(mLocalStorage);

        mUserHuntInfo = new HashMap();
        mUserPlaceData = new ArrayList<Item>();

        Intent intent = getIntent();

        mUserHuntInfo = (HashMap) intent.getSerializableExtra("hashMap");

        // Get firebase data here so you can add a callback to THIS class.
        // Right now, when your Scavenger Hunt updates in firebase,
        // for example when you trip a geofence, MainActivity gets the callback. Which is no use to this Activity.

        mFirebase.getUserHunts(this);  //get this user's current hunt


        for (Object key : mUserHuntInfo.keySet()) {

            mHuntName = key.toString();

        }



        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        setupView();

    }


    //Callback from firebase.getUserHunts()

    @Override
    public void huntList(ArrayList<ScavengerHunt> huntNames) {

        if (huntNames.size() == 0) {

            System.out.println("no user list found");

        } else {

            Log.d(TAG, "callback from firebase " + huntNames);

            //an arraylist of size 1, right? with the user's current hunt?
            ScavengerHunt hunt = huntNames.get(0);


            mUserPlaceData.clear();
            mUserScore = 0;

            mHuntName = hunt.getHuntName();

            for (Item item : hunt.getPlaces()) {

                tag = item.getPlaceName();

                found = item.getLocationFound();

                if (found.equalsIgnoreCase("yes")) {

                    mUserScore = mUserScore + 1;

                }

                mUserPlaceData.add(item);


                mUserMaxScore = mUserPlaceData.size();
                if (mUserScore == mUserMaxScore) {
                    Toast.makeText(this, "You've completed the hunt!!", Toast.LENGTH_LONG).show();

                    //                mFirebase.deleteUserHunt();
                    //                mLocalStorage.writeUserHunt(null);
                    //                setResult(RESULT_OK);
                    //                finish();

                }


                mFirebase.beNotifiedOfGeoFenceEvents(this);


            }


            ActiveHuntListViewAdapter adapter = new ActiveHuntListViewAdapter(this, android.R.layout.simple_list_item_checked, mUserPlaceData);
            setListAdapter(adapter); // Listview with checkboxes.

            mScoreTextCombined = scoreText + mUserScore;

            mUserScoreViewer.setText(mScoreTextCombined);


        }
    }

    private void setupView() {

        // This section sets up the Listview for the activity.



        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                userSelection = mUserListView.getItemAtPosition(i).toString();

                Item item = (Item) mUserListView.getItemAtPosition(i);

                userSelectionName = item.getPlaceName();


            }
        });

        mUserCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (userSelectionName == null) {

                    Toast.makeText(getApplicationContext(), "You need to select a route first",Toast.LENGTH_LONG).show();

                } else {

                    Set keys = mUserHuntInfo.keySet();

                    for (Iterator i = keys.iterator(); i.hasNext(); ) {

                        String kEy = (String) i.next();

                        ArrayList<Item> value = (ArrayList<Item>) mUserHuntInfo.get(kEy);

                        for (Item item : value) {

                            tag = item.getPlaceName();

                            if (tag.equalsIgnoreCase(userSelectionName)) {

                                lat = item.getLat();
                                lon = item.getLon();

                                Geocoder geocoder = new Geocoder(ActiveHuntActivity.this);

                                String geoUriString = String.format("geo:%f,%f", lat, lon);

                                Uri geoUri = Uri.parse(geoUriString);

                                Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);

                                startActivity(mapIntent);

                            }
                        }
                    }
                }
            }
        });


        mUserDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebase.deleteUserHunt();
                mLocalStorage.writeUserHunt(null);
                setResult(RESULT_OK);
                finish();

            }
        });


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


    // Configures the Geofence, it will do this for multiple locations.

    private void configureGeoFence() {

        //(double lat, double lon, float radius, String tag)
        //Create a new GeoFence. Configure the GeoFence using a Builder. See documentation for setting options
        //Can create many GeoFences, differentiate by the requestId String. The lat+lon could be replaced with user input.

        double lat1 = 0;
        double lon1 = 0;
        String tag1 = "";

        Set keys = mUserHuntInfo.keySet();

        for(Iterator i = keys.iterator(); i.hasNext();){

            String kEy = (String) i.next();

            ArrayList<Item> value = (ArrayList<Item>) mUserHuntInfo.get(kEy);//breaks

            for(Item item: value){


                lat1 = item.getLat();
                lon1 = item.getLon();
                tag1 = item.getPlaceName();

                Geofence geoFence = new Geofence.Builder()
                        .setRequestId(tag1)    //identifies your GeoFence, put a unique String here
                        .setCircularRegion(
                                lat1,        // latitude
                                lon1,     // longitude
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

                geoFenceIntent.putExtra("huntNmae", mHuntName);
                //Wrap it in a pending intent - an intent that can be used to send the Intent at a later time. The Geofence will use this to start the Service
                PendingIntent geoFencePendingIntent = PendingIntent.getService(this, 0, geoFenceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //Required to double-check location permission
                int permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

                if (permission == PackageManager.PERMISSION_GRANTED) {

                    //If we have permission, request LocationServices monitors this GeoFence.

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

            Log.d(TAG, kEy + " = " + value);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_OK);
        finish();


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

        // Figure out a way to update score from here.
        System.out.println("testing");



    }



}

