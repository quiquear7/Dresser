package com.uc3m.dresser.api

import com.uc3m.dresser.util.Constants.Companion.OW_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OW_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherAPI: WeatherAPI by lazy {
        retrofit.create(weatherAPI::class.java)
    }
}