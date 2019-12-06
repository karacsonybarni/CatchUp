package com.udacity.catchup.ui.subscriptionsview;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;
import java.util.Objects;

public class SubscriptionsActivity extends AppCompatActivity {

    private SubscriptionsActivityViewModel viewModel;
    private RecyclerView subredditsView;
    private SubscriptionsAdapter subredditsAdapter;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        viewModel = getViewModel();
        initViews();
        getNonNullActionBar().setTitle(getString(R.string.subreddits));
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
        initContinueButton();
    }

    private void initSubreddits() {
        subredditsView = findViewById(R.id.subreddits);
        subredditsView.setLayoutManager(new LinearLayoutManager(this));
        subredditsAdapter = new SubscriptionsAdapter(this, viewModel);
        subredditsView.setAdapter(subredditsAdapter);
        viewModel.getSubreddits().observe(this, this::updateViews);
    }

    private void updateViews(List<Subreddit> subreddits) {
        subredditsAdapter.update(subreddits);
        if (!subreddits.isEmpty()) {
            subredditsView.scrollToPosition(subreddits.size());
            continueButton.setEnabled(true);
        } else {
            continueButton.setEnabled(false);
        }
    }

    private void initContinueButton() {
        continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this::finish);
    }

    private void finish(@SuppressWarnings("unused") View controllerView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        viewModel.getSubreddits().removeObservers(this);
        super.onDestroy();
    }
}
