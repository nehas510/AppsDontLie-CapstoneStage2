package com.example.neha.appsdontlie_capstonestage2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProgressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mBefore;
    private TextView mAfter;
    private boolean isButtonLongPressed = false;
  //  private DataPresenter pPresenter;
    private ImageButton mImageViewBefore , mImageViewAfter;
   // private MyProfileData data;

    private OnFragmentInteractionListener mListener;

    public MyProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProgressFragment newInstance(String param1, String param2) {
        MyProgressFragment fragment = new MyProgressFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_progress, container, false);
      //  data = pPresenter.loadData();
        initViews(rootView);
        Bundle b = getArguments();
        if (b != null) {
            String transitionName = b.getString("transitionName");
            final MyProfileData data = (MyProfileData) b.getSerializable("movie");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mImageViewAfter.setTransitionName(transitionName);
                mAfter.setText("After");
                Picasso.with(getContext())
                        .load(data.getNewUrl())
                        .fit()
                        .into(mImageViewAfter);

                mImageViewAfter.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                         isButtonLongPressed = true;
                        showdata(data);
                        return true;
                    }
                });


                mImageViewAfter.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        v.onTouchEvent(event);
                        // We're only interested in when the button is released.
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            // We're only interested in anything if our speak button is currently pressed.
                            if (isButtonLongPressed) {
                                // Do something when the button is released.
                                mAfter.setText("After");
                                Picasso.with(getContext())
                                        .load(data.getNewUrl())
                                        .fit()
                                        .into(mImageViewAfter);
                                isButtonLongPressed = false;
                            }
                        }
                        return false;
                    }
                });

            }




            //  pPresenter.showProgress();
          //  showdata(data);
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 private void initViews(View view){

     mAfter = (TextView) view.findViewById(R.id.textAfter);
    // mBefore = (TextView) view.findViewById(R.id.textBefore);
     mImageViewAfter = (ImageButton) view.findViewById(R.id.thumbnail);
    // mImageViewBefore = (ImageView) view.findViewById(R.id.imageViewBefore);




     view.findViewById(R.id.share_fab_progress).setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
           //  Bitmap bitMap = overlay(data.getNewUrl(),data.getOldUrl());
             Snackbar snackbar = Snackbar
                     .make(view, "Share with your friends", Snackbar.LENGTH_LONG);
             snackbar.show();
             startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                     .setType("image/jpeg")
                     .getIntent(), getString(R.string.action_share)));
         }
     });
 }

 private void showdata(MyProfileData data){

     mAfter.setText("Before");

     Picasso.with(getContext())
             .load(data.getOldUrl())
             .fit()
             .into(mImageViewAfter);


 }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
