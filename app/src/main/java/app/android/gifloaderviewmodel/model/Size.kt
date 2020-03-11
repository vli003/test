package app.android.gifloaderviewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Size(
        @SerializedName("height")
        @Expose
        var height: String? = null,
        @SerializedName("url")
        @Expose
        var url: String? = null,
        @SerializedName("width")
        @Expose
        var width: String? = null
)