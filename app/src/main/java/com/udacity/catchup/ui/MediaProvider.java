package com.udacity.catchup.ui;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MediaProvider {

    private static final int MAX_POOL_SIZE = 3;

    private static MediaProvider INSTANCE;
    private Map<String, ExoPlayer> map;
    private List<String> urls;
    private int currentPoolIndex;

    private MediaProvider() {
        map = new HashMap<>();
        urls = new ArrayList<>();
    }

    private static MediaProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MediaProvider();
        }
        return INSTANCE;
    }

    public static void initVideo(PlayerView playerView, String url) {
        ExoPlayer exoPlayer = getInstance().getExoPlayer(playerView.getContext(), url);
        playerView.setPlayer(exoPlayer);
    }

    private ExoPlayer getExoPlayer(Context context, String url) {
        ExoPlayer exoPlayer = getExoPlayer(url);
        if (exoPlayer == null) {
            updateMap(context, url);
            exoPlayer = map.get(url);
        }
        return exoPlayer;
    }

    @Nullable
    private ExoPlayer getExoPlayer(String url) {
        return map.get(url);
    }

    private void updateMap(Context context, String url) {
        if (urls.size() < MAX_POOL_SIZE) {
            addPlayer(context, url);
        } else {
            updatePlayerFor(context, url);
        }
    }

    private void addPlayer(Context context, String url) {
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        prepareMediaSource(context, exoPlayer, url);
        put(url, exoPlayer);
    }

    private void prepareMediaSource(Context context, ExoPlayer exoPlayer, String mediaSourceUrl) {
        exoPlayer.setPlayWhenReady(false);
        MediaSource mediaSource = newProgressiveMediaSource(context, mediaSourceUrl);
        exoPlayer.prepare(mediaSource);
    }

    private MediaSource newProgressiveMediaSource(Context context, String url) {
        Uri uri = Uri.parse(url);
        return newMediaSourceFactory(context).createMediaSource(uri);
    }

    private ProgressiveMediaSource.Factory newMediaSourceFactory(Context context) {
        String userAgent = Util.getUserAgent(context, "CatchUp");
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(context, userAgent);
        ExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();
        return new ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory);
    }

    private void put(String url, ExoPlayer exoPlayer) {
        map.put(url, exoPlayer);
        urls.add(url);
        currentPoolIndex = urls.size() - 1;
    }

    private void updatePlayerFor(Context context, String newUrl) {
        String oldUrl = getOldUrl();
        ExoPlayer exoPlayer = map.get(oldUrl);
        prepareMediaSource(context, Objects.requireNonNull(exoPlayer), newUrl);
        remove(oldUrl);
        put(newUrl, exoPlayer);
    }

    private String getOldUrl() {
        currentPoolIndex++;
        if (currentPoolIndex >= map.size()) {
            currentPoolIndex = 0;
        }
        return urls.get(currentPoolIndex);
    }

    private void remove(String url) {
        map.remove(url);
        urls.remove(url);
    }

    public static void playVideo(String url) {
        ExoPlayer exoPlayer = getInstance().getExoPlayer(url);
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    public static void close() {
        getInstance().closePool();
        INSTANCE = null;
    }

    private void closePool() {
        if (map == null) {
            return;
        }
        for (Map.Entry<String, ExoPlayer> entry : map.entrySet()) {
            ExoPlayer exoPlayer = entry.getValue();
            exoPlayer.stop();
            exoPlayer.release();
        }
        map = null;
        urls = null;
    }
}
