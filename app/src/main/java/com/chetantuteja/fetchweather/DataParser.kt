package com.chetantuteja.fetchweather

import android.util.Log
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DataParser(Data: String) {
    private val LOG_NAME = "JSONValues"
    private var iconID = 0
    private var weatherDesc = "Weather Description"
    private var temprature = 0.0
    private var pressure = 0.0
    private var humidity = 0.0
    private var windSpeed = 0.0
    private var date = 0
    private var sunrise = 0
    private var sunset = 0
    private var cityName = "City Name"
    private var jsonData = Data

    init {
        extractAllData()
    }

    private fun extractAllData() {
        val BASE_OBJECT =JSONObject(jsonData)
        val jsonArray = BASE_OBJECT.getJSONArray("weather")

        iconID = jsonArray.getJSONObject(0).getInt("id")
        weatherDesc = jsonArray.getJSONObject(0).getString("description")

        val mainObject = BASE_OBJECT.getJSONObject("main")

        temprature = mainObject.getDouble("temp")
        pressure = mainObject.getDouble("pressure")
        humidity = mainObject.getDouble("humidity")

        windSpeed = BASE_OBJECT.getJSONObject("wind").getDouble("speed")

        date = BASE_OBJECT.getInt("dt")
        sunrise = BASE_OBJECT.getJSONObject("sys").getInt("sunrise")
        sunset = BASE_OBJECT.getJSONObject("sys").getInt("sunset")

        cityName = BASE_OBJECT.getString("name")


    }

    fun getIconID(): String {
        return iconID.toString()
    }

    fun getWeatherDesc(): String {
        return weatherDesc
    }

    fun getTemprature(): Double {
        return temprature
    }

    fun getPressure(): String {
        return pressure.toString()
    }

    fun getHumidity(): String {
        return humidity.toString()
    }

    fun getWindSpeed(): String {
        return windSpeed.toString()
    }

    fun getCityName(): String {
        return cityName
    }

    fun isDayTime(): Boolean {
        val currTime = Date(date.toLong() * 1000)
        val sunriseTime = Date(sunrise.toLong() * 1000)
        val sunsetTime = Date(sunset.toLong() * 1000)
        if(currTime.after(sunriseTime) && currTime.before(sunsetTime)) {
            return true
        }
        return false
    }

    fun logAllValues() {
        val d1 = Date(date.toLong() * 1000)
        val d2 = Date(sunrise.toLong() * 1000)
        val d3 = Date(sunset.toLong() * 1000)
        val sdf = SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT+05:30")
        val formatDate = sdf.format(d1)

        Log.d(LOG_NAME, "Curr Date: $formatDate")
        Log.d(LOG_NAME, "Sunrise: "+sdf.format(d2))
        Log.d(LOG_NAME, "Sunset: "+sdf.format(d3))

        if(d1.after(d2) && d1.before(d3)) {
            Log.d(LOG_NAME, "it is day time")

        }
        else {
            Log.d(LOG_NAME, "it is night time")
        }

        Log.d(LOG_NAME, "Time: "+d1.time)
        Log.d(LOG_NAME, "icon: $iconID")
        Log.d(LOG_NAME, "weatherDesc: $weatherDesc")
        Log.d(LOG_NAME, "temp: $temprature")
        Log.d(LOG_NAME, "pressure: $pressure")
        Log.d(LOG_NAME, "humidity: $humidity")
        Log.d(LOG_NAME, "windspeed: $windSpeed")
        Log.d(LOG_NAME, "date: $date")
        Log.d(LOG_NAME, "sunrise: $sunrise")
        Log.d(LOG_NAME, "sunset: $sunset")
        Log.d(LOG_NAME, "city: $cityName")
    }



}