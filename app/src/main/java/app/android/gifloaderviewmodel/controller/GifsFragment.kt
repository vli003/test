package app.android.gifloaderviewmodel.controller

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import app.android.gifloaderviewmodel.R
import app.android.gifloaderviewmodel.model.Datum
import kotlinx.android.synthetic.main.fragment_gifs.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class GifsFragment : Fragment(R.layout.fragment_gifs) {

    private var myAdapter: MyAdapter? = null
    private var timer: Timer? = null
    private val delay: Long = 200
    private lateinit var myViewModel: MyViewModel
    /*    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // alternative ViewModel initialization
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        myViewModel.datumList.observe(this, datumList -> {
            myAdapter.submitList(datumList);
        });
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv.adapter

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        myViewModel.datumList.observe(viewLifecycleOwner, Observer { datumList: List<Datum>? -> myAdapter?.submitList(datumList) })
        myAdapter = MyAdapter(object : OnUpdatePage {
            override fun updatePage() {
                makeToast("loaded new page")
                myViewModel.updatePage(et_gif_type?.text?.toString())
            }
        })
        rv.adapter = myAdapter
        rv.layoutManager = LinearLayoutManager(context)
        et_gif_type.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length >= 2) {
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            myViewModel.replacePage(s.toString())
                        }
                    }, delay)
                } else makeToast("too short request")
            }
        })
    }

    fun makeToast(txt: String?) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show()
    }
}