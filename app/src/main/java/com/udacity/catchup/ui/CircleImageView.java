package com.udacity.catchup.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.udacity.catchup.R;

public class CircleImageView extends CardView {

    private ImageView imageView;

    public CircleImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public CircleImageView(@NonNull Context context,
                           @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageView(@NonNull Context context,
                           @Nullable AttributeSet attrs,
                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_circle_image, this);
        imageView = findViewById(R.id.circleIcon);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setRadius(widthMeasureSpec / 2);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void load(String url) {
        Picasso.get().load(url).into(imageView);
    }
}
