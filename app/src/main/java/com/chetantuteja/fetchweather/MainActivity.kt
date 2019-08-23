package com.chetantuteja.fetchweather

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        const val API_KEY = "INSERT_YOUR_API_KEY_HERE"
        const val ERROR = "ERROR!"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun getWeatherClick(view: View) {
        if(!isNetworkAvailable()) {
            dialogMaker(ERROR, getString(R.string.no_internet))
        }
        else {
            Log.d("ButtonClick", "Button Click success")
            val cityName = getCityET.text.toString()
            if (cityName.trim().isEmpty()) {
                dialogMaker("ERROR!", getString(R.string.no_city))
                //getCityET.error = getString(R.string.no_city)
                //getCityET.requestFocus()

            } else {
                val apiURL = "https://api.openweathermap.org/data/2.5/weather?q=$cityName&appid=$API_KEY&units=imperial"
                Ion.with(this)
                    .load(apiURL)
                    .asString()
                    .setCallback { e, result ->
                        Log.d("IonData", "JSON = $result")
                        if(e !=null) {
                            val errorM = e.message!!
                            dialogMaker(ERROR, errorM)
                        }
                        setupIntent(result)


                    }

                YoYo.with(Techniques.Tada)
                    .duration(2000)
                    .playOn(btnGetWeather)
            }
        }
    }

    private fun setupIntent(jsonData: String) {
        if(!isJSONDataValid(jsonData)) {
            dialogMaker(ERROR, getString(R.string.no_city))
            getCityET.setText("")

        } else {
            val intent = Intent(this, DisplayWeatherActivity::class.java)
            intent.putExtra("json_data", jsonData)
            startActivity(intent)
        }

    }

    private fun isJSONDataValid(jsonData: String): Boolean {
        val msg = JSONObject(jsonData)
            .getString("cod")
        if(msg.equals("404", ignoreCase = true)) {
            return false
        }
        return true
    }

    private fun isNetworkAvailable():Boolean {
        val cManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeInfo = cManager.activeNetwork
        if(activeInfo!=null) {
            val cap = cManager.getNetworkCapabilities(activeInfo)
            if(cap!=null) {
                cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                return true
            }
        }
        return false
    }

    private fun dialogMaker(title: String, message: String) {
        val dBuilder = AlertDialog.Builder(this)
        dBuilder.setTitle(title)
        dBuilder.setMessage(message)
        dBuilder.setCancelable(false)
        dBuilder.setNeutralButton("OK",null)
        val dialog = dBuilder.create()
        dialog.show()
    }
}
