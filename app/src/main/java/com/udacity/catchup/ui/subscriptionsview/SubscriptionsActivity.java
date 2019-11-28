package com.udacity.catchup.ui.subscriptionsview;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.CircleImageView;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;
import java.util.Objects;

public class SubscriptionsActivity extends AppCompatActivity {

    private SubscriptionsActivityViewModel viewModel;
    private ViewGroup subredditsView;
    private EditText subredditInput;
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
        initInputRow();
        initContinueButton();
    }

    private void initSubreddits() {
        subredditsView = findViewById(R.id.subreddits);
        viewModel.getSubreddits().observe(this, this::updateViews);
    }

    private void updateViews(List<Subreddit> subreddits) {
        updateSubreddits(subreddits);
        if (!subreddits.isEmpty()) {
            continueButton.setEnabled(true);
        } else {
            continueButton.setEnabled(false);
        }
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
        loadOrHideIcon(subredditItem, subreddit.getIconUrl());
        subredditsView.addView(subredditItem);
    }

    private void loadOrHideIcon(View subredditItem, String iconUrl) {
        View iconView = subredditItem.findViewById(R.id.icon);
        if (!iconUrl.isEmpty()) {
            loadIcon(iconView, iconUrl);
        } else {
            iconView.setVisibility(View.INVISIBLE);
        }
    }

    private void loadIcon(View iconView, String iconUrl) {
        if (iconView instanceof CircleImageView) {
            ((CircleImageView) iconView).load(iconUrl);
        } else {
            Picasso.get().load(iconUrl).into((ImageView) iconView);
        }
    }

    private void initInputRow() {
        subredditInput = findViewById(R.id.subredditInput);
        subredditInput.setOnEditorActionListener(getEnterKeyListener());
        Button addButton = findViewById(R.id.addSubredditButton);
        addButton.setOnClickListener(this::insertSubreddit);
    }

    private TextView.OnEditorActionListener getEnterKeyListener() {
        return (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                insertSubreddit(v);
                return true;
            }
            return false;
        };
    }

    private void insertSubreddit(@SuppressWarnings("unused") View addButton) {
        String inputText = subredditInput.getText().toString().trim();
        if (inputText.length() > 0) {
            viewModel.insertSubredditIfValid(subredditInput);
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
