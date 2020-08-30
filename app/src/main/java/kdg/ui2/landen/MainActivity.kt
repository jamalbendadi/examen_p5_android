package kdg.ui2.landen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable
import kdg.ui2.landen.adapters.CountryRecyclerViewAdapter
import kdg.ui2.landen.rest.RestClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

const val SAVED_INDEX = "SAVED_INDEX"
const val SHARED_PREFS = "sharedPrefs"
const val COUNTRY_ID = "COUNTRY_ID"
class MainActivity : AppCompatActivity(), CountryRecyclerViewAdapter.OnSelectionListener {
    private lateinit var lblPopInM: TextView
    private lateinit var lblIndep: TextView
    private lateinit var valPopInM: TextView
    private lateinit var valIndep: TextView
    private lateinit var valCountryName: TextView
    private lateinit var valFullname: TextView
    private lateinit var imgFlag: ImageView
    private lateinit var btnCities:Button
    private lateinit var disposable: Disposable
    private var currentIndex: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialize()
        orientation()

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(SAVED_INDEX)
            if (currentIndex != -1) {
                loadCountry(currentIndex)
            }
        } else {
            val sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE) ?: return
            currentIndex = sharedPref.getInt(SHARED_PREFS, -1)
            if (currentIndex != -1) {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    loadCountry(currentIndex)
                }
            }
        }
    }

    private fun initialize() {
        valCountryName = findViewById(R.id.txtValCountryName)
        valFullname = findViewById(R.id.txtValFullname)
        lblPopInM = findViewById(R.id.txtViewPop)
        lblIndep = findViewById(R.id.txtViewDate)
        valPopInM = findViewById(R.id.txtValpop)
        valIndep = findViewById(R.id.txtValDate)
        imgFlag = findViewById(R.id.flagImg)
        btnCities = findViewById(R.id.btn_view_cities)
    }

    private fun orientation() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            supportFragmentManager.beginTransaction()
                .show(supportFragmentManager.findFragmentById(R.id.fragment)!!)
                .hide(supportFragmentManager.findFragmentById(R.id.fragment2)!!)
                .commit()
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportFragmentManager.beginTransaction()
                .show(supportFragmentManager.findFragmentById(R.id.fragment)!!)
                .show(supportFragmentManager.findFragmentById(R.id.fragment2)!!)
                .commit()
        }
    }

    fun writeSharedPrefs(index: Int) {
        getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            .edit().apply {
                putInt(SHARED_PREFS, index)
            }.apply()
    }

    override fun onSelected(position: Int) {
        var index = position + 1
        println(Integer.toString(position))
        loadCountry(index)
        currentIndex = index
    }

    private fun loadCountry(index: Int) {
        showDetails(index)
        disposable = RestClient(this).getCountry(index)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe {
                valCountryName.text = it.name
                lblIndep.text = getString(R.string.date)
                lblPopInM.text = getString(R.string.popHere)
                valIndep.text = it.dateOfIndependence
                valPopInM.text = Integer.toString(it.populationInMillions)
                valFullname.text = it.fullname
                imgFlag.setImageBitmap(it.flagBitmap)
                btnCities.setOnClickListener{
                    val newIntent = Intent(this,CitiesActivity::class.java)
                    newIntent.putExtra(COUNTRY_ID,index)
                    startActivity(newIntent)
                }
            }
        writeSharedPrefs(index)
    }

    private fun showDetails(countryId: Int) {
        if (findViewById<LinearLayout>(R.id.main_activity) != null) {
            val manager = supportFragmentManager
            manager.findFragmentById(R.id.fragment2)?.arguments?.putInt("param1", countryId)
            manager.beginTransaction()
                .hide(manager.findFragmentById(R.id.fragment)!!)
                .show(manager.findFragmentById(R.id.fragment2)!!)
                .addToBackStack(null)
                .commit()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVED_INDEX, currentIndex)
    }


}