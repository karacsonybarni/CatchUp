package com.udacity.catchup.ui.subscriptionsview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Subreddit;

import java.util.List;

class SubscriptionsActivityViewModel extends ViewModel {

    private Repository repository;
    private LiveData<List<Subreddit>> subreddits;

    SubscriptionsActivityViewModel(Repository repository) {
        this.repository = repository;
        subreddits = repository.getSubreddits();
    }

    LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    void insertSubreddit(String subredditName) {
        repository.insertSubreddit(subredditName);
    }

    void removeSubreddit(Subreddit subreddit) {
        repository.removeSubreddit(subreddit);
    }
}
