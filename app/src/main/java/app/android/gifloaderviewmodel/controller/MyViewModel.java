package app.android.gifloaderviewmodel.controller;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import app.android.gifloaderviewmodel.model.Datum;

public class MyViewModel extends ViewModel {

    public MutableLiveData<List<Datum>> datumList;


    public MyViewModel() {
//            super(application);
        datumList = new MutableLiveData<>();
    }
}

