package com.udacity.catchup.ui.detailsview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;

class DetailsActivityViewModel extends ViewModel {

    private LiveData<Post> post;

    DetailsActivityViewModel(Repository repository, String id) {
        post = repository.getPost(id);
    }

    public LiveData<Post> getPost() {
        return post;
    }
}
