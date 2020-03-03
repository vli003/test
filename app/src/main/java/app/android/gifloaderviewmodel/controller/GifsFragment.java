package app.android.gifloaderviewmodel.controller;


import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.android.gifloaderviewmodel.R;
import app.android.gifloaderviewmodel.model.Datum;
import app.android.gifloaderviewmodel.model.MyResponse;
import app.android.gifloaderviewmodel.model.Size;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class GifsFragment extends Fragment {

    private EditText etGifType;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Timer timer;
    private final long DELAY = 200;
    private int limit = 20;
    private String keyDemo = "dc6zaTOxFJmzC";
    private String key = "dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu";

    private MyWorkerThread myWorkerThread;

    private MyViewModel model;

    public GifsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gifs, container, false);

        etGifType = v.findViewById(R.id.et_gif_type);
        recyclerView = v.findViewById(R.id.rv);

//        recyclerView.setHasFixedSize(true);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        model = ViewModelProviders.of(this).get(MyViewModel.class);
        model.datumList.observe(this, datumList -> {
            myAdapter.submitList(datumList);
        });

        myWorkerThread = new MyWorkerThread("myWorkerThread");
        /* Runnable task = () -> model.request(etGifType.getText().toString(), key, limit,true);
        myWorkerThread.start();
        myWorkerThread.prepareHandler();
        myWorkerThread.postTask(task);*/

        etGifType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 2) {
                    model.setPage(false);

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
//                            request(page);

                            model.request(s.toString(), key, limit, true);
                        }
                    }, DELAY);
                }
            }
        });
        return v;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<Datum> gifs = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;
            public ImageView imageView;

            public ViewHolder(View v) {

                super(v);

                textView = v.findViewById(R.id.tv_item_title);
                imageView = v.findViewById(R.id.iv_item);
            }
        }

        public void submitList(List<Datum> list) {

//            if (page == 0) {
            gifs = list;
//            } else gifs.addAll(list);
            notifyDataSetChanged();//update recycler view
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gif, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (position == gifs.size() - 1) {
                backgroundThreadToast(getContext(), "position " + position);
                model.setPage(true);

                if (myWorkerThread.isAlive()) myWorkerThread.interrupt();

                myWorkerThread = new MyWorkerThread("myWorkerThread");
                Runnable task = () -> model.request(etGifType.getText().toString(), key, limit, false);
                myWorkerThread.start();
                myWorkerThread.prepareHandler();
                myWorkerThread.postTask(task);
            }

            fillItemGif(holder, position);
            Log.e("TEST", /*"URL " + original.getUrl() + */"position " + position);
        }

        @Override
        public int getItemCount() {

            if (gifs != null) {
                return gifs.size();
            } else return 0;
        }

        public void fillItemGif(ViewHolder holder, final int position) {
            Datum datum = gifs.get(position);
            TextView tv = holder.textView;
            tv.setText(datum.getTitle());

            Size fixedHeightDownsampled = datum.getImages().getFixedHeightDownsampled();
            ImageView iv = holder.imageView;
            Glide.with(holder.itemView.getContext()).load(fixedHeightDownsampled.getUrl()).into(iv);
        }
    }

    public static void backgroundThreadToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
        }
    }

    public class MyWorkerThread extends HandlerThread {

        private Handler myWorkerHandler;

        public MyWorkerThread(String name) {
            super(name);
        }

        public void postTask(Runnable task) {
            myWorkerHandler.post(task);
        }

        public void prepareHandler() {
            myWorkerHandler = new Handler(getLooper());
        }
    }
}