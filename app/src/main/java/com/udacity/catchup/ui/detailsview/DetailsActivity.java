package com.udacity.catchup.ui.detailsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.ui.widget.PostIntentService;
import com.udacity.catchup.util.ConfigurationUtils;
import com.udacity.catchup.util.InjectorUtils;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA = "postId";

    private DetailsActivityViewModel viewModel;
    private LiveData<Post> postLiveData;
    private DetailsActivityDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        showUpButton();
        viewModel = getViewModel(getId());
        postLiveData = viewModel.getPost();
        postLiveData.observe(this, this::updatePost);
    }

    private void showUpButton() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
        updateAppViews(post);
        updateWidgets(post);
    }

    private void updateAppViews(Post post) {
        initDelegate(post);
        delegate.initWindow();
        delegate.initViews();
        delegate.updatePost(post);
    }

    private void initDelegate(Post post) {
        if (post == null) {
            return;
        }
        boolean hasMedia = post.hasImage() || post.hasVideo();
        if (ConfigurationUtils.isInLandscapeMode(this) && hasMedia) {
            delegate = new MediaDelegate(this);
        } else {
            delegate = new RecyclerViewDelegate(this);
        }
    }

    private void updateWidgets(Post post) {
        viewModel.setSeen(post);
        PostIntentService.startActionUpdateWidget(this);
    }

    DetailsActivityViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onDestroy() {
        postLiveData.removeObservers(this);
        delegate.close();
        super.onDestroy();
    }
}
