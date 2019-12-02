package com.udacity.catchup.ui.postview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

public class PostView extends ConstraintLayout {

    private Post post;
    private OnClickListener onClickListener;
    private boolean shouldUseNewVideoPlayerInstance;

    private ImageView subredditIcon;
    private ViewGroup headerTexts;
    private TextView subredditName;
    private TextView postDetails;
    private TextView title;
    private TextView bodyText;
    private TextView link;
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
        initSubredditIcon();
        headerTexts = findViewById(R.id.headerTexts);
        if (headerTexts != null) {
            subredditName = headerTexts.findViewById(R.id.subredditName);
            postDetails = headerTexts.findViewById(R.id.postDetails);
        }
        title = findViewById(R.id.title);
        bodyText = findViewById(R.id.bodyText);
        link = findViewById(R.id.link);
        image = findViewById(R.id.image);
        playerView = findViewById(R.id.playerView);
        setPadding();
    }

    int getLayoutResourceId() {
        return R.layout.layout_post;
    }

    private void initSubredditIcon() {
        subredditIcon = findViewById(R.id.icon);
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
        loadOrHideSubredditIcon();
        fillSubredditName();
        fillPostDetails();
        fillTitle();
        fillBodyText();
        addMedia();
    }

    public void loadOrHideSubredditIcon() {
        Subreddit subreddit = post.getSubreddit();
        if (subreddit == null) {
            return;
        }
        if (!subreddit.getIconUrl().isEmpty()) {
            loadSubredditIcon(subreddit);
        } else {
            hideSubredditIconAndUpdateLayout();
        }
    }

    private void loadSubredditIcon(Subreddit subreddit) {
        if (subredditIcon != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                subredditIcon.setClipToOutline(true);
            }
            Picasso.get().load(subreddit.getIconUrl()).into(subredditIcon);
        }
    }

    private void hideSubredditIconAndUpdateLayout() {
        hideSubredditIcon();
        updateLayoutAfterHidingIcon();
    }

    private void hideSubredditIcon() {
        subredditIcon.setVisibility(INVISIBLE);
        ViewGroup.LayoutParams iconLP = subredditIcon.getLayoutParams();
        iconLP.width = 0;
        subredditIcon.setLayoutParams(iconLP);
    }

    private void updateLayoutAfterHidingIcon() {
        LayoutParams headerTextsLP = (LayoutParams) headerTexts.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            headerTextsLP.setMarginStart(0);
        } else {
            headerTextsLP.leftMargin = 0;
        }
        headerTexts.setLayoutParams(headerTextsLP);
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
            title.setText(processHtml(post.getTitle()));
        }
    }

    private Spanned processHtml(String htmlText) {
        String cleanedText = htmlText.replaceAll("&amp;#x200B;", "");
        String textWithLineBreaks = cleanedText.replace("\n", "<br/>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(textWithLineBreaks, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(textWithLineBreaks, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    private void fillBodyText() {
        String text = post.getText();
        if (this.bodyText == null || hasNoText()) {
            return;
        }
        bodyText.setText(processHtml(text));
        bodyText.setOnClickListener(onClickListener);
        bodyText.setVisibility(VISIBLE);
    }

    private boolean hasNoText() {
        String text = post.getText();
        return text == null || text.isEmpty();
    }

    private void addMedia() {
        if (post.hasImage()) {
            loadImage();
        } else if (post.hasVideo()) {
            loadVideo();
        } else if (hasLink() && hasNoText()) {
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

    private boolean hasLink() {
        String mediaUrl = post.getMediaUrl();
        return mediaUrl != null && !mediaUrl.isEmpty();
    }

    private void addLink() {
        if (link == null) {
            return;
        }
        link.setText(post.getMediaUrl());
        link.setOnClickListener(this::openLink);
        link.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        link.setVisibility(View.VISIBLE);
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

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        onClickListener = l;
    }

    public boolean hasMedia() {
        return post.hasImage() || post.hasVideo();
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
