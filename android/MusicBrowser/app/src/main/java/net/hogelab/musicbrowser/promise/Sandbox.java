package net.hogelab.musicbrowser.promise;

import android.util.Log;

import java.util.concurrent.Callable;

/**
 * Created by kobayasi on 2017/02/20.
 */

public class Sandbox {
    private final String TAG = Sandbox.class.getSimpleName();

    public static void doMain() {
        new Sandbox().main();
    }


    public void main() {
        promiseTest();
    }

    private void futureTest() {
        final MyFutureTask task = new MyFutureTask(() -> {
            Log.d(TAG, "on task");
            return "test";
        });
        FutureExecutorService executorService = FutureExecutorService.getInstance();
        executorService.submit(task);

        try {
            task.waitComplete();
        } catch (InterruptedException e) {
        }

        executorService.shutdown();
    }

    private void promiseTest() {
        Log.d(TAG, "Create MyPromise");
        final MyPromise promise = new MyPromise(
                () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }

                    return "ある日";
                });
        promise.pipe((result) -> {
            return () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }

                return result.toString() + " 森の中";
            };
        }).done((result) -> {
            Log.d(TAG, "done: " + result.toString());
        }).fail(() -> {
            Log.d(TAG, "fail");
        });

        FutureExecutorService executorService = FutureExecutorService.getInstance();
        Log.d(TAG, "submit promise");
        executorService.submit(promise);

        try {
            Log.d(TAG, "MyPromise waitComplete");
            promise.waitComplete();
        } catch (InterruptedException e) {
        }

        executorService.shutdown();
    }
}
