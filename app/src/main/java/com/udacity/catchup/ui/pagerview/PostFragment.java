package com.udacity.catchup.ui.pagerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.detailsview.DetailsActivity;
import com.udacity.catchup.ui.postview.PostView;

import java.util.Objects;

public class PostFragment extends Fragment {

    private static final String POST_ID = "postId";

    private PagerActivityViewModel viewModel;
    private LiveData<Post> postLiveData;
    private LiveData<Subreddit> subredditLiveData;
    private Post post;

    private PostView postView;
    private View textTransitioner;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflateView(inflater, container);
        initPostView(rootView);
        populateViews(savedInstanceState);
        return rootView;
    }

    private View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    private void initPostView(View rootView) {
        postView = rootView.findViewById(R.id.post);
        postView.setOnClickListener(this::startDetailsActivity);
        textTransitioner = rootView.findViewById(R.id.textTransitioner);
    }

    private void startDetailsActivity(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(getNonNullActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.POST_ID_EXTRA, post.getId());
        startActivity(intent);
    }

    private void populateViews(Bundle savedInstanceState) {
        if (post == null) {
            initPost(savedInstanceState);
        } else {
            populateViews(post);
        }
    }

    private void initPost(Bundle savedInstanceState) {
        String postId = savedInstanceState.getString(POST_ID);
        postLiveData = getViewModel().getPost(postId);
        postLiveData.observe(this, this::populateViews);
    }

    private PagerActivityViewModel getViewModel() {
        if (viewModel == null) {
            ViewModelProvider viewModelProvider = ViewModelProviders.of(getNonNullActivity());
            viewModel = viewModelProvider.get(PagerActivityViewModel.class);
        }
        return viewModel;
    }

    @NonNull
    private FragmentActivity getNonNullActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private void populateViews(Post post) {
        this.post = post;
        postView.updatePost(post);
        if (post.getSubreddit() == null) {
            loadSubreddit();
        }
        if (!postView.hasMedia()) {
            textTransitioner.setVisibility(View.VISIBLE);
        }
    }

    private void loadSubreddit() {
        subredditLiveData = getViewModel().getSubreddit(post.getSubredditName());
        subredditLiveData.observe(this, this::updateSubredditInPostView);
    }

    private void updateSubredditInPostView(Subreddit subreddit) {
        post.setSubreddit(subreddit);
        postView.loadOrHideSubredditIcon();
    }

    void setPost(Post post) {
        this.post = post;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (post != null && post.hasVideo()) {
            postView.playVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        postView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        postView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(POST_ID, post.getId());
    }

    @Override
    public void onDestroy() {
        if (postLiveData != null) {
            postLiveData.removeObservers(this);
        }
        if (subredditLiveData != null) {
            subredditLiveData.removeObservers(this);
        }
        super.onDestroy();
    }
}
