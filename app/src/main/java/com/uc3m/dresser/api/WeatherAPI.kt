package com.uc3m.dresser.api

import com.uc3m.dresser.model.Clima
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface WeatherAPI {
    @GET("/data/2.5/weather?appid=b68e8f01d046458b6358fe2284f5d96f&lang=es&units=metric")
    suspend fun getClima(@QueryMap params: Map<String, String> ): Response<Clima>
}