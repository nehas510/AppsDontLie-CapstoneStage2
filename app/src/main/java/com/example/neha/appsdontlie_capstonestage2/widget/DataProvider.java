package com.example.neha.appsdontlie_capstonestage2.widget;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String NAME = "name";
    private static final String STEPS = "steps";

    private ArrayList<MyProfileData> mChoreList = new ArrayList<>();
    private Context mContext = null;
    private Intent mIntent;

    DataProvider(Context context, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
    }

    private void populateChoreWidgetList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChoreList.clear();
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    mChoreList.add(choreSnapshot.getValue(MyProfileData.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return mChoreList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_layout);

        String choreWidgetName = mChoreList.get(position).getName();

        String choreWidgetChoreSteps = mChoreList.get(position).getSteps();


        remoteView.setTextViewText(R.id.name_view_wigdet, choreWidgetName);
        remoteView.setTextViewText(R.id.steps_count_wigdet, choreWidgetChoreSteps);

        mIntent.putExtra(NAME, choreWidgetName);
        mIntent.putExtra(STEPS, choreWidgetChoreSteps);


        remoteView.setOnClickFillInIntent(R.id.card_layout_wigdet, mIntent);
        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        populateChoreWidgetList();
    }

    @Override
    public void onDestroy() {

    }
}