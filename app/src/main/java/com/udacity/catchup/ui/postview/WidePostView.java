package com.udacity.catchup.ui.postview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

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
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.layout_post_wide;
    }

    @Override
    void showImage() {
        super.showImage();
        mediaView.setVisibility(VISIBLE);
    }

    @Override
    void showVideo() {
        super.showVideo();
        mediaView.setVisibility(VISIBLE);
    }
}
