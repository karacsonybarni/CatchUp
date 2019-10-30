package com.udacity.catchup.data.network;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.FeedDataElem;
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

    public void fetchPosts() {
        redditService.getPosts().enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                List<Post> posts = extractPosts(response.body());
                if (posts != null) {
                    postsLiveData.postValue(posts);
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

    private List<Post> extractPosts(Feed feed) {
        List<FeedDataElem> feedElems = feed != null ? feed.getData().getChildren() : null;
        if (feedElems == null || feedElems.size() == 0) {
            return null;
        }

        List<Post> posts = new ArrayList<>();
        for (FeedDataElem feedElem : feedElems) {
            posts.add(feedElem.getPost());
        }
        return posts;
    }

    public MutableLiveData<List<Post>> getPosts() {
        return postsLiveData;
    }
}
