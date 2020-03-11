package app.android.gifloaderviewmodel.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Images(
        @SerializedName("fixed_height_downsampled")
        @Expose
        var fixedHeightDownsampled: Size? = null
)
