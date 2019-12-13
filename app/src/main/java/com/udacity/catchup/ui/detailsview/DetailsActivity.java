package com.udacity.catchup.ui.detailsview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
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
    private Post post;
    private DetailsActivityDelegate delegate;

    @Nullable
    private ShareActionProvider shareActionProvider;

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
        this.post = post;
        initShareButton();
        updateAppViews();
        updateWidgets();
    }

    private void initShareButton() {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        }
    }

    private Intent createShareIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, post.getMediaUrl());
        return sendIntent;
    }

    private void updateAppViews() {
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

    private void updateWidgets() {
        viewModel.setSeen(post);
        PostIntentService.startActionUpdateWidget(this);
    }

    DetailsActivityViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        MenuItem shareItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        if (post != null) {
            shareActionProvider.setShareIntent(createShareIntent());
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        postLiveData.removeObservers(this);
        delegate.close();
        super.onDestroy();
    }
}
