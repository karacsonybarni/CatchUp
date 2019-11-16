package com.udacity.catchup.data;

import androidx.lifecycle.LiveData;

import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.database.PostDao;
import com.udacity.catchup.data.database.SubredditDao;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.data.entity.Subreddit;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class Repository {

    private static Repository sInstance;
    private PostDao postDao;
    private SubredditDao subredditDao;
    private RedditNetworkDataSource redditNetworkDataSource;
    private Executor diskIO;
    private LiveData<List<Subreddit>> subreddits;
    private LiveData<List<Post>> posts;

    private List<String> lastLoadedSubredditNames;

    private Repository(
            Database database,
            RedditNetworkDataSource redditNetworkDataSource,
            Executor diskIO) {
        postDao = database.postDao();
        subredditDao = database.subredditDao();
        this.redditNetworkDataSource = redditNetworkDataSource;
        this.diskIO = diskIO;
        subreddits = subredditDao.getAll();
        posts = postDao.getAll();

        initObservers();
    }

    private void initObservers() {
        redditNetworkDataSource.getPosts().observeForever(this::storePosts);
        subreddits.observeForever(this::fetchPosts);
    }

    private void storePosts(List<Post> posts) {
        if (posts != null && !posts.isEmpty()) {
            diskIO.execute(() -> postDao.insert(posts));
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

    public LiveData<Post> getPost(String id) {
        return postDao.get(id);
    }

    public void updatePost(Post post) {
        diskIO.execute(() -> postDao.insert(post));
    }

    public LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    public void insertSubreddit(String subredditName) {
        diskIO.execute(() -> {
            Subreddit subreddit = new Subreddit();
            subreddit.setName(subredditName);
            subredditDao.insert(subreddit);
        });
    }

    public void removeSubreddit(Subreddit subreddit) {
        diskIO.execute(() -> subredditDao.remove(subreddit));
    }
}
