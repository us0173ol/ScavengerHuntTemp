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
     Firebase.huntListnames {

    Button mStartButton;
    Button mNewHuntButton;
    ListView mHuntListView;
    Button mLaunchActiveHuntScreen;

    Firebase mFirebase;
    GoogleApiClient mGoogleApiClient;


    ArrayList mHuntList;

    private static final String NEW_HUNT_KEY = "new hunt";
    private static final String TAG = "GEOFENCE";


    private static final int NEW_HUNT_CODE = 0;
    private static final int ACTIVE_HUNT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Configure GoogleApiClient. Look at the onConnectionListener for the next part of the app logic
        //GoogleApiClient includes LocationServices, and LocationServices is used to monitor GeoFences.


        mLaunchActiveHuntScreen = (Button)findViewById(R.id.launch_active_hunt_screen);
        mStartButton = (Button) findViewById(R.id.start_hunt_button);
        mHuntListView = (ListView) findViewById(R.id.hunt_list_view);
        mNewHuntButton = (Button) findViewById(R.id.new_hunt_button);

        mFirebase = new Firebase();

        mHuntList = new ArrayList();

        mFirebase.getAllScavengerLists(this);

        mLaunchActiveHuntScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActiveHuntActivity.class);

                startActivityForResult(intent, ACTIVE_HUNT_CODE);
            }
        });


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

                //figure out what was selected in the list

                //get the ScavengertHunt object that was selected

                //start new activity with the selected ScavengerHunt

                //create an example ScavengerHunt.
                // Replace the example with actual hunt once list of hunts, obtained from Firebase, is working

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

            }
        });

    }

    @Override
    public void huntnameList(ArrayList huntNames) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_view, R.id.list_view_text, huntNames);

        mHuntListView.setAdapter(arrayAdapter);

    }
}

