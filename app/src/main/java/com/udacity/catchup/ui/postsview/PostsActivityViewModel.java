package com.udacity.catchup.ui.postsview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Post;

import java.util.List;

class PostsActivityViewModel extends ViewModel {

    private LiveData<List<Post>> posts;

    PostsActivityViewModel(Repository repository) {
        posts = repository.getPosts();
    }

    LiveData<List<Post>> getPosts() {
        return posts;
    }
}
