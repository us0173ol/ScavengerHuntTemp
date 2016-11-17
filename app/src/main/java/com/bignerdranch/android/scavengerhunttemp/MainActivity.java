package com.bignerdranch.android.scavengerhunttemp;

import android.app.DownloadManager;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import com.google.firebase.database.Query;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
     Firebase.huntListnames, Firebase.getUserHuntList {

    Button mStartButton;
    Button mNewHuntButton;
    ListView mHuntListView;
    Button mLaunchActiveHuntScreen;

    Firebase mFirebase;
    GoogleApiClient mGoogleApiClient;


    ArrayList mHuntList;

    String mUserName;
    String mUserHunt;

    private static final String NEW_HUNT_KEY = "new hunt";
    private static final String TAG = "GEOFENCE";


    private static final int NEW_HUNT_CODE = 0;
    private static final int ACTIVE_HUNT_CODE = 1;
    int REQUEST_LOCATION_PERMISSION = 0;


    LocalStorage mLocalStorage;


    NewUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLaunchActiveHuntScreen = (Button)findViewById(R.id.launch_active_hunt_screen);
        mStartButton = (Button) findViewById(R.id.start_hunt_button);
        mHuntListView = (ListView) findViewById(R.id.hunt_list_view);
        mNewHuntButton = (Button) findViewById(R.id.new_hunt_button);

        mLocalStorage = new LocalStorage(this);

        mFirebase = new Firebase(mLocalStorage);

        mUserName = mLocalStorage.fetchUsername(); // Gets the User name from Local Storage //todo deal with user objects

        // If one doesn't exist this will create it.
        if (mUserName == null) {
            mUser = new NewUser();
            mUser.setUserScore(0);
            //newUser.setCurrentHunt("none");   //don't need this. currentHunt will be null if you don't define it.  Use null for things that don't exist.

            mFirebase.addNewUser(mUser);

            mUserName = mLocalStorage.fetchUsername();
        }


        mUserHunt = mLocalStorage.fetchUserHunt();


        mHuntList = new ArrayList();

        mFirebase.getAllScavengerLists(this);


        // Works with current hunt if one exists.
        mLaunchActiveHuntScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userSelection = mLocalStorage.fetchUserHunt();

                //Intent intent = new Intent(MainActivity.this, ActiveHuntActivity.class);

                //startActivityForResult(intent, ACTIVE_HUNT_CODE);
            }
        });

        mHuntListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                //if (mUserHunt.equalsIgnoreCase("none")) {

                    String huntSelect = mHuntListView.getItemAtPosition(i).toString();

                    ScavengerHunt hunt = (ScavengerHunt)mHuntListView.getItemAtPosition(i);

                    mLocalStorage.writeUserHunt(huntSelect);

                    Log.d(TAG, "Name of hunt selected " + huntSelect);

                    //Copy hunt to user

                    mFirebase.updateUserHunt(mUser, hunt);


//                } else {
//
//                    Toast.makeText(MainActivity.this, "Sorry, you already have a hunt in progress", Toast.LENGTH_LONG).show();
//
//                }
            }
        });


        // Allows for the creation of a new hunt.
        mNewHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, HuntEntryScreen.class);

                startActivityForResult(intent, NEW_HUNT_CODE);


            }
        });


        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String huntSelection = mLocalStorage.fetchUserHunt();

                if (huntSelection.equalsIgnoreCase("none")) {

                    Toast.makeText(MainActivity.this, "You need to select a Scavenger Hunt.", Toast.LENGTH_SHORT).show();

                } else {

                    ArrayList places = new ArrayList();

                    /*
                    The goal here is to search the scavenger hunt lists and retrieve all the places
                    as an arraylist so that we can iterate over it and copy it to the User list.
                     */
                    mFirebase.getUserHunts(MainActivity.this);

                    Toast.makeText(MainActivity.this, "Thank you" + " " +
                            mLocalStorage.fetchUserHunt(), Toast.LENGTH_LONG).show();
                }

                /*
                ScavengerHunt hunt = new ScavengerHunt();

                ArrayList<Item> items = new ArrayList<>();

                Item item1 = new Item("IDS Center", 43, -90.9);
                Item item2 = new Item("MCTC", 44, -93.9);
                Item item3 = new Item("Loring Park", 42.4, -90);
                Item item4 = new Item("Starbucks", 47.4, -92.3);

                items.add(item1);
                items.add(item2);
                items.add(item3);
                items.add(item4);

                hunt.setPlaces(items);
                hunt.setHuntName("Hunt 1");

                Intent intent = new Intent(MainActivity.this, ActiveHuntActivity.class);

                intent.putExtra("HUNT", hunt);   //todo make constant variable for key

                startActivity(intent);
                */

            }
        });

    }




    @Override
    public void huntnameList(ArrayList<ScavengerHunt> huntNames) {

        ArrayAdapter<ScavengerHunt> arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_view, R.id.list_view_text, huntNames);

        mHuntListView.setAdapter(arrayAdapter);

    }

    @Override
    public void huntList(ArrayList huntNames) {


        Toast.makeText(this, huntNames.toString(), Toast.LENGTH_LONG).show();

    }
}

