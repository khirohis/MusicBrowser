package net.hogelab.musicbrowser.promise;

import android.util.Log;

/**
 * Created by kobayasi on 2017/02/20.
 */

public class Sandbox {
    private final String TAG = Sandbox.class.getSimpleName();

    public static void doMain() {
        new Sandbox().main();
    }


    public void main() {
        Log.d(TAG, "Create Future");
        final Future future = new Future() {
        }.done(result -> {
            Log.d(TAG, "done: " + result.toString());
        }).fail(result -> {
            Log.d(TAG, "fail: " + result.toString());
        });

        Log.d(TAG, "Create Runnable");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "on run");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                future.resolve("Kuma---!");
//                future.reject(new Exception("Maku---!"));
            }
        };

        Log.d(TAG, "Thread start");
        FutureExecutorService executorService = FutureExecutorService.getInstance();
        executorService.submit(runnable);

        try {
            Log.d(TAG, "Future waitComplete");
            future.waitComplete();
        } catch (InterruptedException e) {
        }
    }
}
