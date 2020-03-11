package app.android.gifloaderviewmodel.controller

import app.android.gifloaderviewmodel.model.MyResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApi {
    @GET("search?api_key=dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu")
    fun search(@Query("q") type: String?, @Query("limit") limit: Int, @Query("offset") offset: Int): Single<MyResponse?>?
}