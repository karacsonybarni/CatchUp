package com.udacity.catchup.ui.detailsview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.comment.Comment;
import com.udacity.catchup.data.entity.post.Post;
import com.udacity.catchup.data.entity.subreddit.Subreddit;
import com.udacity.catchup.ui.postview.PostView;

import java.util.List;

class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.CommentViewHolder> {

    private static final int VIEW_TYPE_POST = 0;
    private static final int VIEW_TYPE_COMMENT = 1;

    private Context context;
    private Post post;
    private List<Comment> comments;

    DetailsAdapter(Context context) {
        this.context = context;
    }

    void updatePost(Post post) {
        if (post != null) {
            this.post = post;
            notifyDataSetChanged();
        }
    }

    void updateSubreddit(Subreddit subreddit) {
        if (subreddit != null) {
            post.setSubreddit(subreddit);
            notifyDataSetChanged();
        }
    }

    void updateComments(List<Comment> comments) {
        if (comments != null) {
            this.comments = comments;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_POST) {
            return new CommentViewHolder(createPostView(), viewType);
        } else {
            return new CommentViewHolder(inflateCommentView(parent), viewType);
        }
    }

    private View createPostView() {
        PostView postView = new PostView(context);
        postView.useNewVideoPlayerInstance();
        postView.updatePost(post);
        initPostLayout(postView);
        return postView;
    }

    private void initPostLayout(PostView postView) {
        RecyclerView.LayoutParams layoutParams =
                new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        postView.setLayoutParams(layoutParams);
    }

    private View inflateCommentView(ViewGroup parent) {
        return LayoutInflater
                .from(context)
                .inflate(R.layout.item_comment, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(position - 1, getItemViewType(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_POST : VIEW_TYPE_COMMENT;
    }

    @Override
    public int getItemCount() {
        if (post == null) {
            return 0;
        }
        return comments != null ? comments.size() + 1 : 1;
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView body;

        CommentViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_COMMENT) {
                author = itemView.findViewById(R.id.author);
                body = itemView.findViewById(R.id.body);
            }
        }

        private void bind(int position, int viewType) {
            if (viewType == VIEW_TYPE_POST) {
                bindPost();
            } else {
                bindComment(comments.get(position));
            }
        }

        private void bindPost() {
            PostView postView = (PostView) itemView;
            postView.loadOrHideSubredditIcon();
            postView.playVideo();
        }

        private void bindComment(Comment comment) {
            author.setText(comment.getAuthor());
            body.setText(comment.getBody());
        }
    }
}
