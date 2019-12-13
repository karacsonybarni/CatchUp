package com.udacity.catchup.ui.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.ui.postview.PostViewPopulatorDelegate;

import java.io.IOException;

public class RemotePostViewPopulator implements PostViewPopulatorDelegate {

    private Context context;
    private RemoteViews remoteViews;

    public RemotePostViewPopulator(Context context, RemoteViews remoteViews) {
        this.context = context;
        this.remoteViews = remoteViews;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void loadSubredditIcon(String iconUrl) {
        loadImage(R.id.icon, iconUrl);
    }

    private void loadImage(int resId, String url) {
        try {
            remoteViews.setViewVisibility(resId, View.VISIBLE);
            Bitmap imageBitmap =
                    Picasso
                            .get()
                            .load(url)
                            .resize(1024, 0)
                            .onlyScaleDown()
                            .get();
            remoteViews.setImageViewBitmap(resId, imageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideSubredditIconAndUpdateLayout() {

    }

    @Override
    public void fillSubredditName(String subredditName) {
        remoteViews.setTextViewText(R.id.subredditName, subredditName);
    }

    @Override
    public void fillPostDetails(String postDetails) {
        remoteViews.setTextViewText(R.id.postDetails, postDetails);
    }

    @Override
    public void fillTitle(String title) {
        remoteViews.setTextViewText(R.id.title, title);
    }

    @Override
    public void fillBodyText(String bodyText) {
        remoteViews.setTextViewText(R.id.bodyText, bodyText);
        remoteViews.setViewVisibility(R.id.bodyText, View.VISIBLE);
    }

    @Override
    public void showImage(String imageUrl) {
        loadImage(R.id.image, imageUrl);
    }

    @Override
    public void addLink(String link, View.OnClickListener onClickListener) {
        if (link == null) {
            return;
        }
        remoteViews.setTextViewText(R.id.link, link);
        int linkColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        remoteViews.setTextColor(R.id.link, linkColor);
        remoteViews.setViewVisibility(R.id.link, View.VISIBLE);
    }

    @Override
    public void setConstraintsUpdater(Runnable constraintsUpdater) {

    }

    @Override
    public void setOnClickIntent(PendingIntent intent) {
        remoteViews.setOnClickPendingIntent(R.id.postWidget, intent);
    }

    @Override
    public void setOnClickListener(View.OnClickListener listener) {
    }
}
