package com.udacity.catchup.ui.postview;

import android.content.Context;
import android.util.AttributeSet;

import com.udacity.catchup.R;

public class WidePostView extends PostView {

    public WidePostView(Context context) {
        super(context);
    }

    public WidePostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidePostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    int getLayoutResourceId() {
        return R.layout.layout_post_wide;
    }
}
