package com.bignerdranch.android.scavengerhunttemp;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// This class allows the User to build a new Scavenger Hunt.
public class HuntEntryScreen extends AppCompatActivity {

    TextView mHuntTitle;
    EditText mTitleEntry;
    TextView mLocationTitle;
    EditText mLocTitleEntry;
    TextView mAddressTitle;
    EditText mAddressEntry;
    Button mNextEntry;
    Button mCreateHunt;
    TextView mNextTitle;
    TextView mCreateTitle;

    Firebase mFirebase;

    HashMap<String, String> mAddressCollections;
    HashMap<String, ArrayList> mAddressCollectionLatLon;

    private String mHuntTitleTemp;

    private static final String ENTRY_TAG = "entered";
    private static final String CREATE_TAG = "created";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hunt_entry);

        mHuntTitle = (TextView) findViewById(R.id.hunt_title);
        mTitleEntry = (EditText) findViewById(R.id.hunt_title_entry);
        mLocationTitle = (TextView) findViewById(R.id.location_title);
        mLocTitleEntry = (EditText) findViewById(R.id.location_title_entry);
        mAddressTitle = (TextView) findViewById(R.id.address_title);
        mAddressEntry = (EditText) findViewById(R.id.address_entry);
        mNextEntry = (Button) findViewById(R.id.next_button);
        mCreateHunt = (Button) findViewById(R.id.create_hunt_button);
        mNextTitle = (TextView) findViewById(R.id.next_button_title);
        mCreateTitle = (TextView) findViewById(R.id.create_hunt_title);

        mFirebase = new Firebase();

        mAddressCollections = new HashMap<String, String>();
        mAddressCollectionLatLon = new HashMap<String, ArrayList>();


        // Converts addresses to Lat Lon and stores them till the User is ready.
        mNextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mHuntTitleTemp = mTitleEntry.getText().toString();

                String locationTitle = mLocTitleEntry.getText().toString();

                String locationAddress = mAddressEntry.getText().toString();

                mAddressCollections.put(locationTitle, locationAddress);

                mAddressCollectionLatLon = getLatLong(mAddressCollections);

                Toast.makeText(HuntEntryScreen.this, "Entry added.", Toast.LENGTH_LONG).show();

                mLocTitleEntry.setText(""); // clears the entries so the user can continue.
                mAddressEntry.setText("");

                Log.d(ENTRY_TAG, "location added");

            }
        });


        mCreateHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebase.addNewHunt(mHuntTitleTemp, mAddressCollectionLatLon);

                Log.e(CREATE_TAG, "locations added to database");

                Toast.makeText(getApplicationContext(), "Hunt Created!", Toast.LENGTH_LONG).show();

                setResult(RESULT_OK);
                finish();


                //TODO I would like to figure out how to create this, it's throwing a weird error.
                /*
                final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                builder.setTitle("Create Hunt");
                builder.setMessage("Do you wish to create a new Scavenger Hunt?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mFirebase.addNewHunt(mHuntTitleTemp, mAddressCollectionLatLon);

                        setResult(RESULT_OK);
                        finish();

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                */


            }

        });



    }



    // This will allow the user to get the Lat/Lon of multiple locations and return them as a HashMap.
    private HashMap<String, ArrayList> getLatLong(HashMap<String, String> listOfAddresses) {

        Context context = this;

        Geocoder coder = new Geocoder(context);
        List<Address> address;


        HashMap<String, ArrayList> addressesLatLong = new HashMap<String, ArrayList>();


        try {

            for (HashMap.Entry<String, String> entry : listOfAddresses.entrySet()) {

                ArrayList latLong = new ArrayList();

                String locationName = entry.getKey();
                String locationAddress = entry.getValue();

                address = coder.getFromLocationName(locationAddress, 1);

                if (address == null) {

                    return null;
                }

                Address location = address.get(0);

                double lat = location.getLatitude();
                double lon = location.getLongitude();

                latLong.add(lat);
                latLong.add(lon);

                addressesLatLong.put(locationName, latLong);


            }


        } catch (IOException ioe) {

            ioe.printStackTrace();
        }

        return addressesLatLong;

    }


}
