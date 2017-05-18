/*package com.example.neha.appsdontlie_capstonestage2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.neha.appsdontlie_capstonestage2.presenter.DataPresenter;


public class SplashScreenActivity  extends AppCompatActivity {
    private Typeface font;
    private TextView text1;
    private Handler handler;
    private Runnable callback;
    private DataPresenter mPresenter;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.primary);



        setContentView(R.layout.activity_splash_screen);

        text1 = (TextView) findViewById(android.R.id.text1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }

}
*/