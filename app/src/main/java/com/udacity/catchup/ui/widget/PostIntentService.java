package com.udacity.catchup.ui.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.util.InjectorUtils;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class PostIntentService extends IntentService {

    private static final String SERVICE_NAME = PostIntentService.class.getSimpleName();
    static final String ACTION_UPDATE_POST_WIDGET =
            "com.udacity.catchup.action.update_post_widget";

    public PostIntentService() {
        super(SERVICE_NAME);
    }

    public static void startActionUpdateWidget(Context context) {
        Intent intent = new Intent(context, PostIntentService.class);
        intent.setAction(ACTION_UPDATE_POST_WIDGET);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
        Log.d(PostIntentService.class.getSimpleName(), "trying to start service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground();
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startForeground() {
        String channelId = createNotificationChannel();
        if (channelId == null) {
            return;
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        NotificationChannel channel =
                new NotificationChannel(
                        PostIntentService.SERVICE_NAME,
                        PostIntentService.SERVICE_NAME,
                        NotificationManager.IMPORTANCE_NONE);
        NotificationManager service =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (service != null) {
            service.createNotificationChannel(channel);
            return PostIntentService.SERVICE_NAME;
        }
        return null;
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
