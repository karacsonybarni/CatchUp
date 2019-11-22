package com.udacity.catchup.ui.subscriptionsview;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.List;

class SubscriptionsActivityViewModel extends ViewModel {

    private Repository repository;
    private RedditNetworkDataSource networkDataSource;
    private LiveData<List<Subreddit>> subreddits;

    SubscriptionsActivityViewModel(Repository repository) {
        this.repository = repository;
        networkDataSource = repository.getNetworkDataSource();
        subreddits = repository.getSubreddits();
    }

    LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    void insertSubredditIfValid(EditText subredditInput) {
        String subredditName = subredditInput.getText().toString().trim();
        networkDataSource
                .fetchSubreddit(
                        subredditName,
                        new SubredditValidatorCallback(repository, subredditInput));
    }

    void removeSubreddit(Subreddit subreddit) {
        repository.removeSubreddit(subreddit);
    }
}
