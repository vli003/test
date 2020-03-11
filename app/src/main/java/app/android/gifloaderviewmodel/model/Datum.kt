package app.android.gifloaderviewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Datum(
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("images")
        @Expose
        var images: Images? = null
)