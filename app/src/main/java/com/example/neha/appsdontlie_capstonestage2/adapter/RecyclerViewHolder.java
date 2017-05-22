package com.example.neha.appsdontlie_capstonestage2.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.DashboardFragment;
import com.example.neha.appsdontlie_capstonestage2.MyProgressActivity;
import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class RecyclerViewHolder  extends RecyclerView.ViewHolder{

     public TextView textViewRank;
     ImageView thumbnail;
     TextView textViewName;
     TextView textViewSteps;
     Fragment fragment;
     MyProfileData readData;
     int pos = -1;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        initViews(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ((DashboardFragment)fragment).openProgressFragment(readData,getAdapterPosition(), v.findViewById(R.id.thumbnail));

            }
        });



    }

    public void shareDataInTransition(Fragment fragment,final MyProfileData readData, final int position){
        pos = position;
        this.fragment = fragment;
        this.readData = readData;

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
            Picasso.with(fragment.getContext()).load(readData.getNewUrl()).placeholder(R.drawable.profile_pic).into(target);
        } else {
           thumbnail.setImageDrawable(null);
        }

       textViewName.setText(readData.getName());
        textViewSteps.setText(readData.getSteps());


    }

    private void initViews(View view){


        textViewName = (TextView) view.findViewById(R.id.name_view);
        textViewRank = (TextView) view.findViewById(R.id.rank);
        textViewSteps = (TextView) view.findViewById(R.id.steps_count);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);


    }

}
