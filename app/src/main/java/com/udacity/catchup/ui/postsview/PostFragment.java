package com.udacity.catchup.ui.postsview;

import android.annotation.SuppressLint;
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

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.Post;

public class PostFragment extends Fragment {

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

    void updatePost(Post post) {
        this.post = post;
        populateViews();
    }

    @SuppressLint("SetTextI18n")
    private void populateViews() {
        subredditName.setText("EarthPorn");
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
}
