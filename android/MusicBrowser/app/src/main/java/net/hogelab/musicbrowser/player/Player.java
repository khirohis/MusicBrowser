package net.hogelab.musicbrowser.player;

import android.support.v4.media.session.MediaSessionCompat;

/**
 * Created by kobayasi on 2017/04/25.
 */

public interface Player {

    void setPlayerCallback(Callback callback);

    void start();
    void stop();

    void play(MediaSessionCompat.QueueItem item);

    public interface Callback {
        void onCompletion();
        void onStatusChanged(int state);
        void onError(String error);
    }
}
