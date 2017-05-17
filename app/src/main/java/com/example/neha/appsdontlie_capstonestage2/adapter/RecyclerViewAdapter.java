package com.example.neha.appsdontlie_capstonestage2.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.DashboardFragment;
import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
    public void onBindViewHolder(final RecyclerViewHolder holder,final int position) {

        holder.textViewRank.setText(String.valueOf(getItemCount()-position));
        holder.shareDataInTransition(readData.get(position),position);


    }

    @Override
    public int getItemCount() {
        return readData.size();
    }



}

class RecyclerViewHolder  extends RecyclerView.ViewHolder{

     TextView textViewRank;
     ImageView thumbnail;
     TextView textViewName;
     TextView textViewSteps;
     Fragment fragment;
     int pos = -1;

    public RecyclerViewHolder(Fragment frag,View itemView) {
        super(itemView);
        this.fragment = frag;
        initViews(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashboardFragment)fragment).openMovieDetailFragment(getAdapterPosition(), v.findViewById(R.id.thumbnail));


            }
        });

    }


    public void shareDataInTransition(final MyProfileData readData,final int position){
        pos = position;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if ((bitmap != null) && (pos == position))
                    thumbnail.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                if ((errorDrawable != null) && (pos == position))
                   thumbnail.setImageDrawable(errorDrawable);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if ((placeHolderDrawable != null) && (pos == position))
                   thumbnail.setImageDrawable(placeHolderDrawable);
            }
        };

        thumbnail.setTag(target);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thumbnail.setTransitionName("transition" + position);
        }

        if (!TextUtils.isEmpty(readData.getNewUrl())) {
            Picasso.with(fragment.getContext()).load(readData.getNewUrl()).into(target);
        } else {
           thumbnail.setImageDrawable(null);
        }

       textViewName.setText(readData.getName());
        textViewSteps.setText(readData.getSteps());


    }

  /*  public void showData(MyProfileData data){

        textViewName.setText(data.getName());
        textViewSteps.setText(data.getSteps());
        Picasso.with(fragment.getContext())
                .load(data.getNewUrl())
                .placeholder(R.drawable.profile_pic)
                .error(R.drawable.profile_pic)
                .fit()
                .into(imageViewPhoto);




    }
**/
    private void initViews(View view){


        textViewName = (TextView) view.findViewById(R.id.name_view);
        textViewRank = (TextView) view.findViewById(R.id.rank);
        textViewSteps = (TextView) view.findViewById(R.id.steps_count);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);




    }

}


