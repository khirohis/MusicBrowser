package net.hogelab.musicbrowser.promise;

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

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }


    private static class FutureExecutorServiceInstanceHolder {
        private static final FutureExecutorService instance = new FutureExecutorService();
    }
}
