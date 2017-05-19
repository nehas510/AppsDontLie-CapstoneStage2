package com.example.neha.appsdontlie_capstonestage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.neha.appsdontlie_capstonestage2.adapter.DashBoardRecyclerAdapter;
import com.example.neha.appsdontlie_capstonestage2.adapter.RecyclerViewHolder;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
private View rootView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter mAdapter;
    private  LinearLayoutManager layoutManager;
    private Context context;
    private MyProfileData readData;

    private DataPresenter dPresenter;


   // private OnFragmentInteractionListener mListener;

    public DashboardFragment(DataPresenter presenter) {

        this.dPresenter = presenter;

    }


    public DashboardFragment(){}
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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

        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        updateRecyclerView(rootView);


        return rootView;
    }


    public void updateRecyclerView(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DashBoardRecyclerAdapter(this,MyProfileData.class,R.layout.recycler_card_view, RecyclerViewHolder.class,(dPresenter.getmQueryRef()));
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


    }

    public void openProgressFragment(MyProfileData data,int position, View view) {

        if(data.getUserID().equals(dPresenter.pushID)){
            this.readData = data;

        }

        if (context instanceof MainActivity) {
            MyProgressFragment movieDetail = new MyProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("transitionName", "transition" + position);
            bundle.putSerializable("movie", data);

            movieDetail.setArguments(bundle);
            ((MainActivity)context).showFragmentWithTransition(this, movieDetail, "movieDetail", view, "transition" + position);
        }
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
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
