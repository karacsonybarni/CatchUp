package com.udacity.catchup.ui.detailsview;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.comment.Comment;
import com.udacity.catchup.data.entity.comment.PageSection;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.data.network.RedditNetworkDataSource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RecyclerViewDelegate implements DetailsActivityDelegate {

    private DetailsActivity activity;
    private DetailsActivityViewModel viewModel;
    private LiveData<Subreddit> subredditLiveData;
    private DetailsAdapter adapter;

    RecyclerViewDelegate(DetailsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initWindow() {
    }

    @Override
    public void initViews() {
        viewModel = activity.getViewModel();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView);
        adapter = new DetailsAdapter(activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider =
                new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updatePost(Post post) {
        adapter.updatePost(post);
        updateSubreddit(post.getSubredditName());
        fetchComments(post);
    }

    private void updateSubreddit(String subredditName) {
        subredditLiveData = viewModel.getSubreddit(subredditName);
        subredditLiveData.observe(activity, this::updateSubreddit);
    }

    private void updateSubreddit(Subreddit subreddit) {
        adapter.updateSubreddit(subreddit);
    }

    private void fetchComments(Post post) {
        RedditNetworkDataSource
                .getInstance(activity)
                .fetchComments(
                        post.getSubredditName(),
                        post.getId(),
                        new Callback<List<PageSection>>() {
                            @Override
                            public void onResponse(Call<List<PageSection>> call,
                                                   Response<List<PageSection>> response) {
                                List<Comment> comments = TypeConverter.toComments(response.body());
                                adapter.updateComments(comments);
                            }

                            @Override
                            public void onFailure(Call<List<PageSection>> call, Throwable t) {

                            }
                        });
    }

    @Override
    public void close() {
        subredditLiveData.removeObservers(activity);
    }
}
