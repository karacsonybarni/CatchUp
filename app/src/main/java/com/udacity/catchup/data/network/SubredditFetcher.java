package com.udacity.catchup.data.network;

import android.util.Log;

import com.udacity.catchup.data.entity.post.Feed;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.entity.subreddit.SubredditWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class SubredditFetcher {

    private static final String LOG_TAG = SubredditFetcher.class.getSimpleName();

    private RedditNetworkDataSource dataSource;
    private RedditService redditService;
    private List<Subreddit> subredditsToFetch;
    private List<Subreddit> subredditsFetched;
    private List<Post> postsFetched;
    private int numOfSubredditResults;
    private int numOfPostFetchResults;

    SubredditFetcher(RedditNetworkDataSource dataSource) {
        this.dataSource = dataSource;
        redditService = dataSource.getRedditService();
        subredditsFetched = new ArrayList<>();
        postsFetched = new ArrayList<>();
    }

    void fetchSubreddits(List<Subreddit> subreddits) {
        this.subredditsToFetch = subreddits;
        for (Subreddit subreddit : subreddits) {
            fetchSubreddit(subreddit);
            fetchPosts(subreddit);
        }
    }

    private void fetchSubreddit(Subreddit subreddit) {
        redditService.getSubreddit(subreddit.getName()).enqueue(new Callback<SubredditWrapper>() {
            @Override
            public void onResponse(Call<SubredditWrapper> call,
                                   Response<SubredditWrapper> response) {
                SubredditWrapper responseBody = response.body();
                Subreddit subreddit = responseBody != null ? responseBody.getData() : null;
                if (subreddit != null) {
                    subredditsFetched.add(subreddit);
                }
            }

            @Override
            public void onFailure(Call<SubredditWrapper> call, Throwable t) {
                onSubredditResult();
                log(t);
            }
        });
    }

    private void onSubredditResult() {
        numOfSubredditResults++;
        if (numOfSubredditResults == subredditsToFetch.size()) {
            dataSource.updateSubreddits(subredditsFetched);
        }
    }

    private void log(Throwable t) {
        String message = t.getLocalizedMessage();
        if (message != null) {
            Log.e(LOG_TAG, message);
        }
    }

    private void fetchPosts(Subreddit subreddit) {
        redditService.getPosts(subreddit.getName()).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call,
                                   Response<Feed> response) {
                List<Post> posts = TypeConverter.toPosts(response.body());
                if (posts != null) {
                    postsFetched.addAll(posts);
                }
                onPostsResult();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                onPostsResult();
                log(t);
            }
        });
    }

    private void onPostsResult() {
        numOfPostFetchResults++;
        if (numOfPostFetchResults == subredditsToFetch.size()) {
            dataSource.updatePosts(postsFetched);
        }
    }
}
