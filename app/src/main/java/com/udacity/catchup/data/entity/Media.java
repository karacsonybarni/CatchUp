package com.udacity.catchup.data.entity;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class Media {

    @SerializedName("reddit_video")
    private RedditVideo redditVideo;

    public RedditVideo getRedditVideo() {
        return redditVideo;
    }

    public void setRedditVideo(RedditVideo redditVideo) {
        this.redditVideo = redditVideo;
    }
}
