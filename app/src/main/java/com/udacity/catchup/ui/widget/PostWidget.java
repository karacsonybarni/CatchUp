package com.udacity.catchup.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.postview.PostViewPopulator;
import com.udacity.catchup.util.InjectorUtils;

public class PostWidget extends AppWidgetProvider {

    private Post post;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.post_widget);
        updateWidget(context, views);
        appWidgetManager.updateAppWidget(widgetId, views);
    }

    private void updateWidget(Context context, RemoteViews views) {
        if (post != null) {
            new PostViewPopulator(context, views).updatePost(post);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Repository repository = InjectorUtils.getRepository(context);
        repository.getDiskIO().execute(() -> updatePostAndViews(context, repository));
    }

    private void updatePostAndViews(Context context, Repository repository) {
        updatePost(repository);
        updateViews(context);
    }

    private void updatePost(Repository repository) {
        post = repository.getNextUnseenPost();
        Subreddit subreddit = repository.getSubredditImmediately(post.getSubredditName());
        post.setSubreddit(subreddit);
    }

    private void updateViews(Context context) {
        ComponentName provider = new ComponentName(context, PostWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
    }
}

