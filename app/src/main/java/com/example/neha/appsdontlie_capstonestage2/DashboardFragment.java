package com.example.neha.appsdontlie_capstonestage2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
   // private MyProfileData readData;

    private DataPresenter dPresenter;

    public DashboardFragment(){}
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(DataPresenter presenter) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("presenter",presenter);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            this.dPresenter = (DataPresenter) b.getSerializable("presenter");
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

    /*    if(data.getUserID().equals(dPresenter.pushID)){
            this.readData = data;

        }
*/
        if (context instanceof MainActivity) {
            MyProgressFragment progFrag = new MyProgressFragment();
            Bundle bundle = new Bundle();
            bundle.putString("transitionName", "transition" + position);
            bundle.putSerializable("profileData", data);

            progFrag.setArguments(bundle);
            ((MainActivity)context).showFragmentWithTransition(this, progFrag, "progFrag", view, "transition" + position);
        }
    }

}
