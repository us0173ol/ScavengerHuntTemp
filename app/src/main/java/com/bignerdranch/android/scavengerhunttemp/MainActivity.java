package com.bignerdranch.android.scavengerhunttemp;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button mStartButton;
    Button mNewHuntButton;
    ListView mHuntListView;

    Firebase mFirebase;

    ArrayList mHuntList;

    private static final String NEW_HUNT_KEY = "new hunt";

    private static final int NEW_HUNT_CODE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.start_hunt_button);
        mHuntListView = (ListView) findViewById(R.id.hunt_list_view);
        mNewHuntButton = (Button) findViewById(R.id.new_hunt_button);


        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_view, R.id.list_view_text);

        mHuntListView.setAdapter(arrayAdapter);

        mFirebase = new Firebase();

        mFirebase.getAllScavengerLists();



        mNewHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, HuntEntryScreen.class);

                startActivityForResult(intent, NEW_HUNT_CODE);



            }
        });







    }



}
