package com.udacity.catchup.ui.pagerview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

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
        if (oldPosts == null || !areEqual(oldPosts, postsFromDb)) {
            posts.postValue(postsFromDb);
        }
    }

    private boolean areEqual(List<Post> listA, List<Post> listB) {
        if (listA == null || listB == null) {
            return listA == null && listB == null;
        }
        if (listA.size() != listB.size()) {
            return false;
        }
        if (listA.isEmpty()) {
            return true;
        }
        return areUnseenPostsEqual(listA, listB);
    }

    private boolean areUnseenPostsEqual(List<Post> listA, List<Post> listB) {
        int size = listB.size();
        int lastSeenPostPosition = getFirstUnseenPostPosition(listB) - 1;
        lastSeenPostPosition = lastSeenPostPosition < 0 ? lastSeenPostPosition : 0;
        Set<Post> setA = new HashSet<>(listA.subList(lastSeenPostPosition, size));
        Set<Post> setB = new HashSet<>(listB.subList(lastSeenPostPosition, size));
        for (Post postA : setA) {
            if (!contains(setB, postA)) {
                return false;
            }
        }
        return true;
    }

    int getFirstUnseenPostPosition(List<Post> posts) {
        int position = posts.size() - 1;
        ListIterator<Post> iterator = posts.listIterator(position);

        while (iterator.hasPrevious() && !iterator.previous().isSeen()) {
            position--;
        }
        return position;
    }

    private boolean contains(Set<Post> posts, Post post) {
        for (Post setElem : posts) {
            if (setElem.equals(post)) {
                return true;
            }
        }
        return false;
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
        repository.setSeen(post);
    }

    LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    LiveData<Subreddit> getSubreddit(String name) {
        return repository.getSubreddit(name);
    }
}
