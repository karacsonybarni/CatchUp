package com.udacity.catchup.ui.subscriptionsview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.ui.postsview.PostsActivity;
import com.udacity.catchup.util.InjectorUtils;

public class SubscriptionsActivity extends AppCompatActivity {

    private SubscriptionsActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions);

        viewModel = getViewModel();
        initViews();
    }

    private SubscriptionsActivityViewModel getViewModel() {
        Repository repository = InjectorUtils.getRepository(this);
        SubscriptionsViewModelFactory factory = new SubscriptionsViewModelFactory(repository);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(SubscriptionsActivityViewModel.class);
    }

    private void initViews() {
        EditText subredditInput = findViewById(R.id.subredditInput);
        Button addButton = findViewById(R.id.addSubredditButton);
        String subredditName = subredditInput.getText().toString();
        addButton.setOnClickListener(v -> viewModel.insertSubreddit(subredditName));

        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setOnClickListener(this::startPostsActivity);
    }

    private void startPostsActivity(@SuppressWarnings("unused") View controllerView) {
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
    }
}
