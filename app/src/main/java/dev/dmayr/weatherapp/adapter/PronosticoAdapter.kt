package dev.dmayr.weatherapp.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.dmayr.weatherapp.databinding.ItemDiaPronosticoBinding
import dev.dmayr.weatherapp.model.DiaPronostico
import java.text.SimpleDateFormat
import java.util.Locale

class PronosticoAdapter(private val pronosticos: List<DiaPronostico>) :
    RecyclerView.Adapter<PronosticoAdapter.PronosticoViewHolder>() {

    class PronosticoViewHolder(binding: ItemDiaPronosticoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivIcono: ImageView = binding.ivIconoClima
        val tvFecha: TextView = binding.tvFecha
        val tvDescripcion: TextView = binding.tvDescripcionPronostico
        val tvTemperatura: TextView = binding.tvTemperaturaPronostico
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PronosticoViewHolder {
        val binding = ItemDiaPronosticoBinding.inflate(LayoutInflater.from(parent.context))
        Log.d("PronosticoAdapter", "onCreateViewHolder llamado")
        return PronosticoViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PronosticoViewHolder, position: Int) {
        val pronostico = pronosticos[position]
        Log.d("PronosticoAdapter", "Binding posición $position: ${pronostico.dtTxt}")

        try {
            val fechaFormateada = formatearFecha(pronostico.dtTxt)
            holder.tvFecha.text = fechaFormateada

            val descripcionClima = if (pronostico.weather.isNotEmpty()) {
                pronostico.weather[0].description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                }
            } else {
                "Sin descripción"
            }
            holder.tvDescripcion.text = descripcionClima
            holder.tvTemperatura.text = "${pronostico.main.temp.toInt()}°C"

            val main = if (pronostico.weather.isNotEmpty()) pronostico.weather[0].main else "Clear"
            val emoji = obtenerEmojiClima(main)
            holder.ivIcono.contentDescription = emoji
        } catch (e: Exception) {
            Log.e("PronosticoAdapter", "Error en onBindViewHolder posición $position: ${e.message}")
            holder.tvFecha.text = "Error"
            holder.tvDescripcion.text = "Error al cargar datos"
            holder.tvTemperatura.text = "--°C"
        }
    }

    override fun getItemCount(): Int = pronosticos.size

    private fun formatearFecha(fechaString: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val fecha = formatoEntrada.parse(fechaString)
            val formatoSalida = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatoSalida.format(fecha!!)
        } catch (_: Exception) {
            fechaString.substring(0, 10)
        }
    }

    private fun obtenerEmojiClima(condicionClima: String): String {
        return when (condicionClima.lowercase()) {
            "clear" -> "☀️"
            "clouds" -> "☁️"
            "rain" -> "🌧️"
            "drizzle" -> "🌦️"
            "thunderstorm" -> "⛈️"
            "snow" -> "❄️"
            "mist", "fog" -> "🌫️"
            else -> "🌤️"
        }
    }
}
