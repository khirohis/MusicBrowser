package net.hogelab.musicbrowser.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.hogelab.musicbrowser.player.PlayerService;

/**
 * Created by kobayasi on 2017/04/25.
 */

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private boolean mPlayerServiceBound;
    private PlayerServiceConnection mPlayerServiceConnection = new PlayerServiceConnection();
    private Messenger mPlayerServiceMessenger;
    private Messenger mPlayerServiceResponseMessanger = new Messenger(new PlayerServiceResponseMessanger());
    private MediaControllerCallback mMediaControllerCallback = new MediaControllerCallback();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.v(this.getClass().getSimpleName(), "onStart");
        super.onStart();

        bindPlayerService();
    }

    @Override
    public void onStop() {
        Log.v(this.getClass().getSimpleName(), "onStop");
        super.onStop();

        unbindPlayerService();
        unregisterMediaControllerCallback();
    }

    @Override
    public void onDestroy() {
        Log.v(this.getClass().getSimpleName(), "onDestroy");
        super.onDestroy();
    }


    protected void onRegisteredMediaControllerCallback() {
    }

    protected void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
    }

    protected void onMetadataChanged(MediaMetadataCompat metadata) {
        if (metadata != null) {
        }
    }


    protected void startPlayTrack(String trackId) {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController != null) {
            mediaController.getTransportControls().playFromMediaId(trackId, null);
        }
    }


    private void bindPlayerService() {
        Log.d(this.getClass().getSimpleName(), "bindPlayerService");
        if (!mPlayerServiceBound) {
            bindService(new Intent(BaseActivity.this, PlayerService.class),
                    mPlayerServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unbindPlayerService() {
        Log.d(this.getClass().getSimpleName(), "unbindPlayerService");
        if (mPlayerServiceBound) {
            unbindService(mPlayerServiceConnection);
            mPlayerServiceBound = false;
        }
    }

    private void registerMediaControllerCallback(MediaSessionCompat.Token token) {
        try {
            MediaControllerCompat mediaController = new MediaControllerCompat(
                    BaseActivity.this, token);

            MediaControllerCompat.setMediaController(this, mediaController);
            mediaController.registerCallback(mMediaControllerCallback);

            onRegisteredMediaControllerCallback();
        } catch (RemoteException e) {
        }
    }

    private void unregisterMediaControllerCallback() {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController != null) {
            mediaController.unregisterCallback(mMediaControllerCallback);
        }
    }

    private void sendGetMediaSessionToken() {
        try {
            Message message = Message.obtain();
            message.what = PlayerService.COMMAND_GET_MEDIA_SESSION_TOKEN;
            message.replyTo = mPlayerServiceResponseMessanger;
            mPlayerServiceMessenger.send(message);
        } catch (RemoteException e) {
        }
    }

    private void onGetMediaSessionTokenReply(Message message) {
        MediaSessionCompat.Token token = (MediaSessionCompat.Token) message.obj;
        registerMediaControllerCallback(token);
    }


    private class PlayerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(TAG, "PlayerServiceConnection#onServiceConnected");
            mPlayerServiceMessenger = new Messenger(service);
            mPlayerServiceBound = true;

            sendGetMediaSessionToken();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "PlayerServiceConnection#onServiceDisconnected");
            mPlayerServiceMessenger = null;
            mPlayerServiceBound = false;
        }
    }

    private class PlayerServiceResponseMessanger extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {

                case PlayerService.COMMAND_GET_MEDIA_SESSION_TOKEN_REPLY:
                    onGetMediaSessionTokenReply(message);
                    break;

                default:
                    Log.v(TAG, "Unhandled message: " + message);
            }
        }
    }

    private class MediaControllerCallback extends MediaControllerCompat.Callback {

        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            Log.d(TAG, "MediaControllerCallback#onPlaybackStateChanged");
            BaseActivity.this.onPlaybackStateChanged(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            Log.d(TAG, "MediaControllerCallback#onMetadataChanged");
            BaseActivity.this.onMetadataChanged(metadata);
        }
    }
}
