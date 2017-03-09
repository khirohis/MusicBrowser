package net.hogelab.musicbrowser.event;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Created by kobayasi on 2016/04/11.
 */
public class EventBus {

    public static Bus getBus() {
        return BusInstanceHolder.instance;
    }

    public static Handler getMainLooperHandler() {
        return HandlerInstanceHolder.instance;
    }


    public static void postMainLooper(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            getBus().post(event);
        } else {
            getMainLooperHandler().post(() -> {
                getBus().post(event);
            });
        }
    }


    private static class BusInstanceHolder {
        private static final Bus instance = new Bus();
    }

    private static class HandlerInstanceHolder {
        private static final Handler instance = new Handler(Looper.getMainLooper());
    }
}
