package com.udacity.catchup.ui.detailsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.util.ConfigurationUtils;
import com.udacity.catchup.util.InjectorUtils;

public class DetailsActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA = "postId";

    private DetailsActivityViewModel viewModel;
    private LiveData<Post> postLiveData;
    private DetailsActivityDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewModel = getViewModel(getId());
        postLiveData = viewModel.getPost();
        postLiveData.observe(this, this::updatePost);
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
        initDelegate(post);
        delegate.initWindow();
        delegate.initViews();
        delegate.updatePost(post);
    }

    private void initDelegate(Post post) {
        boolean hasMedia = post.hasImage() || post.hasVideo();
        if (ConfigurationUtils.isInLandscapeMode(this) && hasMedia) {
            delegate = new MediaDelegate(this);
        } else {
            delegate = new RecyclerViewDelegate(this);
        }
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
