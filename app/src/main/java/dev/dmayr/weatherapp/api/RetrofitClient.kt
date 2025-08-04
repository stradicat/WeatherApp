package dev.dmayr.weatherapp.api

import dev.dmayr.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("appid", BuildConfig.WEATHER_API_KEY)
            .addQueryParameter("units", "metric")
            .addQueryParameter("lang", "es")
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
