package app.android.gifloaderviewmodel.controller;

import android.os.Handler;
import android.os.HandlerThread;

public class MyWorkerThread extends HandlerThread {

    private Handler myWorkerHandler;

    public MyWorkerThread(String name) {
        super(name);
    }

    public void postTask(Runnable task) {
        myWorkerHandler.post(task);
    }

    public void prepareHandler() {
        myWorkerHandler = new Handler(getLooper());
    }
}
