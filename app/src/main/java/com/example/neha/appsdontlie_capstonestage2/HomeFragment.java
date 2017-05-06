package com.example.neha.appsdontlie_capstonestage2;



import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;


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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyProfileData data, data1;
    private DataPresenter hPresenter;


    private OnFragmentInteractionListener mListener;

    public HomeFragment(DataPresenter presenter){
        this.hPresenter = presenter;
    }

    public  HomeFragment(){

    }

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
       // hPresenter.callChildListener();
        data = hPresenter.loadData();

        // Inflate the layout for this fragment
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

    public void initViews(View view){
        mStepCounts = (TextView) view.findViewById(R.id.step_count);
        mName = (TextView) view.findViewById(R.id.message);
        mCalories = (TextView) view.findViewById(R.id.calories_count);
        mWeight = (TextView) view.findViewById(R.id.weight_value);
        mHeight = (TextView) view.findViewById(R.id.height_value);
        mGender = (TextView) view.findViewById(R.id.gender_value);





    }









    public void showData(MyProfileData data){

        mStepCounts.setText(data.getSteps());
        mName.setText(data.getName());
        mCalories.setText(data.getCalories());

    }

}
