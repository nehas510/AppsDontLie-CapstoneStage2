package com.example.neha.appsdontlie_capstonestage2;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mStepCounts;
    private TextView mName;
    private TextView mCalories;
    private TextView mWeight;
    private TextView mHeight;
    private TextView mGender;
    private ImageView mPhoto;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyProfileData data;
    private DataPresenter hPresenter;


    private OnFragmentInteractionListener mListener;

    public HomeFragment(DataPresenter presenter,MyProfileData data){
        this.hPresenter = presenter;
        this.data = data;
    }

public HomeFragment(){}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(rootView);
        showData(data);
        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViews(View view){

        mStepCounts = (TextView) view.findViewById(R.id.step_count);
        mName =       (TextView) view.findViewById(R.id.name_first);
        mCalories =   (TextView) view.findViewById(R.id.calories_count);
        mWeight =     (TextView) view.findViewById(R.id.weight_value);
        mHeight =     (TextView) view.findViewById(R.id.height_value);
        mGender =     (TextView) view.findViewById(R.id.gender_value);
        mPhoto =      (ImageView) view.findViewById(R.id.imageViewShow);

          view.findViewById(R.id.share_fab_rank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(view, "Share with your friends", Snackbar.LENGTH_LONG);
                snackbar.show();
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("I took "+ data.getSteps() + " steps today " +"\n-From Apps Dont Lie")
                        .getIntent(), getString(R.string.action_share)));
            }
        });


    }

    private void setPhoto(String url){

            Picasso.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.profile_pic)
                    .error(R.drawable.profile_pic)
                    .fit()
                    .into(mPhoto);

    }

    private void showData(MyProfileData data){

        mStepCounts.setText(data.getSteps());
        mName.setText(data.getName());
        mCalories.setText(data.getCalories());
        mWeight.setText(data.getWeight());
        mHeight.setText(data.getHeight());
        mGender.setText(data.getGender());
        setPhoto(data.getNewUrl());


    }



}
