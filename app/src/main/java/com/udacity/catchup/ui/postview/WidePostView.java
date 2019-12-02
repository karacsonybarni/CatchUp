package com.udacity.catchup.ui.postview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintSet;

import com.udacity.catchup.R;

public class WidePostView extends PostView {

    private View mediaView;

    public WidePostView(Context context) {
        super(context);
        init();
    }

    public WidePostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WidePostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mediaView = findViewById(R.id.media);
        getPostViewPopulator().setConstraintsUpdater(this::updateConstraints);
    }

    private void updateConstraints() {
        mediaView.setVisibility(VISIBLE);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        connectViewToMedia(constraintSet, R.id.title);
        connectViewToMedia(constraintSet, R.id.bodyText);
        connectViewToMedia(constraintSet, R.id.link);
        constraintSet.applyTo(this);
    }

    private void connectViewToMedia(ConstraintSet constraintSet, int viewId) {
        constraintSet.connect(viewId, ConstraintSet.END, R.id.media, ConstraintSet.START);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.layout_post_wide;
    }

    @Override
    void showVideo() {
        super.showVideo();
        mediaView.setVisibility(VISIBLE);
        updateConstraints();
    }
}
