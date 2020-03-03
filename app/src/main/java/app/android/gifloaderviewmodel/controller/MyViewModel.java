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
    private int page = 0;
    private String lastGifType;


    public MyViewModel() {

        datumList = new MutableLiveData<>();
    }

    public void request(String gifType, String key, int limit, boolean isChange) {

        if (!gifType.equalsIgnoreCase(lastGifType) || !isChange) {

            lastGifType = gifType;

            if (!gifType.equals("") & gifType.length() >= 2) {

                Request request = new Request.Builder()
                        .url(httpUrl(gifType, key, limit))
                        .build();
                sendRequest(request, page, isChange);
            }
        }
    }

    public HttpUrl httpUrl(String gifType, String key, int limit) {

        HttpUrl httpUrl = new HttpUrl.Builder()
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
        return httpUrl;
    }

    public void sendRequest(Request request, int page, boolean isChange) {
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

                List<Datum> oldDatum = datumList.getValue();
                if (myResponse == null) {
                    return;
                }

                if (oldDatum == null || isChange) {
                    datumList.postValue(myResponse.getData());

                } else {
                    oldDatum.addAll(myResponse.getData());
                    datumList.postValue(oldDatum);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPage(boolean isIncrement) {
        if (isIncrement) {
            page += 1;
        } else page = 0;
    }
}

