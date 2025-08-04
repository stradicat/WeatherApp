package dev.dmayr.weatherapp.model

data class PronosticoResponse(
    val list: List<DiaPronostico>
)

data class DiaPronostico(
    val dtTxt: String,
    val main: Main,
    val weather: List<Weather>
)
