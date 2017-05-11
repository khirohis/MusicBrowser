package net.hogelab.musicbrowser.player;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.util.List;

/**
 * Created by kobayasi on 2017/04/25.
 */

public class PlayerService extends Service {
    private static final String TAG = PlayerService.class.getSimpleName();

//    public static final String SERVICE_INTERFACE = "net.hogelab.musicbrowser.service.PlayerService";

    public static final int COMMAND_GET_MEDIA_SESSION_TOKEN = 1000;
    public static final int COMMAND_GET_MEDIA_SESSION_TOKEN_REPLY = 1001;

    public static final int COMMAND_GET_MEDIA_QUEUE_ITEMS = 2000;
    public static final int COMMAND_GET_MEDIA_QUEUE_ITEMS_REPLY = 2001;

    private final Handler mHandler = new PlayerServiceHandler();
    private Messenger mMessenger;

    private PlayerQueueManager mPlayerQueueManager;
    private PlayerQueueManager.Callback mPlayerQueueManagerCallback = new PlayerQueueManagerCallback();
    private PlayerManager mPlayerManager;
    private PlayerManager.Callback mPlayerManagerCallback = new PlayerManagerCallback();

    private MediaSessionCompat mSession;

    private PlayerNotificationManager mNotificationManager;


    public static Intent newIntent(Context context) {
        return new Intent(context, PlayerService.class);
    }


    public MediaSessionCompat getSession() {
        return mSession;
    }

    public MediaSessionCompat.Token getSessionToken() {
        return mSession.getSessionToken();
    }


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        mMessenger = new Messenger(mHandler);

        Context applicationContext = getApplicationContext();
        mPlayerQueueManager = new PlayerQueueManager(applicationContext, mPlayerQueueManagerCallback);
        mPlayerManager = new PlayerManager(applicationContext, mPlayerManagerCallback,
                mPlayerQueueManager, new SystemPlayer(applicationContext));

        mSession = new MediaSessionCompat(this, "PlayerService");
        mSession.setCallback(mPlayerManager.getMediaSessionCallback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mNotificationManager = new PlayerNotificationManager(this);
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if (startIntent != null) {
//            String action = startIntent.getAction();
            MediaButtonReceiver.handleIntent(mSession, startIntent);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        // TODO: 再生停止
//        mPlayerManager.stop?
        mNotificationManager.stopNotification();

        mSession.release();
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return false;
    }


    private void onGetMediaSession(Message message) {
        try {
            Message reply = Message.obtain();
            reply.what = COMMAND_GET_MEDIA_SESSION_TOKEN_REPLY;
            reply.obj = getSessionToken();
            message.replyTo.send(reply);
        } catch (RemoteException e) {
        }
    }

    private void onGetMediaQueueItems(Message message) {
        try {
            Message reply = Message.obtain();
            reply.what = COMMAND_GET_MEDIA_QUEUE_ITEMS_REPLY;
            reply.obj = mPlayerQueueManager.getMasterQueue();
            message.replyTo.send(reply);
        } catch (RemoteException e) {
        }
    }


    private class PlayerServiceHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            switch (message.what) {

                case COMMAND_GET_MEDIA_SESSION_TOKEN:
                    onGetMediaSession(message);
                    break;

                case COMMAND_GET_MEDIA_QUEUE_ITEMS:
                    onGetMediaQueueItems(message);
                    break;

                default:
                    Log.v(TAG, "Unhandled message: " + message);
            }
        }
    }


    private class PlayerQueueManagerCallback implements  PlayerQueueManager.Callback {

        @Override
        public void onQueueUpdated(CharSequence queueTitle, List<MediaSessionCompat.QueueItem> queue) {
            mSession.setQueue(queue);
            mSession.setQueueTitle(queueTitle);
        }

        @Override
        public void onCurrentIndexUpdated(int index) {
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            mSession.setMetadata(metadata);
        }
    }


    private class PlayerManagerCallback implements PlayerManager.Callback {

        @Override
        public void onStartPlay() {
            mSession.setActive(true);

            startService(PlayerService.newIntent(getApplicationContext()));
            mNotificationManager.startNotification();
        }

        @Override
        public void onStopPlay() {
            mSession.setActive(false);

            stopForeground(true);
        }

        @Override
        public void onPlayStateUpdated(PlaybackStateCompat state) {
            mSession.setPlaybackState(state);
        }
    }
}
