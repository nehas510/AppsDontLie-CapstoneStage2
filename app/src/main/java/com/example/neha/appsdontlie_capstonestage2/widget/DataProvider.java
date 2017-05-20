package com.example.neha.appsdontlie_capstonestage2.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final String NAME = "name";
    private static final String STEPS = "steps";
    private static final String PHOTO_URL = "newurl";

    private ArrayList<MyProfileData> mProfileList = new ArrayList<>();
    private Context mContext = null;
    private Intent mIntent;

    DataProvider(Context context, Intent intent) {
        this.mIntent = intent;
        this.mContext = context;
    }

    private void populateWidgetList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProfileList.clear();
                for (DataSnapshot choreSnapshot : dataSnapshot.getChildren()) {
                    mProfileList.add(choreSnapshot.getValue(MyProfileData.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getCount() {
        return mProfileList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.widget_layout);
        if (mProfileList != null) {


            String widgetName = mProfileList.get(position).getName();

            String widgetSteps = mProfileList.get(position).getSteps();
            String widgetThumbnail = mProfileList.get(position).getNewUrl();


            remoteView.setTextViewText(R.id.widget_name_textview, widgetName);
            remoteView.setTextViewText(R.id.widget_steps_textview, widgetSteps);


            mIntent.putExtra(NAME, widgetName);
            mIntent.putExtra(STEPS, widgetSteps);
            mIntent.putExtra(PHOTO_URL, widgetThumbnail);

        }
        remoteView.setOnClickFillInIntent(R.id.widget_single_linear_layout, mIntent);
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
        populateWidgetList();
    }

    @Override
    public void onDestroy() {

    }
}