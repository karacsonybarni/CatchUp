package com.udacity.catchup.data.network;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.comment.PageSection;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.entity.subreddit.SubredditWrapper;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditNetworkDataSource {

    private static RedditNetworkDataSource sInstance;
    private RedditService redditService;
    private MutableLiveData<List<Subreddit>> subreddits;
    private MutableLiveData<List<Post>> posts;

    private RedditNetworkDataSource(Context context) {
        subreddits = new MutableLiveData<>();
        posts = new MutableLiveData<>();
        initRetrofit(context);
    }

    private void initRetrofit(Context context) {
        String baseUrl = context.getString(R.string.base_url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        redditService = retrofit.create(RedditService.class);
    }

    public static RedditNetworkDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RedditNetworkDataSource(context);
        }
        return sInstance;
    }

    public LiveData<List<Subreddit>> getSubreddits() {
        return subreddits;
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void fetchSubredditsAndPosts(List<Subreddit> subreddits) {
        new SubredditFetcher(this).fetchSubreddits(subreddits);
    }

    RedditService getRedditService() {
        return redditService;
    }

    void updateSubreddits(List<Subreddit> subreddits) {
        this.subreddits.postValue(subreddits);
    }

    void updatePosts(List<Post> posts) {
        this.posts.postValue(posts);
    }

    public void fetchSubreddit(String subredditName, Callback<SubredditWrapper> callback) {
        redditService.getSubreddit(subredditName).enqueue(callback);
    }

    public void fetchComments(String subreddit, String id, Callback<List<PageSection>> callback) {
        redditService.getComments(subreddit, id).enqueue(callback);
    }
}
