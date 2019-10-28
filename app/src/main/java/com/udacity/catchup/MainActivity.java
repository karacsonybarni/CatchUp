package com.udacity.catchup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.udacity.catchup.data.network.RedditNetworkDataSource;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RedditNetworkDataSource.getInstance(this).fetchPosts();
    }
}
