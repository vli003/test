package app.android.gifloaderviewmodel.controller

import app.android.gifloaderviewmodel.BuildConfig
import app.android.gifloaderviewmodel.model.MyResponse
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform.Companion.INFO
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GifsRepository {
    private var retrofit: Retrofit? = null
    private val giphyApi: GiphyApi
    private val baseUrl = "https://api.giphy.com/v1/gifs/"
    private val limit = 20
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
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) { // java.lang.UnsupportedOperationException
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
                val client = OkHttpClient.Builder()
                        .addInterceptor(LoggingInterceptor.Builder()
                                .loggable(BuildConfig.DEBUG)
                                .setLevel(Level.BODY)
                                .log(INFO)
                                .tag("MyRequests")
                                .build())
                        .build()
                retrofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }
            return retrofit
        }

    fun getGifs(gifType: String?, page: Int): Single<MyResponse?>? {
        return giphyApi.search(gifType, limit, page * limit)
    }

    init {
        giphyApi = retrofitInstance!!.create(GiphyApi::class.java)
    }
}