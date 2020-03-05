package app.android.gifloaderviewmodel.controller;

import app.android.gifloaderviewmodel.model.MyResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GiphyApi {

    @GET("search?api_key=dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu")
    Single<MyResponse> search(@Query("q") String type, @Query("limit") int limit, @Query("offset") int offset);
}
