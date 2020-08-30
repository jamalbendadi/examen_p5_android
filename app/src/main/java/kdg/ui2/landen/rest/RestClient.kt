package kdg.ui2.landen.rest

import android.content.Context
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import io.reactivex.Observable
import kdg.ui2.landen.model.Country
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import com.google.gson.GsonBuilder
import kdg.ui2.landen.model.City
import java.io.InputStreamReader

const val BASE_URL = "http://10.0.2.2:3000"

@Suppress("DEPRECATION")
class RestClient(private val context: Context) {
    fun connect(url: String): HttpURLConnection {
        val connectionManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        if (networkInfo == null) println("-----------------netwerkinfo null-----------------")
        if (networkInfo != null) println("-----------------netwerkinfo niet null-----------------")
        if (networkInfo != null && networkInfo.isConnected) {
            val url = URL(url)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                connectTimeout = 5000
                readTimeout = 5000
                requestMethod = "GET"
                connect()
                //println("=================================succes===================================")
            }
            return connection
        }
        throw IOException("Unable to connect to network");
    }

    fun getCountries(): Observable<Array<Country>> {
        val observable = Observable.create<Array<Country>> { emitter ->
            try {
                var connection = connect("${BASE_URL}/countries")
                val countries = GsonBuilder()
                    .create()
                    .fromJson(InputStreamReader(connection.inputStream), Array<Country>::class.java)
                countries.forEach { c ->
                    connection = connect("${BASE_URL}/${c.image}")
                    c.flagBitmap = BitmapFactory.decodeStream(connection.inputStream)
                }
                emitter.onNext(countries)
            } catch (e: Exception) {
                println(e)
                emitter.onError(e)
            }

        }
        return observable
    }

    fun getCountry(id:Int): Observable<Country> {
        val observable = Observable.create<Country> { emitter ->
            try {
                var connection = connect("${BASE_URL}/countries/${id}")
                val country = GsonBuilder()
                    .create()
                    .fromJson(InputStreamReader(connection.inputStream), Country::class.java)
                connection = connect("${BASE_URL}/${country.image}")
                country.flagBitmap = BitmapFactory.decodeStream(connection.inputStream)
                emitter.onNext(country)
            } catch (e: Exception) {
                println(e)
                emitter.onError(e)
            }

        }
        return observable
    }
    fun getCities(countryId:Int) : Observable<Array<City>>{
        val observable = Observable.create<Array<City>>{emitter ->
            try{
                var connection = connect("${BASE_URL}/cities?countryId=${countryId}")
                val cities = GsonBuilder()
                    .create()
                    .fromJson(InputStreamReader(connection.inputStream),Array<City>::class.java)
                emitter.onNext(cities)
            }
            catch (e:Exception){
                emitter.onError(e)
            }
        }
        return observable
    }
}