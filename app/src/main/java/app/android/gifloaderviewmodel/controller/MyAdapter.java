package app.android.gifloaderviewmodel.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.android.gifloaderviewmodel.R;
import app.android.gifloaderviewmodel.model.Datum;
import app.android.gifloaderviewmodel.model.Size;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Datum> gifs = new ArrayList<>();
    private OnUpdatePage onUpdatePage;

    public MyAdapter(OnUpdatePage onUpdatePage) {
        this.onUpdatePage = onUpdatePage;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gif, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        if (position == gifs.size() - 1) {
//           backgroundThreadToast(, "position " + position);

            onUpdatePage.updatePage();
        }

        holder.bind(gifs.get(position));
//        fillItemGif(holder, position);
        Log.e("TEST", "position " + position);
    }

    @Override
    public int getItemCount() {

        if (gifs != null) {
            return gifs.size();
        } else return 0;
    }

    public void submitList(List<Datum> list) {

        gifs = list;
        notifyDataSetChanged();// Notify any registered observers that the data set has changed
    }

/*    public void fillItemGif(ViewHolder holder, final int position) {
        Datum datum = gifs.get(position);
        TextView tv = holder.textView;
        tv.setText(datum.getTitle());

        Size fixedHeightDownsampled = datum.getImages().getFixedHeightDownsampled();
        ImageView iv = holder.imageView;
        Glide.with(holder.itemView.getContext()).load(fixedHeightDownsampled.getUrl()).into(iv);
    }*/

/*    public static void backgroundThreadToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }*/

        class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public ViewHolder(View v) {

            super(v);

            textView = v.findViewById(R.id.tv_item_title);
            imageView = v.findViewById(R.id.iv_item);
        }

        void bind(Datum datum) {

            TextView tv = textView;
            tv.setText(datum.getTitle());

            Size fixedHeightDownsampled = datum.getImages().getFixedHeightDownsampled();
            ImageView iv = imageView;
            Glide.with(itemView.getContext()).load(fixedHeightDownsampled.getUrl()).into(iv);
        }
    }
}
