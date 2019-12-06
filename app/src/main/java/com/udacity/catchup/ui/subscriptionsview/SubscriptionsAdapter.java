package com.udacity.catchup.ui.subscriptionsview;

import android.content.Context;
import android.os.Build;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;
import com.udacity.catchup.data.entity.subreddit.Subreddit;

import java.lang.ref.WeakReference;
import java.util.List;

class SubscriptionsAdapter extends RecyclerView.Adapter<SubscriptionsAdapter.SubredditViewHolder> {

    private static final int VIEW_TYPE_SUBREDDIT_INFO = 0;
    private static final int VIEW_TYPE_SUBREDDIT_INPUT = 1;

    private WeakReference<Context> contextReference;
    private SubscriptionsActivityViewModel viewModel;
    private List<Subreddit> subreddits;

    SubscriptionsAdapter(Context context, SubscriptionsActivityViewModel viewModel) {
        contextReference = new WeakReference<>(context);
        this.viewModel = viewModel;
    }

    void update(List<Subreddit> subreddits) {
        this.subreddits = subreddits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubredditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(contextReference.get());
        return viewType == VIEW_TYPE_SUBREDDIT_INFO
                ? new SubredditViewHolder(inflateSubredditInfo(inflater, parent), viewType)
                : new SubredditViewHolder(inflateSubredditInput(inflater, parent), viewType);
    }

    private View inflateSubredditInfo(LayoutInflater inflater, ViewGroup parent) {
        return inflate(inflater, R.layout.item_subreddit, parent);
    }

    private View inflate(LayoutInflater inflater, int layoutId, ViewGroup parent) {
        return inflater.inflate(layoutId, parent, false);
    }

    private View inflateSubredditInput(LayoutInflater inflater, ViewGroup parent) {
        return inflate(inflater, R.layout.item_subreddit_input, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull SubredditViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SUBREDDIT_INFO) {
            holder.bind(subreddits.get(position));
        } else {
            holder.requestInputFocus();
        }
    }

    @Override
    public int getItemCount() {
        return subreddits != null ? subreddits.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < subreddits.size() ? VIEW_TYPE_SUBREDDIT_INFO : VIEW_TYPE_SUBREDDIT_INPUT;
    }

    class SubredditViewHolder extends RecyclerView.ViewHolder {

        private ImageView icon;
        private TextView name;
        private Button removeButton;

        private EditText input;

        SubredditViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == VIEW_TYPE_SUBREDDIT_INFO) {
                initSubredditInfo();
            } else {
                initSubredditInput();
            }
        }

        private void initSubredditInfo() {
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        private void initSubredditInput() {
            initInput();
            initAddButton();
        }

        private void initInput() {
            input = itemView.findViewById(R.id.subredditInput);
            input.setOnEditorActionListener(this::insertSubreddit);
        }

        private boolean insertSubreddit(TextView view,
                                        int actionId,
                                        @SuppressWarnings("unused") KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                insertSubreddit(view);
                return true;
            }
            return false;
        }

        private void insertSubreddit(@SuppressWarnings("unused") View addButton) {
            String inputText = input.getText().toString().trim();
            if (inputText.length() > 0) {
                viewModel.insertSubredditIfValid(input);
            }
        }

        private void initAddButton() {
            View addButton = itemView.findViewById(R.id.addSubredditButton);
            addButton.setOnClickListener(this::insertSubreddit);
        }

        private void bind(Subreddit subreddit) {
            loadOrHideIcon(subreddit.getIconUrl());
            name.setText(subreddit.getName());
            removeButton.setOnClickListener(v -> viewModel.removeSubreddit(subreddit));
        }

        private void requestInputFocus() {
            if (!input.hasFocus()) {
                input.requestFocus();
            }
        }

        private void loadOrHideIcon(String iconUrl) {
            if (!iconUrl.isEmpty()) {
                loadIcon(iconUrl);
            } else {
                icon.setVisibility(View.INVISIBLE);
            }
        }

        private void loadIcon(String iconUrl) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                icon.setClipToOutline(true);
            }
            Picasso.get().load(iconUrl).into(icon);
        }
    }
}
