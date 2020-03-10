package app.android.gifloaderviewmodel.controller;

import android.app.Application;
import android.widget.Toast;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RxJavaPlugins.setErrorHandler(throwable -> {
            throwable.printStackTrace();
            Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
        });
    }
}
