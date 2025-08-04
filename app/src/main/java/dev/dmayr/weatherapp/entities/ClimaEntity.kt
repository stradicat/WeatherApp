package dev.dmayr.weatherapp.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.dmayr.weatherapp.model.Weather


@Entity(tableName = "clima_actual")
@TypeConverters(Converters::class)
data class ClimaEntity(
    @PrimaryKey
    val ciudad: String,
    val temperatura: Double,
    val descripcion: String,
    val iconoClima: String,
    val condicionPrincipal: String,
    val presion: Int,
    val humedad: Int,
    val temMax: Double,
    val temMin: Double,
    val fechaActualizacion: Long = System.currentTimeMillis(),
    val weather: List<Weather>
)

@Entity(tableName = "pronostico", primaryKeys = ["id"])
@TypeConverters(Converters::class)
data class PronosticoEntity(
    val id: String,
    val ciudad: String,
    val fechaHora: String,
    val temperatura: Double,
    val iconoClima: String,
    val condicionPrincipal: String,
    val presion: Int,
    val humedad: Int,
    val temMax: Double,
    val temMin: Double,
    val fechaActualizacion: Long = System.currentTimeMillis(),
    val weather: List<Weather>
)

// Room
class Converters {
    @TypeConverter
    fun fromWeatherList(value: List<Weather>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toWeatherList(value: String): List<Weather> {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
