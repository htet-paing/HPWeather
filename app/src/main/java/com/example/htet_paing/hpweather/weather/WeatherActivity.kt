package com.example.htet_paing.hpweather.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.htet_paing.hpweather.R
import com.example.htet_paing.hpweather.model.WeatherResponse
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.DecimalFormat

class WeatherActivity : AppCompatActivity() {

    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var weatherResponse: WeatherResponse? = null

    val retrofit = Retrofit.Builder()
        .baseUrl("http://api.openweathermap.org/data/2.5/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val  weatherService = retrofit.create(WeatherService::class.java)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestForLocationPermission()
        setSupportActionBar(toolbar)
            setTitle("Weather")

        showLoading()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_change_city){

            val alert = AlertDialog.Builder(this)
            var et_changeCity: EditText? = null

            with(alert){
                setTitle("Search for City")
                et_changeCity = EditText(context)


                setPositiveButton("OK"){
                    dialog, which ->
                    dialog.dismiss()

                  var changeCity  = et_changeCity!!.text.toString()
                    Log.i("ChangeCityText","${changeCity}")
                    showLoading()

                    if (changeCity != null) {
                        weatherService.getWeatherByCityName(changeCity, "a7bd521bfef4caf29b1e5f820049a930", "metric").enqueue(object : Callback<WeatherResponse>{
                            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                                t.printStackTrace()
                                showError()

                            }

                            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {

                                if (response.isSuccessful){
                                    weatherResponse = response.body()
                                    showWeatherInfo()
                                }else{
                                    showError()
                                }
                            }

                        })
                    }
                }

                setNegativeButton("Cancel"){
                    dialog, which ->
                    dialog.dismiss()
                }
            }
            val dialog = alert.create()
            dialog.setView(et_changeCity)
            dialog.show()




        }else if (item?.itemId == R.id.menu_reload){
            Toast.makeText(this,"Refresh app",Toast.LENGTH_SHORT).show()
        }else if (item?.itemId == R.id.menu_setting){
            Toast.makeText(this,"Setting",Toast.LENGTH_SHORT).show()
        }else if (item?.itemId == R.id.menu_about){
            Toast.makeText(this,"About app",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun requestForLocationPermission(){
        //TODO : Request to access Location
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    getLocation()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    //Do nothing
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    //Do nothing
                }

            }).check()
    }
    @SuppressLint("MissingPermission")
    fun getLocation(){
        //TODO : Get location of User
        val locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location?) {

                Log.i("locationUpdate","lat : ${location?.latitude}, lon : ${location?.longitude}")

                if (location != null){
                    latitude= location.latitude
                    longitude= location.longitude
                    makeNetworkCall()
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                //Do nothing
            }

            override fun onProviderEnabled(provider: String?) {
                //Do nothing
            }

            override fun onProviderDisabled(provider: String?) {
                //Do nothing
            }

        }
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,2.0f,locationListener)
    }
    fun makeNetworkCall(){
        //TODO : Call Network Call Function Here


        showLoading()
        weatherService.getWeatherByCoordinates(latitude,longitude,"a7bd521bfef4caf29b1e5f820049a930","metric").enqueue(object : Callback<WeatherResponse>{
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t?.printStackTrace()
                showError()
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response?.isSuccessful){
                    weatherResponse = response.body()
                    Log.i("weatherResponse",weatherResponse.toString())
                    showWeatherInfo()
                }else{
                    showError()
                }
            }

        })

    }
    fun showLoading(){
        //TODO : Show Loading
        groupdata.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }
    fun showError(){
        //TODO : Show Error
        Snackbar.make(rootView,"Error getting weather data",Snackbar.LENGTH_INDEFINITE)
            .setAction("Retry"){
                makeNetworkCall()
            }.show()
    }
    fun showWeatherInfo(){
        //TODO : show Weather
        if (weatherResponse != null) {
            groupdata.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            val decimalFormat = DecimalFormat("#.#")
            val roundedTemp = decimalFormat.format(weatherResponse!!.main.temperature)
            tv_temperature.text = roundedTemp.toString()
            tv_humidity.text = weatherResponse!!.main.humidity.toString()+"%"
            tv_cityName.text = weatherResponse!!.cityName

            if (weatherResponse!!.weatherList.isNotEmpty()) {

                tv_desc.text = weatherResponse!!.weatherList[0].weatherDesc


                var weatherIcon: String = weatherResponse!!.weatherList[0].weatherIcon
                when(weatherIcon){
                     "01d" -> {iv_weather.setImageResource(R.drawable.clearsky_day)}

                     "01n" -> {iv_weather.setImageResource(R.drawable.clearsky_night)}
                     "02d" -> {iv_weather.setImageResource(R.drawable.fewclouds_day)}
                     "02n" -> {iv_weather.setImageResource(R.drawable.fewclouds_night)}
                     "03d" -> {iv_weather.setImageResource(R.drawable.scatteredcloud_day)}
                     "03n" -> {iv_weather.setImageResource(R.drawable.scatteredcloud_night)}
                     "04d" -> {iv_weather.setImageResource(R.drawable.brokencloud_day)}
                     "04n" -> {iv_weather.setImageResource(R.drawable.brokencloud_night)}
                     "09d" -> {iv_weather.setImageResource(R.drawable.showerrain_day)}
                     "09n" -> {iv_weather.setImageResource(R.drawable.showerrain_night)}
                     "10d" -> {iv_weather.setImageResource(R.drawable.rain_day)}
                     "10n" -> {iv_weather.setImageResource(R.drawable.rain_night)}
                     "11d" -> {iv_weather.setImageResource(R.drawable.thunderstorm_day)}
                     "11n" -> {iv_weather.setImageResource(R.drawable.thunderstorm_night)}
                     "13d" -> {iv_weather.setImageResource(R.drawable.snow_day)}
                     "13n" -> {iv_weather.setImageResource(R.drawable.snow_night)}
                     "50d" -> {iv_weather.setImageResource(R.drawable.mist_day)}
                     "50n" -> {iv_weather.setImageResource(R.drawable.mist_night)}


                }

              /*  Glide.with(this)
                    .load("http://openweathermap.org/img/w/${weatherResponse!!.weatherList[0].weatherIcon}.png")
                    .into(iv_weather)
                    */

            }else{
                iv_weather.visibility = View.GONE
            }

        }
    }

}
