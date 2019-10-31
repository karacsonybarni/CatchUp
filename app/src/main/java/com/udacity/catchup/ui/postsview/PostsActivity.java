package com.udacity.catchup.ui.postsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.data.network.RedditNetworkDataSource;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private PostsActivityViewModel viewModel;
    private PostFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        initPostFragment();
        viewModel = getViewModel();
        viewModel.getPosts().observe(this, this::updatePosts);
    }

    private void initPostFragment() {
        postFragment =
                (PostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.postFragmentContainer);
        if (postFragment == null) {
            postFragment = new PostFragment();
            addPostFragment();
        }
    }

    private void addPostFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.postFragmentContainer, postFragment)
                .commit();
    }

    private PostsActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        PostsViewModelFactory factory = new PostsViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(PostsActivityViewModel.class);
    }

    private void updatePosts(List<Post> posts) {
        if (posts.size() > 0) {
            postFragment.updatePost(posts.get(0));
        } else {
            showInternetErrorSnackbar();
        }
    }

    private void showInternetErrorSnackbar() {
        Snackbar
                .make(
                        findViewById(R.id.root),
                        getString(R.string.no_internet_connection),
                        Snackbar.LENGTH_LONG)
                .setAction(
                        getString(R.string.retry),
                        v -> RedditNetworkDataSource.getInstance(this).fetchPosts())
                .show();
    }

    @Override
    protected void onDestroy() {
        viewModel.getPosts().removeObservers(this);
        super.onDestroy();
    }
}
