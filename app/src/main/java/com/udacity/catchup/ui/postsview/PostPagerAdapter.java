package com.udacity.catchup.ui.postsview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.udacity.catchup.data.entity.Post;

import java.util.List;

class PostPagerAdapter extends FragmentStatePagerAdapter {

    private List<Post> posts;

    PostPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        PostFragment fragment = new PostFragment();
        fragment.setPost(posts.get(position));
        fragment.setPostPosition(position);
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
}
