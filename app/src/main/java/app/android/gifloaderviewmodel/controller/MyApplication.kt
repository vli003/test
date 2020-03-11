package app.android.gifloaderviewmodel.controller

import android.app.Application
import android.widget.Toast
import io.reactivex.plugins.RxJavaPlugins

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler(fun(throwable: Throwable) {
            throwable.printStackTrace()
            Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
        })
    }
}