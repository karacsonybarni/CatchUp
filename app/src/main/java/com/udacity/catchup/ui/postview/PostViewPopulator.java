package com.udacity.catchup.ui.postview;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.widget.RemotePostViewPopulator;

public class PostViewPopulator {

    private Post post;
    private PostViewPopulatorDelegate delegate;

    PostViewPopulator(ViewGroup root) {
        delegate = new AppPostViewPopulator(root);
    }

    public PostViewPopulator(Context context,
                             RemoteViews remoteViews,
                             PendingIntent onClickIntent) {
        delegate = new RemotePostViewPopulator(context, remoteViews);
        delegate.setOnClickIntent(onClickIntent);
    }

    public void updatePost(Post post) {
        this.post = post;
        populateViews();
    }

    private void populateViews() {
        loadOrHideSubredditIcon();
        delegate.fillSubredditName(post.getSubredditName());
        delegate.fillPostDetails(getPostDetails());
        delegate.fillTitle(processHtml(post.getTitle()));
        fillBodyText();
        addMedia();
    }

    void loadOrHideSubredditIcon() {
        Subreddit subreddit = post.getSubreddit();
        if (subreddit == null) {
            return;
        }
        String iconUrl = subreddit.getIconUrl();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            delegate.loadSubredditIcon(subreddit.getIconUrl());
        } else {
            delegate.hideSubredditIconAndUpdateLayout();
        }
    }

    private String getPostDetails() {
        return delegate
                .getContext()
                .getString(R.string.post_details, post.getAuthorName(), getPostTime());
    }

    private String getPostTime() {
        long time = post.getDate();
        long now = System.currentTimeMillis();
        CharSequence timeAgo =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return String.valueOf(timeAgo);
    }

    private String processHtml(String htmlText) {
        String cleanedText = htmlText.replaceAll("&amp;#x200B;", "");
        String textWithLineBreaks = cleanedText.replace("\n", "<br/>");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(textWithLineBreaks, Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            //noinspection deprecation
            return Html.fromHtml(textWithLineBreaks).toString();
        }
    }

    private void fillBodyText() {
        String text = post.getText();
        if (hasText()) {
            delegate.fillBodyText(processHtml(text));
        }
    }

    private boolean hasText() {
        String text = post.getText();
        return text != null && !text.isEmpty();
    }

    private void addMedia() {
        if (post.hasImage()) {
            delegate.showImage(post.getMediaUrl());
        } else if (hasLink() && !hasText() && !post.hasVideo()) {
            delegate.addLink(post.getMediaUrl(), this::openLink);
        }
    }

    private boolean hasLink() {
        String mediaUrl = post.getMediaUrl();
        return mediaUrl != null && !mediaUrl.isEmpty();
    }

    private void openLink(@SuppressWarnings("unused") View linkView) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getMediaUrl()));
        delegate.getContext().startActivity(browserIntent);
    }

    void setConstraintsUpdater(Runnable constraintsUpdater) {
        delegate.setConstraintsUpdater(constraintsUpdater);
    }

    void setOnClickListener(View.OnClickListener listener) {
        delegate.setOnClickListener(listener);
    }
}
