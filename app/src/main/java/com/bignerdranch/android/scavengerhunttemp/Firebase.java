package com.bignerdranch.android.scavengerhunttemp;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Brendon on 11/8/16.
 */

public class Firebase {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private HuntClass mHuntClass;

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

                    String hunt = ds.getValue().toString();

                    HuntClass huntClass = ds.getValue(HuntClass.class);

                    arrayList.add(huntClass);


                }

                Log.d(LIST_TAG, arrayList.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}




