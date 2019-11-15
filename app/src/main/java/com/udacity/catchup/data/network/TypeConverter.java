package com.udacity.catchup.data.network;

import androidx.annotation.Nullable;

import com.udacity.catchup.data.entity.Feed;
import com.udacity.catchup.data.entity.FeedDataElem;
import com.udacity.catchup.data.entity.Post;

import java.util.ArrayList;
import java.util.List;

class TypeConverter {

    private static final int SECOND = 1000;

    @Nullable
    static List<Post> toPosts(Feed feed, long order) {
        List<FeedDataElem> feedElems = feed != null ? feed.getData().getChildren() : null;
        if (feedElems == null || feedElems.size() == 0) {
            return null;
        }

        List<Post> posts = new ArrayList<>();
        for (FeedDataElem feedElem : feedElems) {
            posts.add(toPost(feedElem, order));
        }
        return posts;
    }

    private static Post toPost(FeedDataElem feedElem, long order) {
        Post post = feedElem.getPost();
        post.setOrder(order);
        post.setDate(post.getDate() * SECOND);
        return post;
    }
}
