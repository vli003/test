package app.android.gifloaderviewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MyResponse (
    @SerializedName("data")
    @Expose
    var data: List<Datum>
)