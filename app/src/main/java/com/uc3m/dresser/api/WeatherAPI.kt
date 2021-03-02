package com.uc3m.dresser.api

import com.uc3m.dresser.model.Clima
import retrofit2.Response
import retrofit2.http.GET

interface WeatherAPI {
    @GET("city")
    suspend fun getClima(): Response<Clima>
}