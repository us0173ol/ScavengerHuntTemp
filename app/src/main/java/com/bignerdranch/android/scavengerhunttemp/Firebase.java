package com.bignerdranch.android.scavengerhunttemp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    private String mUserHuntKey;

    LocalStorage mLocalStorage;

    public Firebase(LocalStorage localStorage){

        this.mLocalStorage = localStorage;
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();


        }


    interface getUserHuntList{

        public void huntList(ArrayList<ScavengerHunt> huntNames);

    }

    // Searches for and returns a list of all the places for the User hunt selection.

    public void getUserHunts(final getUserHuntList callback ) {

        String userName = mLocalStorage.fetchUsername();

        Query query = mDatabaseReference.child(USER_NAME_KEY).child(userName);

        final ArrayList<ScavengerHunt> places = new ArrayList();


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.getChildrenCount() < 2) {

                        break;

                    }
                    else {

                        if (places.size() == 1) {

                            break;

                        }

                        ScavengerHunt scavengerHunt = ds.getValue(ScavengerHunt.class);

                        places.add(scavengerHunt);
                    }

                }

                callback.huntList(places);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }



    // gets all the names of the Hunts..
    interface huntListnames {

        public void huntnameList(ArrayList<ScavengerHunt> huntNames);
    }

    public void getAllScavengerLists(final huntListnames callback) {

        Query query = mDatabaseReference.child(Scavenger_Lists_Key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<ScavengerHunt> arrayList = new ArrayList<ScavengerHunt>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ScavengerHunt huntClass = ds.getValue(ScavengerHunt.class);

                    //String huntName = huntClass.getHuntName();

                    arrayList.add(huntClass);

                }

                callback.huntnameList(arrayList);

                Log.d(LIST_TAG, "Scavenger Hunts from FB database" + arrayList.size() + arrayList.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    // Creates a new User if none available.
    public void addNewUser(NewUser newUser) {

       // NewUser newUser = new NewUser();

        DatabaseReference databaseReference = mDatabaseReference.child(USER_NAME_KEY).push();
        mLocalStorage.writeUsername(databaseReference.getKey());

        //newUser.setUserScore(0);
        //newUser.setCurrentHunt("none");

        mLocalStorage.writeUserHunt(newUser.getCurrentHunt());

        databaseReference.setValue(newUser);

    }

    // Updates the current User hunt.
    public void updateUserHunt(NewUser user, ScavengerHunt hunt) {

        user.mScavengerHunt = hunt;

        String userName = mLocalStorage.fetchUsername();

        DatabaseReference databaseReference = mDatabaseReference.child(USER_NAME_KEY).child(userName);

        databaseReference.setValue(user);

        //databaseReference.setValue(hunt);

    }

    //This will delete the user's current hunt.
    public void deleteUserHunt() {

        String userName = mLocalStorage.fetchUsername();

        DatabaseReference databaseReference = mDatabaseReference.child(USER_NAME_KEY).child(userName).child("mScavengerHunt");

        databaseReference.setValue(null);

        mLocalStorage.writeUserHunt(null);

    }

     // Will change the value of location found to "yes"
    //TODO not working yet.
    public void updateLocationFound(final String placeName) {

        String userName = mLocalStorage.fetchUsername();

        Query query = mDatabaseReference.child(USER_NAME_KEY).child(userName).child("mScavengerHunt").child("places").orderByChild("placeName").equalTo(placeName);
                                                                                                                    //as you had, but order by the placeName child key, which lets you then only select children with placeName equal to your placeName parameter
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //dataSnaphot points to a list of children - your list of places. Hopefully only one match. TODO what if 0 matches, or more than one match for the placeName?

                Log.d(TAG, "Query result ref " + dataSnapshot.getRef());

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                        DatabaseReference ref = child.getRef();   //Points to 0 or 1 or 2 ... in the list, wherever the place is
                        Log.d(TAG, "reference found for this place is " + ref);
                        DatabaseReference locFoundRef = ref.child("locationFound");   //Make new child with key "locationFound", and save the reference to that key
                        locFoundRef.setValue("yes");  //set the value of this key's reference to 'yes'
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }




    //This takes the HashMap of locations and add them to the database as a new Hunt.
    public void addNewHunt(String huntName, HashMap<String, ArrayList> locationData) {

        ScavengerHunt hunt = new ScavengerHunt();

        ArrayList<Item> items = new ArrayList<>();



        for (HashMap.Entry<String, ArrayList> entry : locationData.entrySet()) {

            String place = entry.getKey();

            ArrayList latLong = entry.getValue();

            String locFound = "yes123";

            Item item = new Item(place, (double)latLong.get(0), (double)latLong.get(1), locFound);

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




