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
    private ImageView subredditIconCompat;
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
        LayoutInflater.from(getContext()).inflate(getLayoutResourceId(), this);
        subredditName = findViewById(R.id.subredditName);
        initSubredditIcon();
        postDetails = findViewById(R.id.postDetails);
        title = findViewById(R.id.title);
        bodyText = findViewById(R.id.bodyText);
        image = findViewById(R.id.image);
        playerView = findViewById(R.id.playerView);
        setPadding();
    }

    int getLayoutResourceId() {
        return R.layout.layout_post;
    }

    private void initSubredditIcon() {
        View iconView = findViewById(R.id.icon);
        if (iconView instanceof CircleImageView) {
            subredditIcon = (CircleImageView) iconView;
        } else {
            subredditIconCompat = (ImageView) iconView;
        }
    }

    private void setPadding() {
        if (subredditName != null) {
            int padding = getResources().getDimensionPixelSize(R.dimen.postView_padding);
            setPadding(padding, padding, padding, padding);
        }
    }

    public void updatePost(Post post) {
        this.post = post;
        populateViews();
    }

    private void populateViews() {
        loadSubredditIcon();
        fillSubredditName();
        fillPostDetails();
        fillTitle();
        addBody();
    }

    public void loadSubredditIcon() {
        Subreddit subreddit = post.getSubreddit();
        if (subreddit != null) {
            if (subredditIcon != null) {
                subredditIcon.load(subreddit.getIconUrl());
            } else if (subredditIconCompat != null) {
                Picasso.get().load(subreddit.getIconUrl()).into(subredditIconCompat);
            }
        }
    }

    private void fillSubredditName() {
        if (subredditName != null) {
            subredditName.setText(post.getSubredditName());
        }
    }

    private void fillPostDetails() {
        if (postDetails != null) {
            postDetails.setText(getPostDetails());
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

    private void fillTitle() {
        if (title != null) {
            title.setText(post.getTitle());
        }
    }

    private void addBody() {
        if (post.hasImage()) {
            loadImage();
        } else if (post.hasVideo()) {
            loadVideo();
        } else if (hasLink()) {
            addLink();
        }
    }

    private void loadImage() {
        showImage();
        Picasso
                .get()
                .load(post.getMediaUrl())
                .resize(2048, 1600)
                .onlyScaleDown()
                .into(image);
    }

    void showImage() {
        image.setVisibility(View.VISIBLE);
    }

    private void loadVideo() {
        showVideo();
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

    private boolean hasLink() {
        String mediaUrl = post.getMediaUrl();
        return mediaUrl != null && !mediaUrl.isEmpty();
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
