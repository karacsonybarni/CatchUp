package com.udacity.catchup.ui.postsview;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Post;

import java.util.Objects;

public class PostFragment extends Fragment {

    private static final String POST_ID = "postId";

    private LiveData<Post> postLiveData;
    private Post post;

    private View rootView;
    private TextView subredditName;
    private TextView postDetails;
    private TextView title;
    private ImageView image;

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
        image = rootView.findViewById(R.id.image);
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
        addImageIfIncluded();
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

    private void addImageIfIncluded() {
        if (hasImage()) {
            image.setVisibility(View.VISIBLE);
            Picasso.get().load(post.getMediaUrl()).into(image);
        }
    }

    private boolean hasImage() {
        String type = post.getType();
        return type != null && type.contains("image");
    }

    void setPost(Post post) {
        this.post = post;
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
