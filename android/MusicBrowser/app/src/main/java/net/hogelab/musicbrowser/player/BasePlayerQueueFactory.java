package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

/**
 * Created by kobayasi on 2017/05/12.
 */

public abstract class BasePlayerQueueFactory implements PlayerQueueFactory {

    @Override
    public void createQueueFromMediaId(Context context, String mediaId, Bundle extras,
                                       final SuccessCallback successCallback, final FailureCallback failureCallback, Handler handler) {
        try {
            List<MediaSessionCompat.QueueItem> queue = createQueueFromMediaId(context, mediaId, extras);
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        successCallback.onSuccess(queue);
                    }
                });
            } else {
                successCallback.onSuccess(queue);
            }
            return;
        } catch (Exception e) {
        }

        if (failureCallback != null) {
            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        failureCallback.onFailure();
                    }
                });
            } else {
                failureCallback.onFailure();
            }
        }
    }


    protected abstract List<MediaSessionCompat.QueueItem> createQueueFromMediaId(Context context, String mediaId, Bundle extras);
}
