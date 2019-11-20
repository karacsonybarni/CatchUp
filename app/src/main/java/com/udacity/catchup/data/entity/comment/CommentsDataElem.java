package com.udacity.catchup.data.entity.comment;

import com.google.gson.annotations.SerializedName;

public class CommentsDataElem {

    @SerializedName("data")
    private Comment comment;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
