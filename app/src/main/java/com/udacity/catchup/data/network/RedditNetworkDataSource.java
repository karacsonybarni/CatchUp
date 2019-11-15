package com.udacity.catchup.data.network;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Feed;
import com.udacity.catchup.data.entity.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RedditNetworkDataSource {

    private static final String LOG_TAG = RedditNetworkDataSource.class.getSimpleName();

    private static RedditNetworkDataSource sInstance;
    private MutableLiveData<List<Post>> postsLiveData;
    private RedditService redditService;

    private List<String> subreddits;
    private List<Post> postsToStore;
    private int numOfSubredditsFetched;
    private long order;

    private RedditNetworkDataSource(Context context) {
        postsLiveData = new MutableLiveData<>();
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

    public void fetchSubreddits(List<String> subreddits) {
        this.subreddits = subreddits;
        postsToStore = new ArrayList<>();
        numOfSubredditsFetched = 0;
        order = System.currentTimeMillis();
        for (String subreddit : subreddits) {
            fetchPosts(subreddit);
        }
    }

    private void fetchPosts(String subreddit) {
        redditService.getPosts(subreddit).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Post> posts = TypeConverter.toPosts(response.body(), order);
                if (posts != null) {
                    postsToStore.addAll(posts);
                }
                numOfSubredditsFetched++;
                if (numOfSubredditsFetched >= subreddits.size()) {
                    postsLiveData.postValue(postsToStore);
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                String message = t.getLocalizedMessage();
                if (message != null) {
                    Log.e(LOG_TAG, message);
                }
            }
        });
    }

    public MutableLiveData<List<Post>> getPosts() {
        return postsLiveData;
    }
}
