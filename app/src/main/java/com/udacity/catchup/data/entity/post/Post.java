package com.udacity.catchup.data.entity.post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys =
        @ForeignKey(
                entity = Subreddit.class,
                parentColumns = "name",
                childColumns = "subredditName",
                onDelete = CASCADE))
public class Post {

    @SuppressWarnings("NullableProblems")
    @NonNull
    @PrimaryKey
    private String id;

    @Ignore
    private Subreddit subredditObj;

    @SerializedName("subreddit")
    @ColumnInfo(index = true)
    private String subredditName;

    @SerializedName("author")
    private String authorName;

    @SerializedName("created")
    private long date;

    private String title;

    @SerializedName("post_hint")
    private String type;

    @SerializedName("selftext")
    private String text;

    @SerializedName("url")
    private String mediaUrl;

    @Ignore
    private Media media;

    private String videoUrl;

    private boolean isSeen;

    private long order;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Subreddit getSubreddit() {
        return subredditObj;
    }

    public void setSubreddit(Subreddit subreddit) {
        subredditObj = subreddit;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Nullable
    public String getVideoUrl() {
        if (videoUrl == null) {
            RedditVideo redditVideo = media != null ? media.getRedditVideo() : null;
            videoUrl = redditVideo != null ? redditVideo.getUrl() : null;
        }
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public boolean hasImage() {
        return hasType("image");
    }

    private boolean hasType(String type) {
        return this.type != null && this.type.contains(type);
    }

    public boolean hasVideo() {
        return hasType("video");
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Post)) {
            return false;
        }
        Post post = (Post) obj;
        //noinspection ConstantConditions
        return id != null && id.equals(post.getId());
    }
}
