package com.udacity.catchup.data.network;

import com.udacity.catchup.data.entity.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditService {

    @GET("{subreddit}.json")
    Call<Feed> getPosts(@Path("subreddit") String subreddit);
}
