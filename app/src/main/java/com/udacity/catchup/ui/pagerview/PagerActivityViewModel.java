package com.udacity.catchup.ui.pagerview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;

import java.util.List;

class PagerActivityViewModel extends ViewModel {

    private Repository repository;
    private MediatorLiveData<List<Post>> posts;

    PagerActivityViewModel(Repository repository) {
        this.repository = repository;
        posts = new MediatorLiveData<>();
        posts.addSource(repository.getPosts(), updatedPosts -> {
            List<Post> oldPosts = posts.getValue();
            if (oldPosts == null || oldPosts.size() != updatedPosts.size()) {
                posts.postValue(updatedPosts);
            }
        });
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

    void setSeen(Post post) {
        if (!post.isSeen()) {
            post.setSeen(true);
            post.setOrder(post.getOrder() / 10);
            repository.updatePost(post);
        }
    }
}
