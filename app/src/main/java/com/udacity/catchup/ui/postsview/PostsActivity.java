package com.udacity.catchup.ui.postsview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;

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
    private TextView subredditName;
    private TextView postDetails;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        initViews();
        viewModel = getViewModel();
        viewModel.getPosts().observe(this, this::updatePosts);
    }

    private void initViews() {
        subredditName = findViewById(R.id.subredditName);
        postDetails = findViewById(R.id.postDetails);
        title = findViewById(R.id.title);
    }

    private PostsActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        PostsViewModelFactory factory = new PostsViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(PostsActivityViewModel.class);
    }

    private void updatePosts(List<Post> posts) {
        if (posts.size() > 0) {
            populateViews(posts.get(0));
        } else {
            showInternetErrorSnackbar();
        }
    }

    @SuppressLint("SetTextI18n")
    private void populateViews(Post post) {
        subredditName.setText("EarthPorn");
        String postDetailsString =
                getString(R.string.post_details, post.getAuthorName(), getTime(post));
        postDetails.setText(postDetailsString);
        title.setText(post.getTitle());
    }

    private String getTime(Post post) {
        long time = post.getDate();
        long now = System.currentTimeMillis();
        CharSequence timeAgo =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return String.valueOf(timeAgo);
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
