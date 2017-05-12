package com.example.neha.appsdontlie_capstonestage2;

/**
 * Created by neha on 5/11/17.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

/**
 * Created by neha on 7/14/16.
 */


public class SimpleProgressBar extends Dialog{
    private ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    public SimpleProgressBar(Context context) {
        super(context);
        init(context);
    }


    public static SimpleProgressBar create(Context context) {
        SimpleProgressBar simpleProgressBar = new SimpleProgressBar(context);
        return simpleProgressBar;
    }

    public static SimpleProgressBar show(Context context) {
        SimpleProgressBar simpleProgressBar = SimpleProgressBar.create(context);
        simpleProgressBar.show();
        return simpleProgressBar;
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ProgressBar view = new ProgressBar(context);
        addContentView(view, params);
        setCancelable(false);
    }

}