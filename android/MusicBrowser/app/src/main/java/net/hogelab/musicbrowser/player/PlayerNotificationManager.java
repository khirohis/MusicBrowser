package net.hogelab.musicbrowser.player;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

/**
 * Created by kobayasi on 2017/05/01.
 */

public class PlayerNotificationManager extends BroadcastReceiver {
    private static final String TAG = PlayerNotificationManager.class.getSimpleName();

    private static final int NOTIFICATION_ID = 10000;
    private static final int REQUEST_CODE = 10000;

    public static final String ACTION_PLAY = "net.hogelab.musicbrowser.player.action.PLAY";
    public static final String ACTION_STOP = "net.hogelab.musicbrowser.player.action.STOP";
    public static final String ACTION_PAUSE = "net.hogelab.musicbrowser.player.action.PAUSE";
    public static final String ACTION_TOGGLE_PLAYBACK = "net.hogelab.musicbrowser.player.action.TOGGLE_PLAYBACK";
    public static final String ACTION_SKIP_TO_PREVIOUS = "net.hogelab.musicbrowser.player.action.SKIP_TO_PREVIOUS";
    public static final String ACTION_SKIP_TO_NEXT = "net.hogelab.musicbrowser.player.action.SKIP_TO_NEXT";

    private final PlayerService mService;
    private MediaSessionCompat.Token mSessionToken;
    private MediaControllerCompat mController;
    private MediaControllerCompat.TransportControls mTransportControls;
    private MediaControllerCallback mMediaControllerCallback = new MediaControllerCallback();

    private final NotificationManagerCompat mNotificationManager;

    private PendingIntent mPlayIntent;
    private PendingIntent mStopIntent;
    private PendingIntent mPauseIntent;
    private PendingIntent mTogglePlaybackIntent;
    private PendingIntent mSkipToPreviousIntent;
    private PendingIntent mSkipToNextIntent;

    private boolean mNotificationStarted = false;

    private PlaybackStateCompat mPlaybackState;
    private MediaMetadataCompat mMetadata;


    public PlayerNotificationManager(PlayerService service) {
        mService = service;
        try {
            mSessionToken = service.getSessionToken();
            mController = new MediaControllerCompat(mService, mSessionToken);
            mTransportControls = mController.getTransportControls();
        } catch (RemoteException e) {
        }

        mNotificationManager = NotificationManagerCompat.from(service);

        String packageName = mService.getPackageName();
        mPlayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
        mStopIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_STOP).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
        mPauseIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PAUSE).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
        mTogglePlaybackIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_TOGGLE_PLAYBACK).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
        mSkipToPreviousIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_SKIP_TO_PREVIOUS).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
        mSkipToNextIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_SKIP_TO_NEXT).setPackage(packageName), PendingIntent.FLAG_CANCEL_CURRENT);
    }


    public void startNotification() {
        if (!mNotificationStarted) {
            Notification notification = createNotification();
            if (notification != null) {
                mController.registerCallback(mMediaControllerCallback);

                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_SKIP_TO_PREVIOUS);
                filter.addAction(ACTION_SKIP_TO_NEXT);
                mService.registerReceiver(this, filter);

                mService.startForeground(NOTIFICATION_ID, notification);
                mNotificationStarted = true;
            }
        }
    }

    public void stopNotification() {
        if (mNotificationStarted) {
            mNotificationManager.cancel(NOTIFICATION_ID);
            mNotificationStarted = false;

            mController.unregisterCallback(mMediaControllerCallback);
            mService.unregisterReceiver(this);
            mService.stopForeground(true);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "onReceive: action = " + action);
        switch (action) {
            case ACTION_PLAY:
                mTransportControls.play();
                break;

            case ACTION_STOP:
                mTransportControls.stop();
                break;

            case ACTION_PAUSE:
                mTransportControls.pause();
                break;

            case ACTION_TOGGLE_PLAYBACK:
                if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PAUSED) {
                    mTransportControls.play();
                } else if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                    mTransportControls.pause();
                }
                break;

            case ACTION_SKIP_TO_PREVIOUS:
                mTransportControls.skipToPrevious();
                break;

            case ACTION_SKIP_TO_NEXT:
                mTransportControls.skipToNext();
                break;

            default:
                break;
        }
    }


    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);

        long actions = mPlaybackState.getActions();
        if ((actions & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            builder.addAction(android.R.drawable.ic_media_previous, "Skip To Previous", mSkipToPreviousIntent);
        }

        if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING &&
                mPlaybackState.getPosition() >= 0) {
            builder.setWhen(System.currentTimeMillis() - mPlaybackState.getPosition())
                    .setShowWhen(true)
                    .setUsesChronometer(true);
        } else {
            builder.setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false);
        }

        if ((actions & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
            builder.addAction(android.R.drawable.ic_media_next, "Skip To Next", mSkipToNextIntent);
        }

        MediaStyle mediaStyle = new MediaStyle();
        mediaStyle.setMediaSession(mSessionToken)
                .setShowActionsInCompactView(0, 1, 2);
        builder.setStyle(mediaStyle);

        MediaDescriptionCompat description = mMetadata.getDescription();
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle());

        return builder.build();
    }


    private class MediaControllerCallback extends MediaControllerCompat.Callback {

        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            Log.d(TAG, "MediaControllerCallback#onPlaybackStateChanged");
            mPlaybackState = state;
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "MediaControllerCallback#onMetadataChanged");
            mMetadata = metadata;
        }

        @Override
        public void onSessionDestroyed() {
            Log.d(TAG, "MediaControllerCallback#onSessionDestroyed");
            super.onSessionDestroyed();

            if (mController != null) {
                mController.unregisterCallback(mMediaControllerCallback);
            }

            mSessionToken = mService.getSessionToken();
            try {
                mController = new MediaControllerCompat(mService, mSessionToken);
                mTransportControls = mController.getTransportControls();
                if (mNotificationStarted) {
                    mController.registerCallback(mMediaControllerCallback);
                }
            } catch (RemoteException e) {
                mController = null;
                mTransportControls = null;
            }
        }
    }
}
