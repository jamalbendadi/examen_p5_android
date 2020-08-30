package kdg.ui2.landen.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kdg.ui2.landen.R
import kdg.ui2.landen.adapters.CountryRecyclerViewAdapter
import kdg.ui2.landen.rest.RestClient
/**
 * A fragment representing a list of Items.
 */
class CountryFragment : Fragment() {

    private var columnCount = 1
    private lateinit var onSelectListener: CountryRecyclerViewAdapter.OnSelectionListener
    private lateinit var disposable: Disposable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_country_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = CountryRecyclerViewAdapter(context,onSelectListener)
                disposable = RestClient(context).getCountries()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe{
                        (adapter as CountryRecyclerViewAdapter).countries = it
                    }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            onSelectListener = context as CountryRecyclerViewAdapter.OnSelectionListener;
        }catch (e : ClassCastException){
            throw java.lang.ClassCastException(context.toString() + "must implement interface OnCountrySelectionListener")
        }

    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CountryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}