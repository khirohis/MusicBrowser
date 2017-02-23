package net.hogelab.musicbrowser.promise;

import java.util.concurrent.Callable;

/**
 * Created by kobayasi on 2017/02/22.
 */

public class MyPromise extends MyFutureTask {

    public interface OnDoneCallback {
        void onDone(final Object result);
    }

    public interface OnFailCallback {
        void onFail();
    }

    public interface OnDonePipe {
        Callable<Object> onDone(final Object result);
    }


    enum State {
        PENDING, RESOLVED, REJECTED,
    };


    protected State state = State.PENDING;

    protected OnDoneCallback onDoneCallback;
    protected OnFailCallback onFailCallback;

    protected OnDonePipe onDonePipe;
    protected MyPromise onDonePromise;


    public MyPromise() {
    }

    public MyPromise(Callable<Object> task) {
        super(task);
    }


    public synchronized boolean isPending() {
        return state == State.PENDING;
    }

    public synchronized boolean isResolved() {
        return state == State.RESOLVED;
    }

    public boolean isRejected() {
        return state == State.REJECTED;
    }


    @Override
    public void run() {
        if (task != null) {
            try {
                Object result = task.call();
                resolve(result);

                return;
            } catch (Exception e) {
            }
        }

        reject();
    }


    public synchronized void resolve(Object result) throws IllegalStateException {
        if (!isPending()) {
            throw new IllegalStateException("resolve: not pending");
        }

        state = State.RESOLVED;
        complete();

        onResolved();
    }

    public synchronized void reject() throws IllegalStateException {
        if (!isPending()) {
            throw new IllegalStateException("reject: not pending");
        }

        state = State.REJECTED;
        complete();

        onRejected();
    }


    public synchronized MyPromise done(OnDoneCallback callback) {
        if (isResolved()) {
            callback.onDone(result);
        } else {
            onDoneCallback = callback;
        }

        return this;
    }

    public synchronized MyPromise fail(OnFailCallback callback) {
        if (isRejected()) {
            callback.onFail();
        } else {
            onFailCallback = callback;
        }

        return this;
    }

    public synchronized MyPromise pipe(OnDonePipe pipe) {
        if (onDoneCallback != null || onFailCallback != null) {
            throw new IllegalStateException("pipe: has callback");
        }


        onDonePipe = pipe;
        onDonePromise = new MyPromise();

        if (isResolved()) {
            onDonePromise.task = pipe.onDone(result);
            onDonePromise.run();
        } else if (isRejected()){
            onDonePromise.reject();
        }

        return onDonePromise;
    }


    protected synchronized void onResolved() {
        if (onDoneCallback != null) {
            onDoneCallback.onDone(result);
        } else if (onDonePromise != null) {
            onDonePromise.task = onDonePipe.onDone(result);
            onDonePromise.run();
        }
    }

    protected synchronized void onRejected() {
        if (onFailCallback != null) {
            onFailCallback.onFail();
        } else if (onDonePromise != null) {
            onDonePromise.reject();
        }
    }
}
