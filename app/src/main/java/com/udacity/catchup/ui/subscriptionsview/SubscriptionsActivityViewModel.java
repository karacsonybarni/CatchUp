package com.udacity.catchup.ui.subscriptionsview;

import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;

class SubscriptionsActivityViewModel extends ViewModel {

    private Repository repository;

    SubscriptionsActivityViewModel(Repository repository) {
        this.repository = repository;
    }

    void insertSubreddit(String subredditName) {
        repository.insertSubreddit(subredditName);
    }
}
