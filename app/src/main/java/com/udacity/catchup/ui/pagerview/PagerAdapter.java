package com.udacity.catchup.ui.pagerview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class PagerAdapter extends FragmentStateAdapter {

    private static final String STORED_POSTS = "stored_posts";
    private static final String STORED_POSTS_NUM = "stored_posts_num";

    private List<Post> posts;
    private Map<String, Subreddit> subreddits;
    private Set<Long> ids;
    private FirebaseAnalytics analytics;

    PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        ids = new HashSet<>();
        analytics = FirebaseAnalytics.getInstance(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PostFragment fragment = new PostFragment();
        fragment.setPost(posts.get(position));
        return fragment;
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        long hashCode = posts.get(position).getId().hashCode();
        ids.add(hashCode);
        return hashCode;
    }

    @Override
    public boolean containsItem(long itemId) {
        return ids.contains(itemId);
    }

    void updatePosts(List<Post> posts) {
        this.posts = posts;
        updatePosts();
        logPostsSize();
        notifyDataSetChanged();
    }

    private void logPostsSize() {
        Bundle postsSize = new Bundle();
        postsSize.putInt(STORED_POSTS_NUM, posts.size());
        analytics.logEvent(STORED_POSTS, postsSize);
    }

    private void updatePosts() {
        if (posts == null || subreddits == null) {
            return;
        }
        for (Post post : posts) {
            post.setSubreddit(subreddits.get(post.getSubredditName()));
        }
    }

    void updateSubreddits(List<Subreddit> subreddits) {
        reCreateSubreddits(subreddits);
        updatePosts();
        notifyDataSetChanged();
    }

    private void reCreateSubreddits(List<Subreddit> subredditList) {
        subreddits = new HashMap<>();
        for (Subreddit subreddit : subredditList) {
            subreddits.put(subreddit.getName(), subreddit);
        }
    }

    List<Post> getPosts() {
        return posts;
    }
}
