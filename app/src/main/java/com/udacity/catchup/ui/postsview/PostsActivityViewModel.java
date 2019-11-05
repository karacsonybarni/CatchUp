package com.udacity.catchup.ui.postsview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Post;

import java.util.List;

class PostsActivityViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Post>> posts;

    PostsActivityViewModel(Repository repository) {
        this.repository = repository;
        posts = repository.getPosts();
    }

    LiveData<List<Post>> getPosts() {
        return posts;
    }

    LiveData<Post> getPost(String id) {
        return repository.getPost(id);
    }

    void fetchPosts() {
        repository.fetchPosts();
    }
}
