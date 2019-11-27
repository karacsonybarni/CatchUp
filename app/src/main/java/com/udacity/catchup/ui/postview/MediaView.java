package com.udacity.catchup.ui.postview;

import android.content.Context;
import android.util.AttributeSet;

import com.udacity.catchup.R;

public class MediaView extends PostView {

    public MediaView(Context context) {
        super(context);
    }

    public MediaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.layout_media;
    }
}
