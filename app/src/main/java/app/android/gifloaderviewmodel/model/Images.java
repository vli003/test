package app.android.gifloaderviewmodel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("fixed_height_downsampled")
    @Expose
    private Size fixedHeightDownsampled;

    public Size getFixedHeightDownsampled() {
        return fixedHeightDownsampled;
    }

    public void setFixedHeightDownsampled(Size fixedHeightDownsampled) {
        this.fixedHeightDownsampled = fixedHeightDownsampled;
    }
}
