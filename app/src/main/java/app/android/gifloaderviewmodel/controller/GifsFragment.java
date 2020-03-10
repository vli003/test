package app.android.gifloaderviewmodel.controller;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Timer;
import java.util.TimerTask;

import app.android.gifloaderviewmodel.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GifsFragment extends Fragment {

    private EditText etGifType;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private Timer timer;
    private final long DELAY = 200;
    private MyViewModel myViewModel;

    public GifsFragment() {
        // Required empty public constructor
    }

/*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // alternative ViewModel initialization
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.datumList.observe(this, datumList -> {
            myAdapter.submitList(datumList);
        });
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gifs, container, false);

        etGifType = v.findViewById(R.id.et_gif_type);
        recyclerView = v.findViewById(R.id.rv);

        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.datumList.observe(this, datumList -> {
            myAdapter.submitList(datumList);
        });

        myAdapter = new MyAdapter(new OnUpdatePage() {
            @Override
            public void updatePage() {
                makeToast("loaded new page");
                myViewModel.updatePage(etGifType.getText().toString());
            }
        });
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myViewModel.replacePage(s.toString());
                        }
                    }, DELAY);
                } else makeToast("too short request");
            }
        });
        return v;
    }

    public void makeToast(String txt) {
        Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
    }
}