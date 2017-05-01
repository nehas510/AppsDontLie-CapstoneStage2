package com.example.neha.appsdontlie_capstonestage2.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;

import java.util.ArrayList;

/**
 * Created by neha on 5/1/17.
 */

public class MyProfileDataAdapter extends ArrayAdapter<MyProfileData> {
    public MyProfileDataAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
