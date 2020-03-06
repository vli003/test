package app.android.gifloaderviewmodel.controller;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import app.android.gifloaderviewmodel.BuildConfig;
import app.android.gifloaderviewmodel.model.MyResponse;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GifsRepository {

    private Retrofit retrofit;
    private GiphyApi giphyApi;
    private final String baseUrl = "https://api.giphy.com/v1/gifs/";
    private final int limit = 20;

    public GifsRepository() {

        giphyApi = getRetrofitInstance().create(GiphyApi.class);
    }

    public Retrofit getRetrofitInstance() {

        if (retrofit == null) {
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

            // Logs
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor.Builder()
                            .loggable(BuildConfig.DEBUG)
                            .setLevel(Level.BODY)
                            .log(Platform.INFO)
                            .tag("MyRequests")
                            .build())
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public Single<MyResponse> getGifs(String gifType, int page) {

        return giphyApi.search(gifType, limit, page * limit);
    }
}
