package dev.dmayr.weatherapp.repository

import android.util.Log
import dev.dmayr.weatherapp.api.ClimaApiService
import dev.dmayr.weatherapp.api.RetrofitClient
import dev.dmayr.weatherapp.model.ClimaResponse
import dev.dmayr.weatherapp.model.PronosticoResponse

class ClimaRepository {
    private val api: ClimaApiService = RetrofitClient.instance.create(ClimaApiService::class.java)

    companion object {
        private const val TAG = "ClimaRepository"
    }

    suspend fun obtenerClima(ciudad: String): ClimaResponse {
        Log.d(TAG, "Obteniendo clima para: $ciudad")

        try {
            val response = api.getClimaPorCiudad(
                ciudad = ciudad,
                apiKey = RetrofitClient.apiKey
            )

            if (response.isSuccessful) {
                Log.d(TAG, "Clima obtenido exitosamente para: $ciudad")
                return response.body() ?: throw kotlin.Exception("Respuesta vacía del servidor")
            } else {
                Log.e(TAG, "Error en API: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    404 -> throw kotlin.Exception("Ciudad no encontrada")
                    401 -> throw kotlin.Exception("Error de autenticación API")
                    429 -> throw kotlin.Exception("Demasiadas solicitudes, intente más tarde")
                    500 -> throw kotlin.Exception("Error interno del servidor")
                    else -> throw kotlin.Exception("Error del servidor: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener clima", e)
            throw e
        }
    }

    suspend fun obtenerPronostico(ciudad: String): PronosticoResponse {
        Log.d(TAG, "Obteniendo pronóstico para: $ciudad")

        try {
            val response = api.getPronosticoPorCiudad(
                ciudad = ciudad,
                apiKey = RetrofitClient.apiKey
            )

            if (response.isSuccessful) {
                Log.d(TAG, "Pronóstico obtenido exitosamente para: $ciudad")
                return response.body() ?: throw kotlin.Exception("Respuesta vacía del servidor")
            } else {
                Log.e(TAG, "Error en API pronóstico: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    404 -> throw kotlin.Exception("Ciudad no encontrada")
                    401 -> throw kotlin.Exception("Error de autenticación API")
                    429 -> throw kotlin.Exception("Demasiadas solicitudes, intente más tarde")
                    500 -> throw kotlin.Exception("Error interno del servidor")
                    else -> throw kotlin.Exception("Error del servidor: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener pronóstico", e)
            throw e
        }
    }
}
