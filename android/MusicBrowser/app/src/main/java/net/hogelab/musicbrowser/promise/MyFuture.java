package net.hogelab.musicbrowser.promise;

import java.util.concurrent.CountDownLatch;

/**
 * Created by kobayasi on 2017/02/23.
 */

public class MyFuture {
    boolean completed = false;
    protected Object result;
    protected CountDownLatch latch = new CountDownLatch(1);


    public Object getResult() throws InterruptedException, IllegalStateException {
        waitComplete();

        if (!completed) {
            throw new IllegalStateException("getResult: not resolved");
        }

        return result;
    }

    public synchronized void setResult(Object result) throws IllegalStateException {
        if (completed) {
            throw new IllegalStateException("resolve: not pending");
        }

        this.result = result;

        complete();
    }


    public void waitComplete() throws InterruptedException {
        latch.await();
    }


    protected void complete() {
        completed = true;
        latch.countDown();
    }
}
