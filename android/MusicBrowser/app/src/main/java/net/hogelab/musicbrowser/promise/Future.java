package net.hogelab.musicbrowser.promise;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by kobayasi on 2017/02/20.
 */

public abstract class Future {
    public static interface OnDoneCallback {
        public void onDone(final Object result);
    }

    public static interface OnFailCallback {
        public void onFail(final Exception result);
    }


    public enum State {
        PENDING, RESOLVED, REJECTED,
    }


    protected State state = State.PENDING;
    protected Object resolveValue;
    protected Exception rejectValue;

    protected final List<OnDoneCallback> onDoneCallbacks = new CopyOnWriteArrayList<OnDoneCallback>();
    protected final List<OnFailCallback> onFailCallbacks = new CopyOnWriteArrayList<OnFailCallback>();


    public Future future() {
        return this;
    }

    public boolean isPending() {
        return state == State.PENDING;
    }

    public boolean isResolved() {
        return state == State.RESOLVED;
    }

    public boolean isRejected() {
        return state == State.REJECTED;
    }

    public synchronized Object getResult() {
        if (!isResolved()) {
            throw new IllegalStateException("getResult: not resolved");
        }

        return resolveValue;
    }


    public synchronized Future resolve(Object value) {
        if (!isPending()) {
            throw new IllegalStateException("resolved: not pending");
        }

        resolveValue = value;
        state = State.RESOLVED;

        callOnDoneCallbacks(resolveValue);

        notifyAll();

        return this;
    }

    public synchronized Future reject(Exception value) {
        if (!isPending()) {
            throw new IllegalStateException("rejected: not pending");
        }

        rejectValue = value;
        state = State.REJECTED;

        callOnFailCallbacks(rejectValue);

        notifyAll();

        return this;
    }


    public synchronized Future done(OnDoneCallback callback) {
        if (isResolved()) {
            callback.onDone(resolveValue);
        } else {
            onDoneCallbacks.add(callback);
        }

        return this;
    }

    public synchronized Future fail(OnFailCallback callback) {
        if (isRejected()) {
            callback.onFail(rejectValue);
        } else {
            onFailCallbacks.add(callback);
        }

        return this;
    }

    public synchronized Future then(OnDoneCallback callback) {
        done(callback);

        return this;
    }


    public synchronized void waitComplete() throws InterruptedException {
        wait();
    }


    protected void callOnDoneCallbacks(Object result) {
        for (OnDoneCallback callback : onDoneCallbacks) {
            try {
                callback.onDone(result);
            } catch (Exception e) {
            }
        }

        onDoneCallbacks.clear();
    }

    protected void callOnFailCallbacks(Exception result) {
        for (OnFailCallback callback : onFailCallbacks) {
            try {
                callback.onFail(result);
            } catch (Exception e) {
            }
        }

        onFailCallbacks.clear();
    }
}
