package com.bignerdranch.android.scavengerhunttemp;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button mStartButton;
    ListView mHuntListView;

    Firebase mFirebase;

    ArrayList mHuntList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartButton = (Button) findViewById(R.id.start_hunt_button);
        mHuntListView = (ListView) findViewById(R.id.hunt_list_view);

        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.list_view, R.id.list_view_text);

        mFirebase = new Firebase();

        mFirebase.getAllScavengerLists();







    }



}
