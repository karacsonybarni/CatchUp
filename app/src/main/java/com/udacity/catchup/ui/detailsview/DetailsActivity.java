package com.udacity.catchup.ui.detailsview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.catchup.R;
import com.udacity.catchup.data.Repository;
import com.udacity.catchup.data.entity.comment.Comment;
import com.udacity.catchup.data.entity.comment.PageSection;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.network.RedditNetworkDataSource;
import com.udacity.catchup.util.InjectorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    public static final String POST_ID_EXTRA = "postId";

    private DetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initRecyclerView();
        DetailsActivityViewModel viewModel = getViewModel(getId());
        viewModel.getPost().observe(this, this::updatePost);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new DetailsAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
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
        adapter.updatePost(post);
        fetchComments(post);
    }

    private void fetchComments(Post post) {
        RedditNetworkDataSource
                .getInstance(this)
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
}
