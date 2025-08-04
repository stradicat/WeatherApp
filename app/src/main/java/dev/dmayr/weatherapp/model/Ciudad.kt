package dev.dmayr.weatherapp.model

data class Ciudad(
    val nombre: String,
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val ultimaConsulta: Long = System.currentTimeMillis()
)
