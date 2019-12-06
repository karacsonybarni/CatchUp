package com.udacity.catchup.ui.detailsview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

class DetailsActivityViewModel extends ViewModel {

    private Repository repository;
    private LiveData<Post> post;
    private LiveData<Subreddit> subreddit;

    DetailsActivityViewModel(Repository repository, String id) {
        this.repository = repository;
        post = repository.getPost(id);
    }

    LiveData<Post> getPost() {
        return post;
    }

    LiveData<Subreddit> getSubreddit(String name) {
        if (subreddit == null) {
            subreddit = repository.getSubreddit(name);
        }
        return subreddit;
    }

    void setSeen(Post post) {
        repository.setSeen(post);
    }
}
