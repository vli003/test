package app.android.gifloaderviewmodel.controller;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.android.gifloaderviewmodel.model.Datum;
import app.android.gifloaderviewmodel.model.MyResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyViewModel extends ViewModel {

    public MutableLiveData<List<Datum>> datumList;
    private int limit = 20;
    private int page = 0;
    private String lastGifType;
    private String baseUrl = "https://api.giphy.com/v1/gifs/";
    private String requestExample = "http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC";
    private String key = "dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu";
    private String keyDemo = "dc6zaTOxFJmzC";

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

        if (gifType.equalsIgnoreCase(lastGifType)) return;
        page = 0;
        request(gifType, new OnDataLoaded() {
            @Override
            public void onDataLoaded(MyResponse response) {

                datumList.postValue(response.getData());
            }
        });
    }

    private void request(String gifType, OnDataLoaded onDataLoaded) {

        lastGifType = gifType;
        if (!gifType.equals("") & gifType.length() >= 2) {

            // Logs
/*            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor.Builder()
                            .loggable(BuildConfig.DEBUG)
                            .setLevel(Level.BODY)
                            .log(Platform.INFO)
                            .tag("MyRequests")
                            .build())
                    .build();*/

// java.lang.UnsupportedOperationException
/*            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter("api_key", key)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                }
            });*/

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            GiphyApi giphyApi = retrofit.create(GiphyApi.class);
            giphyApi.search(gifType, limit, page * limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<MyResponse>() {
                        @Override
                        public void onSuccess(MyResponse response) {
                            Log.e("TEST", "page " + page + ": " + response.toString());
                            onDataLoaded.onDataLoaded(response);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("TEST", "onError " + e);
                        }
                    });
        }
    }
}

