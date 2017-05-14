package com.example.neha.appsdontlie_capstonestage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.FloatRange;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.List;

/**
 * Created by neha on 5/12/17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private Fragment frag;
    private List<MyProfileData> readData;

    public RecyclerViewAdapter(Fragment fragment,List<MyProfileData> dataList){
        this.frag = fragment;
        this.readData = dataList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_card_view,parent,false);


        RecyclerViewHolder viewHolder = new RecyclerViewHolder(frag,view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
      //  Context mycontext = holder.imageViewPhoto.getContext();

         holder.showData(readData.get(position));
        holder.textViewRank.setText(String.valueOf(getItemCount()-position));



    }

    @Override
    public int getItemCount() {
        return readData.size();
    }



}

class RecyclerViewHolder  extends RecyclerView.ViewHolder{

     TextView textViewRank;
     ImageView imageViewPhoto;
     TextView textViewName;
     TextView textViewSteps;
     Fragment fragment;

    public RecyclerViewHolder(Fragment frag,View itemView) {
        super(itemView);
        this.fragment = frag;
        initViews(itemView);



    }

    public void showData(MyProfileData data){

        textViewName.setText(data.getName());
        textViewSteps.setText(data.getSteps());
        Picasso.with(fragment.getContext())
                .load(data.getNewUrl())
                .placeholder(R.drawable.profile_pic)
                .error(R.drawable.profile_pic)
                .fit()
                .into(imageViewPhoto);




    }

    private void initViews(View view){


        textViewName = (TextView) view.findViewById(R.id.name_view);
        textViewRank = (TextView) view.findViewById(R.id.rank);
        textViewSteps = (TextView) view.findViewById(R.id.steps_count);
        imageViewPhoto = (ImageView) view.findViewById(R.id.thumbnail);




    }

}


