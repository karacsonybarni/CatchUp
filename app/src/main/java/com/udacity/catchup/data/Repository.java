package com.udacity.catchup.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.database.PostDao;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.List;
import java.util.concurrent.Executor;

public class Repository {

    private static Repository sInstance;
    private PostDao postDao;
    private Executor diskIO;

    private Repository(
            Database database,
            RedditNetworkDataSource redditNetworkDataSource,
            Executor diskIO) {
        postDao = database.postDao();
        this.diskIO = diskIO;

        redditNetworkDataSource.getPosts().observeForever(newNetworkDataObserver());
        redditNetworkDataSource.fetchPosts();
    }

    private Observer<? super List<Post>> newNetworkDataObserver() {
        return posts ->
                diskIO.execute(() -> {
                    if (posts != null && !posts.isEmpty()) {
                        postDao.updatePosts(posts);
                    }
                });
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
        return postDao.getPosts();
    }
}
