package com.bignerdranch.android.scavengerhunttemp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brendon on 11/8/16.
 */

public class Firebase  {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private Context mContext;

    private ArrayList mHuntList;

    private static final String Scavenger_Lists_Key = "scavenger_hunts";
    private static final String LIST_TAG = "ckascbk";


    public Firebase(){

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();


        }


    public void getAllScavengerLists() {

        Query query = mDatabaseReference.child(Scavenger_Lists_Key);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList arrayList = new ArrayList();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ScavengerHunt huntClass = ds.getValue(ScavengerHunt.class);

                    arrayList.add(huntClass);


                }

                Log.d(LIST_TAG, arrayList.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //return mHuntList;

    }



    //This takes the HashMap of locations and add them to the database as a new Hunt.
    public void addNewHunt(String huntName, HashMap<String, ArrayList> locationData) {

        ScavengerHunt hunt = new ScavengerHunt();

        ArrayList<Item> items = new ArrayList<>();



        for (HashMap.Entry<String, ArrayList> entry : locationData.entrySet()) {

            String place = entry.getKey();

            ArrayList latLong = entry.getValue();

            Item item = new Item(place, (double)latLong.get(0), (double)latLong.get(1));

            items.add(item);


        }

        hunt.setPlaces(items);
        hunt.setHuntName(huntName);

        DatabaseReference newHunt = mDatabaseReference.child(Scavenger_Lists_Key).push();

        newHunt.setValue(hunt);

    }



}




