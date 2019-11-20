package com.udacity.catchup.data.entity.post;

import java.util.List;

public class FeedData {

    private List<FeedDataElem> children;

    public List<FeedDataElem> getChildren() {
        return children;
    }

    public void setChildren(List<FeedDataElem> children) {
        this.children = children;
    }
}
