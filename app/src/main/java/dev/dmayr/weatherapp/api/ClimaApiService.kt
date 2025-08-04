package dev.dmayr.weatherapp.api

import dev.dmayr.weatherapp.model.ClimaResponse
import dev.dmayr.weatherapp.model.PronosticoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {
    @GET("weather")
    suspend fun getClimaPorCiudad(
        @Query("q") ciudad: String
    ): Response<ClimaResponse>

    @GET("forecast")
    suspend fun getPronosticoPorCiudad(
        @Query("q") ciudad: String
    ): Response<PronosticoResponse>
}
