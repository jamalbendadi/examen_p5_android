package kdg.ui2.landen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kdg.ui2.landen.model.City
import kdg.ui2.landen.rest.RestClient


class CitiesActivity : AppCompatActivity() {
    private var countryId = 0
    private lateinit var listView : ListView
    private lateinit var disposable: Disposable
    private var cities:Array<City> = arrayOf()
    set(cities) {
        field = cities
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        countryId = intent.getIntExtra(COUNTRY_ID,countryId)
        listView = findViewById(R.id.cities_listview)
        disposable = RestClient(this).getCities(countryId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).subscribe{
                //var arrayList = it
                var arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,it)
                listView.adapter = arrayAdapter
        }


    }
    private fun initialize(){

    }
}