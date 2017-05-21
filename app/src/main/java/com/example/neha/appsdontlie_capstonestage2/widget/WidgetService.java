package com.example.neha.appsdontlie_capstonestage2.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by neha on 5/19/17.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return (new DataProvider(this.getApplicationContext(), intent));
    }
}