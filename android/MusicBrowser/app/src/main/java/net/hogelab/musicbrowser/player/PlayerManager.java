package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import net.hogelab.musicbrowser.model.entity.TrackEntity;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by kobayasi on 2017/04/25.
 */

public class PlayerManager {
    private static final String TAG = PlayerManager.class.getSimpleName();

    private final Context mApplicationContext;
    private final Callback mCallback;
    private final MediaSessionCallback mMediaSessionCallback = new MediaSessionCallback();

    private final PlayerQueueManager mPlayerQueueManager;
    private Player mPlayer;
    private final Player.Callback mPlayerCallback = new PlayerCallback();

    private final WifiManager.WifiLock mWifiLock;


    public PlayerManager(Context applicationContext, Callback callback, PlayerQueueManager queueManager, Player player) {
        mApplicationContext = applicationContext;
        mCallback = callback;

        mPlayerQueueManager = queueManager;
        mPlayer = player;
        mPlayer.setPlayerCallback(mPlayerCallback);

        mWifiLock = ((WifiManager) mApplicationContext.getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "musicbrowser_lock");
    }


    public MediaSessionCompat.Callback getMediaSessionCallback() {
        return mMediaSessionCallback;
    }


    private void handlePlayFromMediaId(String mediaId, Bundle extras) {
        Realm realm = Realm.getDefaultInstance();
        TrackEntity entity = TrackEntity.queryById(realm, mediaId).findFirstAsync();
        entity.addChangeListener(new RealmChangeListener<TrackEntity>() {
            @Override
            public void onChange(TrackEntity element) {
                if (element.isValid() && element.isLoaded()) {
                    Uri uri = Uri.parse(element.getData());
                    MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                            .setMediaId(mediaId)
                            .setTitle(element.getTitle())
                            .setMediaUri(uri)
                            .build();
                    MediaSessionCompat.QueueItem queuItem = new MediaSessionCompat.QueueItem(description, element.getTrack());
                    mPlayer.play(queuItem);
                }
            }
        });
    }


    private void handlePlay() {
        mPlayer.start();
        mCallback.onStartPlay();
    }

    private void handlePause() {
    }

    private void handleStop() {
        mPlayer.stop();
        mCallback.onStopPlay();
    }


    private void handleCompletion() {
    }

    private void handleStatusChanged(int status) {
    }

    private void handleError(String error) {
    }


    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onCommand(String command, Bundle extras, ResultReceiver cb) {
            Log.d(TAG, "MediaSessionCallback#onCommand");
        }

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            Log.d(TAG, "MediaSessionCallback#onMediaButtonEvent");
            boolean handled = false;

            return handled;
        }


        @Override
        public void onPrepare() {
            Log.d(TAG, "MediaSessionCallback#onPrepare");
        }

        @Override
        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPrepareFromMediaId");
        }

        @Override
        public void onPrepareFromSearch(String query, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPrepareFromSearch");
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPrepareFromUri");
        }


        @Override
        public void onPlay() {
            Log.d(TAG, "MediaSessionCallback#onPlay");
            handlePlay();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPlayFromMediaId");
            handlePlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onPlayFromSearch(final String query, final Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPlayFromSearch");
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onPlayFromUri");
        }


        @Override
        public void onSkipToQueueItem(long queueId) {
            Log.d(TAG, "MediaSessionCallback#onSkipToQueueItem");
        }


        @Override
        public void onPause() {
            Log.d(TAG, "MediaSessionCallback#onPause");
            handlePause();
        }

        @Override
        public void onSkipToNext() {
            Log.d(TAG, "MediaSessionCallback#onSkipToNext");
        }

        @Override
        public void onSkipToPrevious() {
            Log.d(TAG, "MediaSessionCallback#onSkipToPrevious");
        }

        @Override
        public void onFastForward() {
            Log.d(TAG, "MediaSessionCallback#onFastForward");
        }

        @Override
        public void onRewind() {
            Log.d(TAG, "MediaSessionCallback#onRewind");
        }

        @Override
        public void onStop() {
            Log.d(TAG, "MediaSessionCallback#onStop");
            handleStop();
        }

        @Override
        public void onSeekTo(long position) {
            Log.d(TAG, "MediaSessionCallback#onSeekTo");
        }


        @Override
        public void onSetRating(RatingCompat rating) {
            Log.d(TAG, "MediaSessionCallback#onSetRating");
        }

        @Override
        public void onSetRepeatMode(@PlaybackStateCompat.RepeatMode int repeatMode) {
            Log.d(TAG, "MediaSessionCallback#onSetRepeatMode");
        }

        @Override
        public void onSetShuffleModeEnabled(boolean enabled) {
            Log.d(TAG, "MediaSessionCallback#onSetShuffleModeEnabled");
        }


        @Override
        public void onCustomAction(@NonNull String action, Bundle extras) {
            Log.d(TAG, "MediaSessionCallback#onCustomAction");
        }


        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG, "MediaSessionCallback#onAddQueueItem");
        }

        @Override
        public void onAddQueueItem(MediaDescriptionCompat description, int index) {
            Log.d(TAG, "MediaSessionCallback#onAddQueueItem");
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            Log.d(TAG, "MediaSessionCallback#onRemoveQueueItem");
        }

        @Override
        public void onRemoveQueueItemAt(int index) {
            Log.d(TAG, "MediaSessionCallback#onRemoveQueueItemAt");
        }
    }


    private class PlayerCallback implements Player.Callback {

        @Override
        public void onCompletion() {
            Log.d(TAG, "PlayerCallback#onCompletion");
            handleCompletion();
        }

        @Override
        public void onStatusChanged(int state) {
            Log.d(TAG, "PlayerCallback#onStatusChanged");
            handleStatusChanged(state);
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "PlayerCallback#onError");
            handleError(error);
        }
    }


    public interface Callback {
        void onStartPlay();
        void onStopPlay();
        void onPlayStateUpdated(PlaybackStateCompat state);
    }
}
