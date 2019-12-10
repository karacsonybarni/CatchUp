package com.udacity.catchup.ui.postview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;

public class PostView extends ConstraintLayout {

    private Post post;
    private PostViewPopulator postViewPopulator;
    private OnClickListener onClickListener;
    private boolean shouldUseNewVideoPlayerInstance;

    private PlayerView playerView;

    private String validVideoUrl;

    public PostView(Context context) {
        super(context);
        initViews();
    }

    public PostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        postViewPopulator = new PostViewPopulator(this);
        playerView = findViewById(R.id.playerView);
    }

    int getLayoutResourceId() {
        return R.layout.layout_post;
    }

    public void updatePost(Post post) {
        this.post = post;
        postViewPopulator.updatePost(post);
        if (post.hasVideo()) {
            loadVideo();
        }
    }

    public void loadOrHideSubredditIcon() {
        postViewPopulator.loadOrHideSubredditIcon();
    }

    private void loadVideo() {
        showVideo();
        playerView.setOnClickListener(onClickListener);
        String videoUrl = post.getVideoUrl();
        validVideoUrl = videoUrl != null ? videoUrl : post.getMediaUrl();
        if (shouldUseNewVideoPlayerInstance) {
            MediaProvider.initVideoWithNewPlayer(playerView, validVideoUrl);
        } else {
            MediaProvider.initVideo(playerView, validVideoUrl);
        }
    }

    void showVideo() {
        playerView.setVisibility(View.VISIBLE);
    }

    public void playVideo() {
        MediaProvider.playVideo(validVideoUrl);
    }

    public void useNewVideoPlayerInstance() {
        this.shouldUseNewVideoPlayerInstance = true;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        onClickListener = l;
        postViewPopulator.setOnClickListener(l);
    }

    public boolean hasMedia() {
        return post.hasImage() || post.hasVideo();
    }

    PostViewPopulator getPostViewPopulator() {
        return postViewPopulator;
    }

    public void onPause() {
        if (Util.SDK_INT <= 23 && isFinishing()) {
            MediaProvider.close();
        }
    }

    private boolean isFinishing() {
        Context context = getContext();
        return context instanceof Activity && ((Activity) context).isFinishing();
    }

    public void onStop() {
        if (Util.SDK_INT > 23 && isFinishing()) {
            MediaProvider.close();
        }
    }
}
