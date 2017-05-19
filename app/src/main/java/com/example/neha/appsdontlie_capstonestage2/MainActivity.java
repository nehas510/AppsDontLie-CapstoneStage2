package com.example.neha.appsdontlie_capstonestage2;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;
import com.example.neha.appsdontlie_capstonestage2.widget.ScoreWidgetProvider;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    private BottomNavigationView navigation;
    private DataPresenter mPresenter;
    private MyProfileData profileData;

    private boolean isTab;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new DataPresenter(this);
        mPresenter.initFirebase();
        mPresenter.showProgress();
        mPresenter.callChildListener(new DataPresenter.MyPresenterCallback() {
            @Override
            public void onSuccess(MyProfileData data) {
              //  updateWidgets();
                profileData = data;

                if (findViewById(R.id.list_container) != null) {

                    isTab = true;

                    if (savedInstanceState == null) {
                        mPresenter.hideProgress();


                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.content, new HomeFragment(mPresenter, data)).commit();

                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.list_container, new DashboardFragment(mPresenter)).commit();
                    }
                } else {
                    if (savedInstanceState == null) {
                        mPresenter.hideProgress();

                        isTab = false;

                        getSupportFragmentManager().beginTransaction().
                                replace(R.id.content, new HomeFragment(mPresenter, data)).commit();
                        profileData = data;
                        setViews();

                    }

                }
            }

            @Override
            public void onFailure(DatabaseError error) {


            }
        });

    }

    private void setViews() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        fragmentManager = getSupportFragmentManager();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navigation_home:
                        fragment = new HomeFragment(mPresenter, profileData);
                        break;
                    case R.id.navigation_dashboard:
                        fragment = new DashboardFragment(mPresenter);
                        break;
                    case R.id.navigation_settings:
                        fragment = new MySettingsFragment(mPresenter, profileData);
                        break;

                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment).commit();
                return true;
            }


        });


    }


    public void showFragmentWithTransition(Fragment current, Fragment newFragment, String tag, View sharedView, String sharedElementName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // check if the fragment is in back stack
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0);
        if (fragmentPopped) {
            // fragment is pop from backStack
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                current.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
                current.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));

                newFragment.setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.default_transition));
                newFragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (isTab) {
                fragmentTransaction.replace(R.id.list_container, newFragment, tag);
            } else {
                fragmentTransaction.replace(R.id.content, newFragment, tag);
            }
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.addSharedElement(sharedView, sharedElementName);
            fragmentTransaction.commit();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        mPresenter.onResumeEvent();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mPresenter.onPauseEvent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (isTab)
            getMenuInflater().inflate(R.menu.main_menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(isTab) {
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.settings) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.list_container, new MySettingsFragment(mPresenter, profileData))
                        .commit();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateWidgets() {
        ComponentName name = new ComponentName(this, ScoreWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);
        Intent intent = new Intent(this, ScoreWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(AppWidgetManager.ACTION_APPWIDGET_UPDATE, ids);
        sendBroadcast(intent);
    }


}