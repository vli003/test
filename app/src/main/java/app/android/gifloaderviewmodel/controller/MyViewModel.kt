package app.android.gifloaderviewmodel.controller

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.android.gifloaderviewmodel.model.Datum
import app.android.gifloaderviewmodel.model.MyResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class MyViewModel : ViewModel() {
    var datumList: MutableLiveData<ArrayList<Datum>?> = MutableLiveData()
    private var page = 0
    private var lastGifType: String? = null
    private val requestExample = "http://api.giphy.com/v1/gifs/search?q=funny+cat&api_key=dc6zaTOxFJmzC"
    private val key = "dvVxO5Yhd3nlfzSzNDbzvo1GZIQsSRLu"
    private val keyDemo = "dc6zaTOxFJmzC"
    private val gifsRepository = GifsRepository()
    private var disposable: Disposable? = null
    fun updatePage(gifType: String?) {
        page += 1
        getGifs(gifType, true)
    }

    fun replacePage(gifType: String) {
        if (gifType.equals(lastGifType, ignoreCase = true)) return
        lastGifType = gifType
        page = 0
        getGifs(gifType, false)
    }

    fun getGifs(gifType: String?, isUpdatePage: Boolean) {
        if (disposable != null) {
            disposable!!.dispose() // close disposable
        }
        disposable = gifsRepository.getGifs(gifType, page)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ response ->
                    Log.e("TEST", "page $page: $response")
                    if (isUpdatePage) {
                        val oldDatum = datumList.value
//                        if (response != null)
                        response?.let {
                            oldDatum?.addAll(it.data)
                            datumList.postValue(oldDatum)
                        }
                    } else response?.let { datumList.value = ArrayList(it.data) }


                }, { throwable -> Log.e("TEST", "onError $throwable") })
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable?.dispose() // close disposable
        }
    } /*    public void getGifs(String gifType, boolean isUpdatePage) {

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
    }*/

}