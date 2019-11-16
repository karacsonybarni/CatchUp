package com.udacity.catchup.ui.postsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private PostsActivityViewModel viewModel;
    private PostPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        viewModel = getViewModel();
        viewModel.getPosts().observe(this, this::updatePosts);
        initViewPager();
    }

    private void initViewPager() {
        adapter = new PostPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(newOnPageChangeListener());
    }

    private ViewPager.OnPageChangeListener newOnPageChangeListener() {
        return new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                PostFragment currentFragment = adapter.getCurrentPage();
                if (currentFragment != null) {
                    setSeen(position);
                    if (currentFragment.hasVideo()) {
                        currentFragment.playVideo();
                    }
                }
            }
        };
    }

    private void setSeen(int postPosition) {
        viewModel.setSeen(adapter.getPost(postPosition));
    }

    private PostsActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        PostsViewModelFactory factory = new PostsViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(PostsActivityViewModel.class);
    }

    private void updatePosts(List<Post> posts) {
        if (posts != null && posts.size() > 0) {
            adapter.updatePosts(posts);
            updateCurrentPage(posts);
        } else {
            showInternetErrorSnackbar();
        }
    }

    private void updateCurrentPage(List<Post> posts) {
        int firstUnseenPostPosition = getFirstUnseenPostPosition(posts);
        viewPager.setCurrentItem(firstUnseenPostPosition);
        setSeen(firstUnseenPostPosition);
    }

    private int getFirstUnseenPostPosition(List<Post> posts) {
        int position = 0;
        while (posts.get(position).isSeen() && position < posts.size() - 1) {
            position++;
        }
        return position;
    }

    private void showInternetErrorSnackbar() {
        Snackbar
                .make(
                        findViewById(R.id.viewPager),
                        getString(R.string.no_internet_connection),
                        Snackbar.LENGTH_LONG)
                .setAction(
                        getString(R.string.retry),
                        v -> viewModel.fetchPosts())
                .show();
    }

    @Override
    protected void onDestroy() {
        viewModel.getPosts().removeObservers(this);
        super.onDestroy();
    }
}
