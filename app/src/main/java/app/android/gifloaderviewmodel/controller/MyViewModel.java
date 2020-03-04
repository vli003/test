package app.android.gifloaderviewmodel.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import app.android.gifloaderviewmodel.model.Datum;
import app.android.gifloaderviewmodel.model.MyResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyViewModel extends ViewModel {

    public MutableLiveData<List<Datum>> datumList;
    private String keyDemo = "dc6zaTOxFJmzC";
    private String key = "dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu";
    private int limit = 20;
    private int page = 0;
    private String lastGifType;
    private MyWorkerThread myWorkerThread;

    public MyViewModel() {

        datumList = new MutableLiveData<>();
    }

    public void updatePage(String gifType) {

        page += 1;
        request(gifType, new OnDataLoaded() {
            @Override
            public void onDataLoaded(MyResponse response) {

                List<Datum> oldDatum = datumList.getValue();
                if (oldDatum != null) {
                    oldDatum.addAll(response.getData());
                }
                datumList.postValue(oldDatum);
            }
        });
    }

    public void getGifs(String gifType) {

        if (gifType.equalsIgnoreCase(lastGifType)) {
            return;
        }
        page = 0;
        request(gifType, new OnDataLoaded() {
            @Override
            public void onDataLoaded(MyResponse response) {

                datumList.postValue(response.getData());
            }
        });
    }

    private void request(String gifType, OnDataLoaded onDataLoaded) {

        Runnable task = () -> {
//            request(gifType, onDataLoaded);

            lastGifType = gifType;

            if (!gifType.equals("") & gifType.length() >= 2) {

                Request request = new Request.Builder()
                        .url(httpUrl(gifType))
                        .build();
                sendRequest(request, onDataLoaded);
            }
        };

        interruptThread(myWorkerThread);
        myWorkerThread = new MyWorkerThread("myWorkerThread");
        myWorkerThread.start();
        myWorkerThread.prepareHandler();
        myWorkerThread.postTask(task);
    }

    public HttpUrl httpUrl(String gifType) {

        return new HttpUrl.Builder()
                .scheme("https")
                .host("api.giphy.com")
                .addPathSegment("v1")
                .addPathSegment("gifs")
                .addPathSegment("search")
                .addQueryParameter("q", gifType)
                .addQueryParameter("api_key", key)
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("offset", String.valueOf(page * limit))
                .build();
    }

    public void sendRequest(Request request, OnDataLoaded onDataLoaded) {
        OkHttpClient client = new OkHttpClient();
        try {
            final Response response = client.newCall(request).execute();

            Log.e("TEST ", "page " + page + " " + response.toString());

            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> {
                MyResponse myResponse = null;
                try {
                    myResponse = new Gson().fromJson(response.body().string(), MyResponse.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onDataLoaded.onDataLoaded(myResponse);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void interruptThread(Thread thread) {
        if (thread != null) {
            if (thread.isAlive()) thread.interrupt();
        }
    }
}

