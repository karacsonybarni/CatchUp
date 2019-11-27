package com.udacity.catchup.ui.detailsview;

import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.ui.postview.PostView;

import java.util.Objects;

class MediaDelegate implements DetailsActivityDelegate {

    private AppCompatActivity activity;
    private PostView media;

    MediaDelegate(DetailsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initViews() {
        media = activity.findViewById(R.id.media);
        media.setVisibility(View.VISIBLE);
    }

    @Override
    public void updatePost(Post post) {
        media.updatePost(post);
        if (post.hasVideo()) {
            media.useNewVideoPlayerInstance();
            media.playVideo();
        }
    }

    @Override
    public void initWindow() {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideActionBar();
    }

    private void hideActionBar() {
        getNonNullActionBar().hide();
    }

    private ActionBar getNonNullActionBar() {
        return Objects.requireNonNull(activity.getSupportActionBar());
    }

    @Override
    public void close() {
    }
}
