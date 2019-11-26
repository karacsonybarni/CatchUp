package com.udacity.catchup.ui.detailsview;

import com.udacity.catchup.data.entity.post.Post;

interface DetailsActivityDelegate {
    void initWindow();
    void initViews();
    void updatePost(Post post);
    void close();
}
