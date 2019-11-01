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
import com.udacity.catchup.data.network.RedditNetworkDataSource;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private PostsActivityViewModel viewModel;
    private PostPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        initViewPager();
        viewModel = getViewModel();
        viewModel.getPosts().observe(this, this::updatePosts);
    }

    private void initViewPager() {
        adapter = new PostPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
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
        } else {
            showInternetErrorSnackbar();
        }
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
