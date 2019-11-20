package com.udacity.catchup.data.entity.post;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("WeakerAccess")
public class RedditVideo {

    @SerializedName("fallback_url")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
