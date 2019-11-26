package com.udacity.catchup.ui.postview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.CircleImageView;

public class PostView extends ConstraintLayout {

    private Post post;
    private boolean shouldUseNewVideoPlayerInstance;

    private TextView subredditName;
    private CircleImageView subredditIcon;
    private TextView postDetails;
    private TextView title;
    private TextView bodyText;
    private ImageView image;
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
        setPadding();
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        subredditName = findViewById(R.id.subredditName);
        subredditIcon = findViewById(R.id.circleIV);
        postDetails = findViewById(R.id.postDetails);
        title = findViewById(R.id.title);
        bodyText = findViewById(R.id.bodyText);
        image = findViewById(R.id.image);
        playerView = findViewById(R.id.playerView);
    }

    private void setPadding() {
        int padding = getResources().getDimensionPixelSize(R.dimen.postView_padding);
        setPadding(padding, padding, padding, padding);
    }

    int getLayoutResourceId() {
        return R.layout.layout_post;
    }

    public void updatePost(Post post) {
        this.post = post;
        populateViews();
    }

    private void populateViews() {
        loadSubredditIcon();
        subredditName.setText(post.getSubredditName());
        postDetails.setText(getPostDetails());
        title.setText(post.getTitle());
        addMedia();
    }

    public void loadSubredditIcon() {
        Subreddit subreddit = post.getSubreddit();
        if (subreddit != null) {
            subredditIcon.load(subreddit.getIconUrl());
        }
    }

    private String getPostDetails() {
        return getContext().getString(R.string.post_details, post.getAuthorName(), getPostTime());
    }

    private String getPostTime() {
        long time = post.getDate();
        long now = System.currentTimeMillis();
        CharSequence timeAgo =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return String.valueOf(timeAgo);
    }

    private void addMedia() {
        if (hasType("image")) {
            loadImage();
        } else if (hasVideo()) {
            loadVideo();
        } else {
            addLink();
        }
    }

    private boolean hasType(String type) {
        String postType = post.getType();
        return postType != null && postType.contains(type);
    }

    private void loadImage() {
        image.setVisibility(View.VISIBLE);
        Picasso
                .get()
                .load(post.getMediaUrl())
                .resize(2048, 1600)
                .onlyScaleDown()
                .into(image);
    }

    public boolean hasVideo() {
        return hasType("video");
    }

    private void loadVideo() {
        playerView.setVisibility(View.VISIBLE);
        String videoUrl = post.getVideoUrl();
        validVideoUrl = videoUrl != null ? videoUrl : post.getMediaUrl();
        if (shouldUseNewVideoPlayerInstance) {
            MediaProvider.initVideoWithNewPlayer(playerView, validVideoUrl);
        } else {
            MediaProvider.initVideo(playerView, validVideoUrl);
        }
    }

    private void addLink() {
        bodyText.setText(post.getMediaUrl());
        bodyText.setOnClickListener(this::openLink);
        bodyText.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        bodyText.setVisibility(View.VISIBLE);
    }

    private void openLink(@SuppressWarnings("unused") View linkView) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getMediaUrl()));
        getContext().startActivity(browserIntent);
    }

    public void playVideo() {
        MediaProvider.playVideo(validVideoUrl);
    }

    public void useNewVideoPlayerInstance() {
        this.shouldUseNewVideoPlayerInstance = true;
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
