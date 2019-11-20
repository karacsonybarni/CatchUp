package com.udacity.catchup.data.entity.comment;

import java.util.List;

public class CommentsData {

    private List<CommentsDataElem> children;

    public List<CommentsDataElem> getChildren() {
        return children;
    }

    public void setChildren(List<CommentsDataElem> children) {
        this.children = children;
    }
}
