package com.udacity.catchup.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.udacity.catchup.data.database.Database;
import com.udacity.catchup.data.database.PostDao;
import com.udacity.catchup.data.database.SubredditDao;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.List;
import java.util.concurrent.Executor;

public class Repository {

    private static Repository sInstance;
    private PostDao postDao;
    private SubredditDao subredditDao;
    private RedditNetworkDataSource redditNetworkDataSource;
    private Executor diskIO;
    private MediatorLiveData<Subreddit> subredditsFromNetwork;
    private LiveData<List<Subreddit>> subredditsFromDb;
    private LiveData<List<Post>> posts;

    private List<Subreddit> lastLoadedSubreddits;

    private Repository(
            Database database,
            RedditNetworkDataSource redditNetworkDataSource,
            Executor diskIO) {
        postDao = database.postDao();
        subredditDao = database.subredditDao();
        this.redditNetworkDataSource = redditNetworkDataSource;
        this.diskIO = diskIO;
        subredditsFromDb = subredditDao.getAll();
        subredditsFromNetwork = new MediatorLiveData<>();
        posts = postDao.getAll();

        initObservers();
    }

    private void initObservers() {
        subredditsFromDb.observeForever(this::fetchPosts);
        subredditsFromNetwork
                .addSource(
                        redditNetworkDataSource.getSubreddits(),
                        this::storeSubredditsIfSizeDiffers);
        redditNetworkDataSource.getPosts().observeForever(this::storePosts);
    }

    private void fetchPosts(List<Subreddit> subreddits) {
        lastLoadedSubreddits = subreddits;
        fetchPosts();
    }

    public void fetchPosts() {
        redditNetworkDataSource.fetchSubredditsAndPosts(lastLoadedSubreddits);
    }

    private void storeSubredditsIfSizeDiffers(List<Subreddit> subredditsFromNetwork) {
        if (lastLoadedSubreddits != null && subredditsFromNetwork != null
                && lastLoadedSubreddits.size() != subredditsFromNetwork.size()
                && !subredditsFromNetwork.isEmpty()) {
            diskIO.execute(() -> subredditDao.insert(subredditsFromNetwork));
        }
    }

    private void storePosts(List<Post> posts) {
        if (posts != null && !posts.isEmpty()) {
            diskIO.execute(() -> postDao.insert(posts));
        }
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

    public RedditNetworkDataSource getNetworkDataSource() {
        return redditNetworkDataSource;
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
        return subredditsFromDb;
    }

    public void insertSubreddit(Subreddit subreddit) {
        diskIO.execute(() -> subredditDao.insert(subreddit));
    }

    public void removeSubreddit(Subreddit subreddit) {
        diskIO.execute(() -> subredditDao.remove(subreddit));
    }
}
