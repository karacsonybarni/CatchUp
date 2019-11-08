package com.udacity.catchup.ui.postsview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Post;
import com.udacity.catchup.ui.MediaProvider;

import java.util.Objects;

public class PostFragment extends Fragment {

    private static final String POST_ID = "postId";

    private LiveData<Post> postLiveData;
    private Post post;
    private String validVideoUrl;

    private View rootView;
    private TextView subredditName;
    private TextView postDetails;
    private TextView title;
    private TextView bodyText;
    private ImageView image;
    private PlayerView playerView;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        rootView = inflateView(inflater, container);
        initViews();
        populateViews(savedInstanceState);
        return rootView;
    }

    private View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    private void initViews() {
        subredditName = rootView.findViewById(R.id.subredditName);
        postDetails = rootView.findViewById(R.id.postDetails);
        title = rootView.findViewById(R.id.title);
        bodyText = rootView.findViewById(R.id.bodyText);
        image = rootView.findViewById(R.id.image);
        playerView = rootView.findViewById(R.id.playerView);
    }

    private void populateViews(Bundle savedInstanceState) {
        if (post == null) {
            initPost(savedInstanceState);
        } else {
            populateViews();
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
        populateViews();
    }

    private void populateViews() {
        subredditName.setText(post.getSubredditName());
        postDetails.setText(getPostDetails());
        title.setText(post.getTitle());
        addMedia();
    }

    private String getPostDetails() {
        return getString(R.string.post_details, post.getAuthorName(), getPostTime());
    }

    private String getPostTime() {
        long time = post.getDate();
        long now = System.currentTimeMillis();
        CharSequence timeAgo =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return String.valueOf(timeAgo);
    }

    private void addMedia() {
        if (hasType("image")) {
            loadImage();
        } else if (hasVideo()) {
            loadVideo();
        } else {
            addLink();
        }
    }

    private boolean hasType(String type) {
        String postType = post.getType();
        return postType != null && postType.contains(type);
    }

    private void loadImage() {
        image.setVisibility(View.VISIBLE);
        Picasso
                .get()
                .load(post.getMediaUrl())
                .resize(2048, 1600)
                .onlyScaleDown()
                .into(image);
    }

    boolean hasVideo() {
        return hasType("video");
    }

    private void loadVideo() {
        playerView.setVisibility(View.VISIBLE);
        String videoUrl = post.getVideoUrl();
        validVideoUrl = videoUrl != null ? videoUrl : post.getMediaUrl();
        MediaProvider.initVideo(playerView, validVideoUrl);
    }

    private void addLink() {
        bodyText.setText(post.getMediaUrl());
        bodyText.setOnClickListener(this::openLink);
        bodyText.setTextColor(ContextCompat.getColor(getNonNullActivity(), R.color.colorAccent));
        bodyText.setVisibility(View.VISIBLE);
    }

    private void openLink(@SuppressWarnings("unused") View linkView) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post.getMediaUrl()));
        startActivity(browserIntent);
    }

    void setPost(Post post) {
        this.post = post;
    }

    void playVideo() {
        MediaProvider.playVideo(validVideoUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23 && isFinishing()) {
            MediaProvider.close();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && isFinishing()) {
            MediaProvider.close();
        }
    }

    private boolean isFinishing() {
        Activity activity = getActivity();
        return activity == null || activity.isFinishing();
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
