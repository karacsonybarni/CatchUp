package com.udacity.catchup.ui.pagerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.subscriptionsview.SubscriptionsActivity;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;

public class PagerActivity extends AppCompatActivity {

    private static boolean isStarting = true;

    private PagerActivityViewModel viewModel;
    private PagerAdapter adapter;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        viewModel = getViewModel();
        viewModel.getPosts().observe(this, this::updatePosts);
        viewModel.getSubreddits().observe(this, this::updateSubreddits);
        initViewPager();
    }

    private void initViewPager() {
        adapter = new PagerAdapter(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(newOnPageChangeCallback());
    }

    private ViewPager2.OnPageChangeCallback newOnPageChangeCallback() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setSeen(position);
                if (position >= adapter.getPosts().size() - 2) {
                    viewModel.fetchPosts();
                }
            }
        };
    }

    private void setSeen(int postPosition) {
        viewModel.setSeen(adapter.getPosts().get(postPosition));
    }

    private PagerActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        PagerViewModelFactory factory = new PagerViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(PagerActivityViewModel.class);
    }

    private void updatePosts(List<Post> posts) {
        adapter.updatePosts(posts);
        if (posts != null && !posts.isEmpty()) {
            updateCurrentPage(posts);
        } else if (isStarting) {
            startSubscriptionsActivity();
        } else {
            showNoSubredditsSnackbar();
        }
        isStarting = false;
    }

    private void updateCurrentPage(List<Post> posts) {
        int firstUnseenPostPosition = getFirstUnseenPostPosition(posts);
        viewPager.setCurrentItem(firstUnseenPostPosition);
        if (firstUnseenPostPosition == 0) {
            setSeen(0);
        }
    }

    private int getFirstUnseenPostPosition(List<Post> posts) {
        int position = 0;
        while (posts.get(position).isSeen() && position < posts.size() - 1) {
            position++;
        }
        return position;
    }

    private void showNoSubredditsSnackbar() {
        Snackbar
                .make(
                        findViewById(R.id.viewPager),
                        getString(R.string.nothing_to_show),
                        Snackbar.LENGTH_LONG)
                .setAction(
                        getString(R.string.go_to_subreddits),
                        v -> startSubscriptionsActivity())
                .show();
    }

    private void updateSubreddits(List<Subreddit> subreddits) {
        if (subreddits != null && !subreddits.isEmpty()) {
            adapter.updateSubreddits(subreddits);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_subscriptions) {
            startSubscriptionsActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSubscriptionsActivity() {
        Intent intent = new Intent(this, SubscriptionsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        viewModel.getPosts().removeObservers(this);
        viewModel.getSubreddits().removeObservers(this);
        super.onDestroy();
    }
}
