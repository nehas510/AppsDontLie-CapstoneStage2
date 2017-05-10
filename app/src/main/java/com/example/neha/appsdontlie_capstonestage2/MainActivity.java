package com.example.neha.appsdontlie_capstonestage2;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private BottomNavigationView navigation;
    private DataPresenter mPresenter;
    private MyProfileData profileData;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new DataPresenter(this);
        mPresenter.initFirebase();

        if(savedInstanceState == null)
        {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new DashboardFragment())
                        .commit();



        }



    }

public void readData(MyProfileData data){

    this.profileData = data;

    navigation = (BottomNavigationView) findViewById(R.id.navigation);

    fragmentManager = getSupportFragmentManager();

    navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_home:
                    fragment = new HomeFragment(mPresenter,profileData);
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new MyProgressFragment(mPresenter,profileData);
                    break;
                case R.id.navigation_settings:
                    fragment = new MySettingsFragment(mPresenter,profileData);
                    break;

            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content,fragment).commit();
            return true;
        }


    });
}

    @Override
    protected void onResume(){

        super.onResume();
        mPresenter.onResumeEvent();
    }

    @Override
    protected void onPause(){

        super.onPause();
        mPresenter.onPauseEvent();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
