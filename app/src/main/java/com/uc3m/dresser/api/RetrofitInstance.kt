package com.uc3m.dresser.api

import com.uc3m.dresser.util.Constants.Companion.OW_URL
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {


        val certificatePinner = CertificatePinner.Builder().add("api.openweathermap.org","sha256/axmGTWYycVN5oCjh3GJrxWVndLSZjypDO6evrHMwbXg=").build()

        val okHttpClient = OkHttpClient.Builder().certificatePinner(certificatePinner).build()

        Retrofit.Builder()
            .baseUrl(OW_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .build()
    }

    val weatherAPI: WeatherAPI by lazy {
        retrofit.create(WeatherAPI::class.java)
    }
}