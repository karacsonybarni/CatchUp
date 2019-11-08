package com.udacity.catchup.ui.subscriptionsview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Subreddit;
import com.udacity.catchup.ui.postsview.PostsActivity;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;
import java.util.Objects;

public class SubscriptionsActivity extends AppCompatActivity {

    private SubscriptionsActivityViewModel viewModel;
    private ViewGroup subredditsView;
    private EditText subredditInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        viewModel = getViewModel();
        initViews();
        getNonNullActionBar().setTitle(getString(R.string.subscriptions));
    }

    private SubscriptionsActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        SubscriptionsViewModelFactory factory = new SubscriptionsViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(SubscriptionsActivityViewModel.class);
    }

    @NonNull
    private ActionBar getNonNullActionBar() {
        return Objects.requireNonNull(getSupportActionBar());
    }

    private void initViews() {
        initSubreddits();
        initInputRow();
        initContinueButton();
    }

    private void initSubreddits() {
        subredditsView = findViewById(R.id.subreddits);
        viewModel.getSubreddits().observe(this, this::updateSubreddits);
    }

    private void updateSubreddits(List<Subreddit> subreddits) {
        subredditsView.removeAllViews();
        addSubreddits(subreddits);
    }

    private void addSubreddits(List<Subreddit> subreddits) {
        for (Subreddit subreddit : subreddits) {
            addSubredditItem(subreddit);
        }
    }

    private void addSubredditItem(Subreddit subreddit) {
        View subredditItem =
                getLayoutInflater()
                        .inflate(R.layout.item_subreddit, subredditsView, false);
        TextView subredditName = subredditItem.findViewById(R.id.name);
        subredditName.setText(subreddit.getName());
        Button removeButton = subredditItem.findViewById(R.id.removeButton);
        removeButton.setOnClickListener(v -> viewModel.removeSubreddit(subreddit));
        subredditsView.addView(subredditItem);
    }

    private void initInputRow() {
        subredditInput = findViewById(R.id.subredditInput);
        Button addButton = findViewById(R.id.addSubredditButton);
        addButton.setOnClickListener(this::insertSubreddit);
    }

    private void insertSubreddit(@SuppressWarnings("unused") View addButton) {
        viewModel.insertSubreddit(subredditInput.getText().toString().trim().toLowerCase());
        subredditInput.setText("");
    }

    private void initContinueButton() {
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this::startPostsActivity);
    }

    private void startPostsActivity(@SuppressWarnings("unused") View controllerView) {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        viewModel.getSubreddits().removeObservers(this);
        super.onDestroy();
    }
}