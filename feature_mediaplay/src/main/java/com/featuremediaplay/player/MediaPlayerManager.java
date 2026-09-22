package com.featuremediaplay.player;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class MediaPlayerManager {
    private static volatile MediaPlayerManager instance;
    private ExoPlayer mPlayer;

    public MediaPlayerManager(Context context) {
        this.mPlayer = new ExoPlayer.Builder(context).build();
    }

    public static MediaPlayerManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MediaPlayerManager.class) {
                if (instance == null) {
                    instance = new MediaPlayerManager(context.getApplicationContext());
                }
            }
        }
        return instance;

    }

    public void bindPlayerView(PlayerView playerView) {
        if (playerView != null && mPlayer != null) {
            playerView.setPlayer(mPlayer);
        }
    }

    public void play(String url) {
        if (mPlayer != null) {
            MediaItem mediaItem = MediaItem.fromUri(url);
            mPlayer.setMediaItem(mediaItem);
            mPlayer.prepare();
            mPlayer.play();
        }

    }

    public void playWhenReady(boolean playWhenReady) {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    /**
     * 播放页面销毁后播放器也销毁,只有在不停的播放视频时保证播放器单例
     */
    public void destroy() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.clearMediaItems();
            mPlayer.release();
            mPlayer = null;
            instance = null;
        }
    }
}
