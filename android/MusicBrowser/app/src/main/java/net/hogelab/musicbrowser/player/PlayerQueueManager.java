package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobayasi on 2017/04/25.
 */

public class PlayerQueueManager {
    private static final String TAG = PlayerQueueManager.class.getSimpleName();

    private final Context mApplicationContext;
    private final Callback mCallback;

    // TODO: 同期
    private List<MediaSessionCompat.QueueItem> mMasterQueue;
    private int mMasterIndex;

    // TODO: 同期
    private List<Integer> mPlayingQueueIndex;
    private int mPlayingIndex;


    public PlayerQueueManager(Context applicationContext, Callback callback) {
        mApplicationContext = applicationContext;
        mCallback = callback;
    }


    public List<MediaSessionCompat.QueueItem> getMasterQueue() {
        return mMasterQueue;
    }


    public void setupMasterQueue(MediaSessionCompat.QueueItem item) {
        mMasterQueue = new ArrayList<>();
        mMasterQueue.add(item);
        mMasterIndex = 0;

        mPlayingQueueIndex = new ArrayList<>();
        mPlayingQueueIndex.add(0);
        mPlayingIndex = 0;

        mCallback.onQueueUpdated(item.getDescription().getTitle(), mMasterQueue);
        mCallback.onCurrentIndexUpdated(mMasterIndex);
    }


    public MediaSessionCompat.QueueItem getCurrentQueueItem() {
        return mMasterQueue.get(mPlayingQueueIndex.get(mPlayingIndex));
    }

    public int getCurrentIndex() {
        return mMasterIndex;
    }

    public void setCurrentIndex(int index) {
        // TODO: indexチェック
        mMasterIndex = index;

        int size = mPlayingQueueIndex.size();
        for (int i = 0; i < size; i++) {
            if (mPlayingQueueIndex.get(i) == index) {
                mPlayingIndex = i;
                break;
            }
        }
    }

    public void setNext() {
        setCurrentIndex(mMasterIndex + 1);
    }

    public interface Callback {
        void onQueueUpdated(CharSequence queueTitle, List<MediaSessionCompat.QueueItem> queue);
        void onCurrentIndexUpdated(int index);
        void onMetadataChanged(MediaMetadataCompat metadata);
    }
}
