package com.bignerdranch.android.scavengerhunttemp;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    Firebase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebase = new Firebase();

        getScavengerLists();




    }


    private void getScavengerLists() {

        mFirebase.getAllScavengerLists();



    }
}
