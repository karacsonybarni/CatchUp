package com.udacity.catchup.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.ui.postview.PostViewPopulator;

public class PostWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        PostIntentService.startActionUpdateWidget(context);
    }

    static void updateWidgets(Context context,
                              AppWidgetManager appWidgetManager,
                              Post post,
                              int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, post, appWidgetId);
        }
    }

    private static void updateWidget(Context context,
                                     AppWidgetManager appWidgetManager,
                                     Post post,
                                     int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.post_widget);
        populateRemoteViews(context, views, post);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    private static void populateRemoteViews(Context context, RemoteViews views, Post post) {
        if (post != null) {
            new PostViewPopulator(context, views).updatePost(post);
        }
    }
}

