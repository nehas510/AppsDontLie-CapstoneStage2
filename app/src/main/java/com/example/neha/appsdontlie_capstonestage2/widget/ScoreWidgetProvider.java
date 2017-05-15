package com.example.neha.appsdontlie_capstonestage2.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.neha.appsdontlie_capstonestage2.MainActivity;
import com.example.neha.appsdontlie_capstonestage2.R;

import java.util.Random;

/**
 * Created by neha on 5/15/17.
 */

public class ScoreWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.name_view, number);
            remoteViews.setTextViewText(R.id.rank, number);
            remoteViews.setTextViewText(R.id.steps_count,number);
            remoteViews.setImageViewResource(R.id.thumbnail,R.drawable.ic_share);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.thumbnail, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
