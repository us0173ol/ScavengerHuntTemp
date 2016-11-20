package com.bignerdranch.android.scavengerhunttemp;

import android.widget.CheckBox;

/**
 * Created by Brendon on 11/19/16.
 */

// This class is for the custom List in the Hunt activity and management of checkbox.

public class userHuntItemList {


    String mPlaceName;
    boolean mIsChecked;





    public userHuntItemList(String place, boolean isChecked) {

        this.mPlaceName = place;
        this.mIsChecked = isChecked;



    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        mPlaceName = placeName;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }


}
