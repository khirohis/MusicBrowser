package net.hogelab.musicbrowser.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kobayasi on 2017/02/20.
 */

public class FutureExecutorService {
    public static FutureExecutorService getInstance() {
        return FutureExecutorServiceInstanceHolder.instance;
    }


    protected ExecutorService executorService;


    public FutureExecutorService() {
        executorService = Executors.newCachedThreadPool();
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public MyPromise submit(Callable<Object> callable) {
        MyPromise promise = new MyPromise(callable);

        return submit(promise);
    }

    public MyFutureTask submit(MyFutureTask future) {
        executorService.submit(future);

        return future;
    }

    public MyPromise submit(MyPromise promise) {
        executorService.submit(promise);

        return promise;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private static class FutureExecutorServiceInstanceHolder {
        private static final FutureExecutorService instance = new FutureExecutorService();
    }
}
