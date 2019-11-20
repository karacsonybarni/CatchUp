package com.udacity.catchup.ui.detailsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.ui.postview.PostView;
import com.udacity.catchup.util.InjectorUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA = "postId";

    private PostView postView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initPostView();
        postView = findViewById(R.id.post);
        DetailsActivityViewModel viewModel = getViewModel(getId());
        viewModel.getPost().observe(this, this::updatePost);
    }

    private void initPostView() {
        postView = findViewById(R.id.post);
        postView.useNewVideoPlayerInstance();
    }

    private String getId() {
        return getIntent().getStringExtra(POST_ID_EXTRA);
    }

    private DetailsActivityViewModel getViewModel(String id) {
        Repository repository = InjectorUtils.getRepository(this);
        DetailsViewModelFactory factory = new DetailsViewModelFactory(repository, id);
        ViewModelProvider viewModelProvider = ViewModelProviders.of(this, factory);
        return viewModelProvider.get(DetailsActivityViewModel.class);
    }

    private void updatePost(Post post) {
        postView.updatePost(post);
        if (postView.hasVideo()) {
            postView.playVideo();
        }
    }
}
