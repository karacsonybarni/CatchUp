package com.udacity.catchup.data.network;

import com.udacity.catchup.data.entity.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RedditService {

    @GET("earthporn/.rss")
    Call<Feed> getPosts();
}
