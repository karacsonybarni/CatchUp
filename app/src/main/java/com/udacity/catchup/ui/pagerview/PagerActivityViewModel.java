package com.udacity.catchup.ui.pagerview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

import java.util.List;

class PagerActivityViewModel extends ViewModel {

    private Repository repository;
    private MediatorLiveData<List<Post>> posts;
    private LiveData<List<Subreddit>> subreddits;

    PagerActivityViewModel(Repository repository) {
        this.repository = repository;
        posts = new MediatorLiveData<>();
        posts.addSource(repository.getPosts(), this::updatePostsIfSizeDiffers);
        subreddits = repository.getSubreddits();
    }

    private void updatePostsIfSizeDiffers(List<Post> postsFromDb) {
        List<Post> oldPosts = this.posts.getValue();
        if (oldPosts == null || oldPosts.size() != postsFromDb.size()) {
            posts.postValue(postsFromDb);
        }
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

    LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    LiveData<Subreddit> getSubreddit(String name) {
        return repository.getSubreddit(name);
    }
}
