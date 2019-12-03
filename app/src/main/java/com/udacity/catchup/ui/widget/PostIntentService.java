package com.udacity.catchup.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.util.InjectorUtils;

public class PostIntentService extends IntentService {

    static final String ACTION_UPDATE_POST_WIDGET =
            "com.udacity.catchup.action.update_post_widget";

    public PostIntentService() {
        super("PostIntentService");
    }

    static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, PostIntentService.class);
        intent.setAction(ACTION_UPDATE_POST_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }
        String action = intent.getAction();
        if (ACTION_UPDATE_POST_WIDGET.equals(action)) {
            handleActionUpdatePostWidget();
        }
    }

    private void handleActionUpdatePostWidget() {
        Repository repository = InjectorUtils.getRepository(this);
        Post post = repository.getNextUnseenPost();
        if (post != null) {
            updatePostsSubreddit(repository, post);
            updateWidgets(post);
            repository.setSeen(post);
        }
    }

    private void updatePostsSubreddit(Repository repository, Post post) {
        Subreddit subreddit = repository.getSubredditImmediately(post.getSubredditName());
        post.setSubreddit(subreddit);
    }

    private void updateWidgets(Post post) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName componentName = new ComponentName(this, PostWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        PostWidget.updateWidgets(this, appWidgetManager, post, appWidgetIds);
    }
}
