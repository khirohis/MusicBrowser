package net.hogelab.musicbrowser.promise;

import java.util.concurrent.Callable;

/**
 * Created by kobayasi on 2017/02/23.
 */

public class MyFutureTask extends MyFuture implements Runnable {

    protected Callable<Object> task;


    public MyFutureTask() {
    }

    public MyFutureTask(Callable<Object> task) {
        this.task = task;
    }


    public Callable<Object> getTask() {
        return task;
    }

    @Override
    public void run() {
        if (task != null) {
            try {
                Object result = task.call();
                setResult(result);
            } catch (Exception e) {
                complete();
            }
        }
    }
}
