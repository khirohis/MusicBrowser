package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import java.io.IOException;

/**
 * Created by kobayasi on 2017/04/28.
 */

public class SystemPlayer implements Player {
    private final String TAG = SystemPlayer.class.getSimpleName();

    // TODO: audio focus control, wifi lock

    private final Context mContext;
    private Callback mCallback;

    private MediaPlayer mMediaPlayer;


    public SystemPlayer(Context context) {
        mContext = context;
    }


    @Override
    public void setPlayerCallback(Callback callback) {
        mCallback = callback;
    }


    @Override
    public void start() {
        Log.d(TAG, "start");
        setupMediaPlayer();
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        releaseMediaPlayer();
    }


    @Override
    public void play(MediaSessionCompat.QueueItem item) {
        Log.d(TAG, "play");
        setupMediaPlayer();

        try {
            mMediaPlayer.setDataSource(mContext, item.getDescription().getMediaUri());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
        }
    }


    private void setupMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setWakeMode(mContext.getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);

            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
        } else {
            mMediaPlayer.reset();
        }
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

//        if (mWifiLock.isHeld()) {
//            mWifiLock.release();
//        }
    }


    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mCallback != null) {
                mCallback.onCompletion();
            }
        }
    };

    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(MediaPlayer mp) {
        }
    };

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            boolean handled = false;

            return handled;
        }
    };

    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            boolean handled = false;

            return handled;
        }
    };
}
