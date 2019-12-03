package com.udacity.catchup.ui.postview;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;

class AppPostViewPopulator implements PostViewPopulatorDelegate {

    private Runnable constraintsUpdater;

    private ViewGroup rootView;
    private ImageView subredditIconView;
    private ViewGroup headerTextsView;
    private TextView subredditNameView;
    private TextView postDetailsView;
    private TextView titleView;
    private TextView bodyTextView;
    private TextView linkView;
    private ImageView imageView;

    AppPostViewPopulator(ViewGroup rootView) {
        this.rootView = rootView;
        subredditIconView = rootView.findViewById(R.id.icon);
        headerTextsView = rootView.findViewById(R.id.headerTexts);
        if (headerTextsView != null) {
            subredditNameView = headerTextsView.findViewById(R.id.subredditName);
            postDetailsView = headerTextsView.findViewById(R.id.postDetails);
        }
        titleView = rootView.findViewById(R.id.title);
        bodyTextView = rootView.findViewById(R.id.bodyText);
        linkView = rootView.findViewById(R.id.link);
        imageView = rootView.findViewById(R.id.image);
        setPaddings(rootView);
    }

    private void setPaddings(ViewGroup root) {
        if (subredditNameView != null) {
            int padding =
                    subredditNameView.getResources().getDimensionPixelSize(R.dimen.postView_padding);
            root.setPadding(padding, padding, padding, padding);
        }
    }

    @Override
    public Context getContext() {
        return rootView.getContext();
    }

    @Override
    public void loadSubredditIcon(String iconUrl) {
        if (subredditIconView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                subredditIconView.setClipToOutline(true);
            }
            Picasso.get().load(iconUrl).into(subredditIconView);
        }
    }

    @Override
    public void hideSubredditIconAndUpdateLayout() {
        if (subredditIconView == null) {
            return;
        }
        hideSubredditIcon();
        updateLayoutAfterHidingIcon();
    }

    private void hideSubredditIcon() {
        subredditIconView.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams iconLP = subredditIconView.getLayoutParams();
        iconLP.width = 0;
        subredditIconView.setLayoutParams(iconLP);
    }

    private void updateLayoutAfterHidingIcon() {
        ConstraintLayout.LayoutParams headerTextsLP =
                (ConstraintLayout.LayoutParams) headerTextsView.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            headerTextsLP.setMarginStart(0);
        } else {
            headerTextsLP.leftMargin = 0;
        }
        headerTextsView.setLayoutParams(headerTextsLP);
    }

    @Override
    public void fillSubredditName(String subredditName) {
        if (subredditNameView != null) {
            subredditNameView.setText(subredditName);
        }
    }

    @Override
    public void fillPostDetails(String postDetails) {
        if (postDetailsView != null) {
            postDetailsView.setText(postDetails);
        }
    }

    @Override
    public void fillTitle(String title) {
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    @Override
    public void fillBodyText(String bodyText) {
        if (bodyTextView != null) {
            bodyTextView.setText(bodyText);
            bodyTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showImage(String imageUrl) {
        updateConstraints();
        imageView.setVisibility(View.VISIBLE);
        Picasso
                .get()
                .load(imageUrl)
                .resize(2048, 0)
                .onlyScaleDown()
                .into(imageView);
    }

    private void updateConstraints() {
        if (constraintsUpdater != null) {
            constraintsUpdater.run();
        }
    }

    @Override
    public void addLink(String link, View.OnClickListener onClickListener) {
        if (this.linkView == null || link == null) {
            return;
        }
        linkView.setText(link);
        linkView.setOnClickListener(onClickListener);
        linkView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        linkView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setConstraintsUpdater(Runnable constraintsUpdater) {
        this.constraintsUpdater = constraintsUpdater;
    }

    @Override
    public void setOnClickIntent(PendingIntent intent) {

    }
}
