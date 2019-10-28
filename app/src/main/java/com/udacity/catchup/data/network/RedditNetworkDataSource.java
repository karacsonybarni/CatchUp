package com.udacity.catchup.data.network;

import android.content.Context;
import android.util.Log;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Feed;
import com.udacity.catchup.data.entity.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class RedditNetworkDataSource {

    private static final String LOG_TAG = RedditNetworkDataSource.class.getSimpleName();
    private static RedditNetworkDataSource instance;

    private RedditService redditService;

    private RedditNetworkDataSource(Context context) {
        initRetrofit(context);
    }

    private void initRetrofit(Context context) {
        String baseUrl = context.getString(R.string.base_url);

        @SuppressWarnings("deprecation")
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        redditService = retrofit.create(RedditService.class);
    }

    public static RedditNetworkDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new RedditNetworkDataSource(context);
        }
        return instance;
    }

    public void fetchPosts() {
        redditService.getPosts().enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                Feed feed = response.body();
                List<Post> posts = feed != null ? feed.getPosts() : null;
                if (posts != null) {
                    Log.d(LOG_TAG, posts.toString());
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
}
