package com.example.neha.appsdontlie_capstonestage2;

import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private BottomNavigationView navigation;
    private DataPresenter mPresenter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new DataPresenter(this);
        mPresenter.initFirebase();
        mPresenter.initAuthListener();
      //  mPresenter.mCreateFitnessClientforSteps();


         navigation = (BottomNavigationView) findViewById(R.id.navigation);
           // navigation.inflateMenu(R.menu.navigation);

        fragmentManager = getSupportFragmentManager();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_home:
                        fragment = new HomeFragment(mPresenter);
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.navigation_notifications:
                        fragment = new MyProgressFragment();
                        break;
                    case R.id.navigation_settings:
                        fragment = new MySettingsFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content,fragment).commit();
                return true;
            }


        });


    }


    @Override
    public void onResume(){

        super.onResume();
        mPresenter.onResumeEvent();
    }

    @Override
    public void onPause(){

        super.onPause();
        mPresenter.onPauseEvent();
    }


}
