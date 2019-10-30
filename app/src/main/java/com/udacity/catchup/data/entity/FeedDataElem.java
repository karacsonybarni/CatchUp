package com.udacity.catchup.data.entity;

import com.google.gson.annotations.SerializedName;

public class FeedDataElem {

    @SerializedName("data")
    private Post post;

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
