package com.example.neha.appsdontlie_capstonestage2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MySettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MySettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DataPresenter sPresenter;
    private Button mSignOut, mSaveChanges;
    private EditText mWeight, mHeight, mGender;

    //  private OnFragmentInteractionListener mListener;

    public MySettingsFragment() {
        // Required empty public constructor
    }

    public MySettingsFragment(DataPresenter presenter) {

        this.sPresenter = presenter;
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MySettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MySettingsFragment newInstance(String param1, String param2) {
        MySettingsFragment fragment = new MySettingsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_my_settings, container, false);
        initViews(rootView);

        mSaveChanges.setOnClickListener(saveChanges);

        mSignOut.setOnClickListener(signOutButton);

        return rootView;
    }


    private View.OnClickListener saveChanges = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pushData();
            Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_SHORT).show();


        }
    };

    private View.OnClickListener signOutButton = new View.OnClickListener() {
        public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();

        }

    };


    private void initViews(View rootView) {

        mSignOut = (Button) rootView.findViewById(R.id.sign_out);
        mSaveChanges = (Button) rootView.findViewById(R.id.save_changes);
        mWeight = (EditText) rootView.findViewById(R.id.weight_fill);
        mHeight = (EditText) rootView.findViewById(R.id.height_fill);
        mGender = (EditText) rootView.findViewById(R.id.gender_fill);


    }


    private void pushData() {

        String weight, height, gender;
        weight = (mWeight.getText().toString());
        height = (mHeight.getText().toString());
        gender = (mGender.getText().toString());

        sPresenter.setData(weight, height, gender);


    }

}

