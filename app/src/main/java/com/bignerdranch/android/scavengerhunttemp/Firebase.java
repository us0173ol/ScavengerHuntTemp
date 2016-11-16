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

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Brendon on 11/8/16.
 */

public class Firebase  {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private Context mContext;

    private ArrayList mHuntList;

    private static final String Scavenger_Lists_Key = "scavenger_hunts";
    private static final String USER_NAME_KEY = "user_names";
    private static final String LIST_TAG = "ckascbk";

    LocalStorage mLocalStorage;

    public Firebase(LocalStorage localStorage){

        this.mLocalStorage = localStorage;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();


        }


    // gets all the names of the Hunts..
    interface huntListnames {

        public void huntnameList(ArrayList huntNames);
    }

    public void getAllScavengerLists(final huntListnames callback) {


        Query query = mDatabaseReference.child(Scavenger_Lists_Key);

        final ArrayList arrayList = new ArrayList();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ScavengerHunt huntClass = ds.getValue(ScavengerHunt.class);

                    String huntName = huntClass.getHuntName();

                    arrayList.add(huntName);


                }

                callback.huntnameList(arrayList);

                Log.d(LIST_TAG, arrayList.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    // Creates a new User if none available.
    public void addNewUser() {

        NewUser newUser = new NewUser();

        DatabaseReference databaseReference = mDatabaseReference.child(USER_NAME_KEY).push();
        mLocalStorage.writeUsername(databaseReference.getKey());

        newUser.setUserName(mLocalStorage.fetchUsername());
        newUser.setCurrentHunt("none");

        mLocalStorage.writeUserHunt(newUser.getCurrentHunt());

        databaseReference.setValue(newUser);

    }

    public void updateUserHunt(String hunt) {


        String userName = mLocalStorage.fetchUsername();

        DatabaseReference databaseReference = mDatabaseReference.child(userName);

        HashMap<String, Object> updateHunt = new HashMap<String,Object>();

        updateHunt.put("currentHunt", hunt);

        databaseReference.updateChildren(updateHunt);

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


    public void addGeoFenceEvent(String event) { mDatabaseReference.push().setValue(event);}

    interface GeoFenceEventCallback  {
        public void newGeoFenceEventMessages(ArrayList<ScavengerHunt> messages);
    }

    public void beNotifiedOfGeoFenceEvents(final GeoFenceEventCallback callback) {
        Query allEvents = mDatabaseReference;    //get all the data. TODO real app will probably filter somehow.

        allEvents.addValueEventListener(new ValueEventListener() {

            //Will be called every time data is changed - in this app, when new GeoFence message is added.

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<ScavengerHunt> messages = new ArrayList<ScavengerHunt>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //messages.add(ds.getValue(ScavengerHunt.class));
                    Log.d(TAG, ds.toString());
                }

                //callback.newGeoFenceEventMessages(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}




