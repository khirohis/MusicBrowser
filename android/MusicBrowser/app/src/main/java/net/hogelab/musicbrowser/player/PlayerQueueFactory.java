package net.hogelab.musicbrowser.player;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

/**
 * Created by kobayasi on 2017/05/12.
 */

public interface PlayerQueueFactory {

    void createQueueFromMediaId(Context context, String mediaId, Bundle extras,
                                SuccessCallback successCallback, FailureCallback failureCallback, Handler handler);

    interface SuccessCallback {
        void onSuccess(List<MediaSessionCompat.QueueItem> queue);
    }

    interface FailureCallback {
        // TODO: exception argument?
        void onFailure();
    }
}
