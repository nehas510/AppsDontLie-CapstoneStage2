package com.example.neha.appsdontlie_capstonestage2.loaders;

import android.content.Context;
import android.support.v4.content.Loader;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neha on 5/22/17.
**/

public class FirebaseDataLoaders extends Loader<List<MyProfileData>> implements ValueEventListener {

    private DatabaseReference mDbReference;

    public FirebaseDataLoaders(Context context) {
        super(context);
        mDbReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mDbReference.addListenerForSingleValueEvent(this);
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        mDbReference.removeEventListener(this);
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mDbReference.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<MyProfileData> dataList = new ArrayList<>();
        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            dataList.add(childSnapshot.getValue(MyProfileData.class));
        }
        deliverResult(dataList);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        deliverCancellation();
    }
}


