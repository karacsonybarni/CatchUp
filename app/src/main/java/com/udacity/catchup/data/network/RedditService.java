package com.udacity.catchup.data.network;

import com.udacity.catchup.data.entity.comment.PageSection;
import com.udacity.catchup.data.entity.post.Feed;
import com.udacity.catchup.data.entity.subreddit.SubredditWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RedditService {

    @GET("{subreddit}/about.json")
    Call<SubredditWrapper> getSubreddit(@Path("subreddit") String subreddit);

    @GET("{subreddit}.json")
    Call<Feed> getPosts(@Path("subreddit") String subreddit);

    @GET("{subreddit}/comments/{id}.json")
    Call<List<PageSection>> getComments(@Path("subreddit") String subreddit, @Path("id") String id);
}
