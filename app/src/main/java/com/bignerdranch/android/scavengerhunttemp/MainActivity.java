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

import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
     Firebase.huntListnames, Firebase.getUserHuntList {

    Button mStartButton;
    Button mNewHuntButton;
    ListView mHuntListView;
    //Button mLaunchActiveHuntScreen;

    Firebase mFirebase;
    GoogleApiClient mGoogleApiClient;
    NewUser mNewUser;


    ArrayList<ScavengerHunt> mHuntList;
    ArrayList<ScavengerHunt> mScavengerHuntArrayList;
    ArrayList mScavengerHuntNamesList;
    HashMap mUserHuntList;              // you all need to work on your variable names

    ArrayAdapter mHuntArrayAdapter;

    String mUserName;
    String mUserHunt;
    double mUserScore;

    private static final String NEW_HUNT_KEY = "new hunt";
    private static final String TAG = "MAINACTIVITY";


    private static final int NEW_HUNT_CODE = 0;
    private static final int ACTIVE_HUNT_CODE = 1;
    int REQUEST_LOCATION_PERMISSION = 0;



    LocalStorage mLocalStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // mLaunchActiveHuntScreen = (Button)findViewById(R.id.launch_active_hunt_screen);
        mStartButton = (Button) findViewById(R.id.start_hunt_button);
        mHuntListView = (ListView) findViewById(R.id.hunt_list_view);
        mNewHuntButton = (Button) findViewById(R.id.new_hunt_button);

        mLocalStorage = new LocalStorage(this);

        mFirebase = new Firebase(mLocalStorage);

        mUserName = mLocalStorage.fetchUsername(); // Gets the User name from Local Storage


        //mLocalStorage.writeUserHunt(null);

        // If one doesn't exist this will create it.
        if (mUserName == null) {

            mNewUser = new NewUser();
            mNewUser.setUserScore(0);

            mFirebase.addNewUser(mNewUser);

            mUserName = mLocalStorage.fetchUsername();
        }


        mHuntList = new ArrayList<ScavengerHunt>();

        mScavengerHuntArrayList = new ArrayList<ScavengerHunt>();

        mScavengerHuntNamesList = new ArrayList();

        mFirebase.getAllScavengerLists(MainActivity.this);

        mFirebase.getUserHunts(MainActivity.this);





        // Changes the current hunt the User is on.
        mHuntListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                String selection = mLocalStorage.fetchUserHunt();

                if (selection == null || selection.equalsIgnoreCase("none")) {

                    String hunt = mHuntListView.getItemAtPosition(i).toString();


                    for (int x = 0; x < mScavengerHuntArrayList.size(); x++) {

                        ScavengerHunt huntSelect = mScavengerHuntArrayList.get(x);

                        String name = huntSelect.getHuntName();

                        List<Item> places = huntSelect.getPlaces();


                        if (name.equalsIgnoreCase(hunt)) {

                            mLocalStorage.writeUserHunt(name);

                            //updateUserCurrentHunt(name, places);


                            if (mNewUser == null) {

                                mNewUser = new NewUser();

                            }

                            mFirebase.updateUserHunt(mNewUser, huntSelect);

                        }

                    }

                } else {


                    Toast.makeText(MainActivity.this, "Sorry, you already have a hunt in progress.", Toast.LENGTH_LONG).show();


                }
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

                //getCurrentUserHunt();


                String huntSelection = mLocalStorage.fetchUserHunt();

                if (huntSelection == null || huntSelection.equalsIgnoreCase("none")) {

                    Toast.makeText(MainActivity.this, "You need to select a Scavenger Hunt.", Toast.LENGTH_SHORT).show();

                } else {

                    Intent intent = new Intent(MainActivity.this, ActiveHuntActivity.class);
                    intent.putExtra("hashMap", mUserHuntList);    ///Why don't you use a ScavengerHunt object??

                    startActivityForResult(intent, ACTIVE_HUNT_CODE);

                }


            }
        });



    }




    public void updateUserCurrentHunt(String huntName, List<Item> places) {


        mUserScore = mLocalStorage.getScore();

        mUserHuntList = new HashMap();

        mUserHuntList.put(huntName, places);   //and if you must use a HashMap, use generic types and give it a better name.




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mLocalStorage = new LocalStorage(this);


        if (requestCode == ACTIVE_HUNT_CODE && resultCode == RESULT_OK) {

            mFirebase.getAllScavengerLists(MainActivity.this);
            mFirebase.getUserHunts(MainActivity.this);

            if (mHuntList.size() == 0) {

                mLocalStorage.writeUserHunt(null);

            }
        }


    }

    @Override
    public void huntnameList(ArrayList<ScavengerHunt> huntNames) {


        this.mScavengerHuntNamesList.clear();
        this.mScavengerHuntArrayList = huntNames;


        Log.d(TAG, "Hunt names = " + huntNames.size()  + " " + huntNames);

        for (int x = 0; x < huntNames.size(); x++) {

            ScavengerHunt item = huntNames.get(x);

            String huntTitle = item.getHuntName();

            mScavengerHuntNamesList.add(huntTitle);
        }

        mHuntArrayAdapter = new ArrayAdapter(this, R.layout.list_view, R.id.list_view_text, mScavengerHuntNamesList);

        mHuntListView.setAdapter(mHuntArrayAdapter);

    }

    @Override
    public void huntList(ArrayList<ScavengerHunt> huntNames) {


        this.mHuntList = huntNames;

        // This should only happen when the isn't using a hunt.
        if (mHuntList.size() == 0) {

            Toast.makeText(this, "You have no current hunt", Toast.LENGTH_SHORT).show();
            mLocalStorage.writeUserHunt(null);

        } else { // Update the current hunt list.

            ScavengerHunt scavengerHunt = huntNames.get(0);

            String name = scavengerHunt.getHuntName();

            mLocalStorage.writeUserHunt(name);

            List<Item> places = scavengerHunt.getPlaces();

            updateUserCurrentHunt(name, places);


        }
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }
}

