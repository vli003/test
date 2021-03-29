package app.android.gifloaderviewmodel.controller

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.android.gifloaderviewmodel.R
import app.android.gifloaderviewmodel.model.Datum
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_gif.view.*
import java.util.*

class MyAdapter(private val onUpdatePage: OnUpdatePage) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var gifs: List<Datum>? = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_gif, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == gifs!!.size - 1) {
            onUpdatePage.updatePage()
        }
        holder.bind(gifs!![position])
        Log.e("TEST", "position $position")
    }

    override fun getItemCount(): Int {
        return if (gifs != null) {
            gifs!!.size
        } else 0
    }

    fun submitList(list: List<Datum>?) {
        gifs = list
        notifyDataSetChanged() // Notify any registered observers that the data set has changed
    }

    /*    public static void backgroundThreadToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }*/
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var textView: TextView = v.tv_item_title
        var imageView: ImageView = v.iv_item
        fun bind(datum: Datum) {
            val tv = textView
            tv.text = datum.title
            val fixedHeightDownsampled = datum.images?.fixedHeightDownsampled
            val iv = imageView
            Glide.with(itemView.context).load(fixedHeightDownsampled?.url).into(iv)
        }
    }
}