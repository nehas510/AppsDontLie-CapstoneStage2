package com.example.neha.appsdontlie_capstonestage2.adapter;

import android.support.v4.app.Fragment;
import android.widget.RemoteViews;

import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

/**
 * Created by neha on 5/17/17.
 */

public class DashBoardRecyclerAdapter extends FirebaseRecyclerAdapter<MyProfileData,RecyclerViewHolder> {

    private Fragment fragment;
    private MyProfileData data;


    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public DashBoardRecyclerAdapter(Fragment fragment, Class<MyProfileData> modelClass, int modelLayout, Class<RecyclerViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);

        this.fragment = fragment;
    }

    @Override
    protected void populateViewHolder(RecyclerViewHolder viewHolder, MyProfileData model, int position) {

        viewHolder.textViewRank.setText(String.valueOf(getItemCount() - position));
        viewHolder.shareDataInTransition(fragment,model,position);

    }
}
