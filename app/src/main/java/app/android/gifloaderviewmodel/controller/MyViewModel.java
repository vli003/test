package app.android.gifloaderviewmodel.controller;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.android.gifloaderviewmodel.model.Datum;
import app.android.gifloaderviewmodel.model.MyResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends ViewModel {

    public MutableLiveData<List<Datum>> datumList;
    private int page = 0;
    private String lastGifType;
    private String requestExample = "http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC";
    private String key = "dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu";
    private String keyDemo = "dc6zaTOxFJmzC";
    private GifsRepository gifsRepository = new GifsRepository();

    public MyViewModel() {

        datumList = new MutableLiveData<>();
    }

    public void updatePage(String gifType) {

        page += 1;
        getGifs(gifType, true);
    }

    public void replacePage(String gifType) {

        if (gifType.equalsIgnoreCase(lastGifType)) return;
        lastGifType = gifType;
        page = 0;
        getGifs(gifType, false);
    }

    public void getGifs(String gifType, boolean isUpdatePage) {

        gifsRepository.getGifs(gifType, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(MyResponse response) {
                        Log.e("TEST", "page " + page + ": " + response.toString());
                        if (isUpdatePage) {
                            List<Datum> oldDatum = datumList.getValue();
                            if (oldDatum != null) {
                                oldDatum.addAll(response.getData());
                            }
                            datumList.postValue(oldDatum);
                        } else datumList.postValue(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TEST", "onError " + e);
                    }
                });
    }
}

