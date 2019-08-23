package com.chetantuteja.fetchweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_display_weather.*
import java.math.RoundingMode

class DisplayWeatherActivity : AppCompatActivity() {


    private lateinit var jsonDataParser: DataParser
    private var isCelsius = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_weather)
        extractIntent()
    }

    private fun extractIntent() {
        val ourIntent = intent
        if(ourIntent.hasExtra("json_data")) {
            val jsonData = ourIntent.getStringExtra("json_data")
            processJSONData(jsonData)
        }
        else {
            Log.d("IntentData", "No Intent Data")
        }
    }

    private fun processJSONData(dataRec: String) {
        jsonDataParser = DataParser(dataRec)
        jsonDataParser.logAllValues()

        setupCardValues()

    }

    private fun setupCardValues() {
        cardCityName.text = jsonDataParser.getCityName()
        cardWeatherLabel.text = jsonDataParser.getWeatherDesc().toUpperCase()
        val iconID = retIconID(jsonDataParser.getIconID())
        cardWeatherIcon.setIconResource(getString(iconID))


        val tempValue = jsonDataParser.getTemprature().toBigDecimal().setScale(2, RoundingMode.UP).toString()

       cardTempValue.text = tempValue+"°F"
        cardPressureValue.text = jsonDataParser.getPressure()+" hPa"
        cardHumidityValue.text = jsonDataParser.getHumidity()+"%"
        cardWindValue.text = jsonDataParser.getWindSpeed()+"m/s"

    }

    private fun retIconID(iconCode: String): Int {
        val dayTime = "wi_owm_day_$iconCode"
        val nightTime = "wi_owm_night_$iconCode"
        var toRet = 0
        if(jsonDataParser.isDayTime()) {
            toRet = resources.getIdentifier(dayTime, "string", packageName)
        }
        else {
            toRet = resources.getIdentifier(nightTime, "string", packageName)
        }
        return toRet
    }

    fun toggleTempClick(view: View) {
        //todo
        if(isCelsius) {
            val tempValue = jsonDataParser.getTemprature().toBigDecimal().setScale(2, RoundingMode.UP).toString()
            cardTempValue.text = tempValue+"°F"
            isCelsius = false
        }
        else {
            val converted = toCelsius(jsonDataParser.getTemprature())
            val tempValue = converted.toBigDecimal().setScale(2, RoundingMode.UP).toString()
            cardTempValue.text = tempValue+"°C"
            isCelsius = true
        }
    }

    fun resetWeatherClick(view: View) {
        //todo
        val sendBack = Intent(this, MainActivity::class.java)
        //finish()
        sendBack.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(sendBack)

    }


    private fun toCelsius(tempVal: Double): Double{
        val calc = (tempVal -32) * 5/9
        return calc
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
