package com.udacity.catchup.ui.postview;

import android.app.PendingIntent;
import android.content.Context;
import android.view.View;

public interface PostViewPopulatorDelegate {
    Context getContext();
    void loadSubredditIcon(String iconUrl);
    void hideSubredditIconAndUpdateLayout();
    void fillSubredditName(String subredditName);
    void fillPostDetails(String postDetails);
    void fillTitle(String title);
    void fillBodyText(String bodyText);
    void showImage(String imageUrl);
    void addLink(String link, View.OnClickListener onClickListener);
    void setConstraintsUpdater(Runnable constraintsUpdater);
    void setOnClickIntent(PendingIntent intent);
}
