package dev.dmayr.weatherapp.api

import dev.dmayr.weatherapp.model.ClimaResponse
import dev.dmayr.weatherapp.model.PronosticoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {
    @GET("weather")
    suspend fun getClimaPorCiudad(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<ClimaResponse>

    @GET("forecast")
    suspend fun getPronosticoPorCiudad(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): Response<PronosticoResponse>
}
