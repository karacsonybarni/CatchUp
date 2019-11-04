package com.udacity.catchup.data;

import androidx.lifecycle.LiveData;

import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.database.RedditDao;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.data.entity.Subreddit;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class Repository {

    private static Repository sInstance;
    private RedditDao redditDao;
    private RedditNetworkDataSource redditNetworkDataSource;
    private Executor diskIO;
    private LiveData<List<Subreddit>> subreddits;
    private LiveData<List<Post>> posts;

    private List<String> lastLoadedSubredditNames;

    private Repository(
            Database database,
            RedditNetworkDataSource redditNetworkDataSource,
            Executor diskIO) {
        redditDao = database.postDao();
        this.redditNetworkDataSource = redditNetworkDataSource;
        this.diskIO = diskIO;
        subreddits = redditDao.getSubreddits();
        posts = redditDao.getPosts();

        initObservers();
    }

    private void initObservers() {
        redditNetworkDataSource.getPosts().observeForever(this::storePosts);
        subreddits.observeForever(this::fetchPosts);
    }

    private void storePosts(List<Post> posts) {
        if (posts != null && !posts.isEmpty()) {
            diskIO.execute(() -> redditDao.updatePosts(posts));
        }
    }

    private void fetchPosts(List<Subreddit> subreddits) {
        lastLoadedSubredditNames = new ArrayList<>();
        for (Subreddit subreddit : subreddits) {
            lastLoadedSubredditNames.add(subreddit.getName());
        }
        fetchPosts();
    }

    public void fetchPosts() {
        redditNetworkDataSource.fetchSubreddits(lastLoadedSubredditNames);
    }

    public static Repository getInstance(
            Database database,
            RedditNetworkDataSource redditNetworkDataSource,
            Executor diskIO) {
        if (sInstance == null) {
            sInstance = new Repository(database, redditNetworkDataSource, diskIO);
        }
        return sInstance;
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    public void insertSubreddit(String subredditName) {
        diskIO.execute(() -> {
            Subreddit subreddit = new Subreddit();
            subreddit.setName(subredditName);
            redditDao.insertSubreddit(subreddit);
        });
    }

    public void removeSubreddit(Subreddit subreddit) {
        diskIO.execute(() -> redditDao.removeSubreddit(subreddit));
    }
}
