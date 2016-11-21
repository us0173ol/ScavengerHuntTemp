package com.bignerdranch.android.scavengerhunttemp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by clara on 11/21/16.

 You'll need an adapter to manipulate individual list elements

 */



public class ActiveHuntListViewAdapter extends ArrayAdapter<Item> {


	public ActiveHuntListViewAdapter(Context context, int resource, List<Item> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		//set up your list items
		CheckedTextView row = (CheckedTextView) super.getView(position, convertView, parent);

		Item item = getItem(position);

		Log.d("ACTIVE HUNT ADAPTER", "item = " + item);

		row.setText(item.getPlaceName());

		boolean isFound = false;
		if (item.getLocationFound() != null && item.getLocationFound().equals("yes")) {
			isFound = true;
		}

		((ListView)parent).setItemChecked(position, isFound);   //setting the checkbox is awkward - ask the parent ListView to check or uncheck it

		return row;

	}
}
