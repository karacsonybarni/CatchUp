package com.udacity.catchup.ui.postview;

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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

class MediaProvider {

    private static final int MAX_POOL_SIZE = 5;

    private static MediaProvider INSTANCE;
    private LinkedHashMap<String, ExoPlayer> map;

    private MediaProvider() {
        map = new LinkedHashMap<>();
    }

    private static MediaProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MediaProvider();
        }
        return INSTANCE;
    }

    static void initVideoWithNewPlayer(PlayerView playerView, String url) {
        getInstance().remove(url);
        initVideo(playerView, url);
    }

    private void remove(String url) {
        map.remove(url);
    }

    static void initVideo(PlayerView playerView, String url) {
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
        if (map.size() < MAX_POOL_SIZE) {
            addPlayer(context, url);
        } else {
            updatePlayerFor(context, url);
        }
    }

    private void addPlayer(Context context, String url) {
        ExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        prepareMediaSource(context, exoPlayer, url);
        map.put(url, exoPlayer);
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

    private void updatePlayerFor(Context context, String newUrl) {
        String oldUrl = map.keySet().iterator().next();
        ExoPlayer exoPlayer = map.get(oldUrl);
        prepareMediaSource(context, Objects.requireNonNull(exoPlayer), newUrl);
        map.remove(oldUrl);
        map.put(newUrl, exoPlayer);
    }

    static void playVideo(String url) {
        ExoPlayer exoPlayer = getInstance().getExoPlayer(url);
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);
        }
    }

    static void close() {
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
    }
}
