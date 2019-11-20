package com.udacity.catchup.ui.postsview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.ui.detailsview.DetailsActivity;
import com.udacity.catchup.ui.postview.PostView;

import java.util.Objects;

public class PostFragment extends Fragment {

    private static final String POST_ID = "postId";

    private LiveData<Post> postLiveData;
    private Post post;

    private CardView card;
    private PostView postView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View rootView = inflateView(inflater, container);
        initViews(rootView);
        populateViews(savedInstanceState);
        return rootView;
    }

    private View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    private void initViews(View rootView) {
        card = rootView.findViewById(R.id.card);
        postView = card.findViewById(R.id.post);
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
        PostsActivityViewModel viewModel =
                ViewModelProviders.of(getNonNullActivity()).get(PostsActivityViewModel.class);
        postLiveData = viewModel.getPost(postId);
        postLiveData.observe(this, this::populateViews);
    }

    @NonNull
    private FragmentActivity getNonNullActivity() {
        return Objects.requireNonNull(getActivity());
    }

    private void populateViews(Post post) {
        this.post = post;
        postView.updatePost(post);
        card.setOnClickListener(this::startDetailsActivity);
    }

    private void startDetailsActivity(@SuppressWarnings("unused") View view) {
        Intent intent = new Intent(getNonNullActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.POST_ID_EXTRA, post.getId());
        startActivity(intent);
    }

    void setPost(Post post) {
        this.post = post;
    }

    boolean hasVideo() {
        return postView.hasVideo();
    }

    void playVideo() {
        postView.playVideo();
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
        super.onDestroy();
    }
}
