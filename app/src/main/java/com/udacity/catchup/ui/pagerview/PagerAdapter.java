package com.udacity.catchup.ui.pagerview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.udacity.catchup.data.entity.post.Post;

import java.util.List;

class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Post> posts;
    private PostFragment currentPage;

    PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        PostFragment fragment = new PostFragment();
        fragment.setPost(posts.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return posts != null ? posts.size() : 0;
    }

    void updatePosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        currentPage = (PostFragment) super.instantiateItem(container, position);
        return currentPage;
    }

    @Nullable
    PostFragment getCurrentPage() {
        return currentPage;
    }

    List<Post> getPosts() {
        return posts;
    }
}
