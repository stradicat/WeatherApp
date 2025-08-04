package dev.dmayr.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import dev.dmayr.weatherapp.databinding.ActivityMainBinding
import dev.dmayr.weatherapp.repository.ClimaRepository
import dev.dmayr.weatherapp.utils.LocationHelper.obtenerUbicacion
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val locationRequestPermission = 100
    private var ultimaCiudadConsultada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        enableEdgeToEdge()
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListeners()
    }

    fun Context.hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let { view ->
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setupClickListeners() {
        binding.btnBuscar.setOnClickListener {
            val ciudad = binding.etIngresarCiudad.text.toString().trim()
            if (ciudad.isNotBlank()) {
                obtenerClima(ciudad)
            } else {
                Toast.makeText(
                    this,
                    "Por favor, ingrese el nombre de una ciudad",
                    Toast.LENGTH_SHORT
                ).show()
            }
            hideKeyboard()
        }

        binding.btnUbicacion.setOnClickListener {
            solicitarPermisosUbicacion()
            hideKeyboard()
        }

        binding.tvCiudad.setOnClickListener {
            if (ultimaCiudadConsultada.isNotEmpty()) {
                abrirPronostico(ultimaCiudadConsultada)
            } else {
                Toast.makeText(this, "Primero busque el clima de una ciudad", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun obtenerClima(ciudad: String) {
        lifecycleScope.launch {
            try {
                binding.tvCiudad.text = "🔄 Cargando..."
                binding.tvTemperatura.text = "--°C"
                binding.tvDescripcion.text = "Obteniendo datos del clima..."

                val climaRepository = ClimaRepository()
                val climaResponse = climaRepository.obtenerClima(ciudad)

                binding.tvCiudad.text = "🏙️ ${climaResponse.nombre}"
                binding.tvDescripcion.text = "☁️ ${
                    climaResponse.weather[0].description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                }"
                binding.tvTemperatura.text = "${climaResponse.main.temp.toInt()}°C"
                ultimaCiudadConsultada = climaResponse.nombre

                Toast.makeText(this@MainActivity, "Clima actualizado", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                binding.tvCiudad.text = "❌ Error"
                binding.tvTemperatura.text = "--°C"
                binding.tvDescripcion.text = "No se pudo obtener el clima"

                Toast.makeText(
                    this@MainActivity,
                    e.message ?: "Error desconocido",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun abrirPronostico(ciudad: String) {
        val intent = Intent(this, PronosticoActivity::class.java)
        intent.putExtra("CIUDAD_NOMBRE", ciudad)
        startActivity(intent)
    }

    private fun solicitarPermisosUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationRequestPermission
            )
        } else {
            obtenerUbicacion()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestPermission &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            obtenerUbicacion()
        } else {
            Toast.makeText(
                this,
                "Permiso de ubicación requerido para obtener clima actual",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun obtenerUbicacion() {
        binding.tvCiudad.text = "📍 Obteniendo ubicación..."
        binding.tvTemperatura.text = "--°C"
        binding.tvDescripcion.text = "Buscando su ubicación actual..."

        fun procesarDireccion(direcciones: List<Address>?) {
            val ciudad = direcciones?.firstOrNull()?.locality ?: "Ciudad no encontrada"

            if (ciudad != "Ciudad no encontrada") {
                binding.etIngresarCiudad.setText(ciudad)
                obtenerClima(ciudad)
            } else {
                mostrarErrorUbicacion("No se pudo determinar la ciudad")
            }
        }

        obtenerUbicacion(this) { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ) { direcciones ->
                        procesarDireccion(direcciones)
                    }
                } else {
                    try {
                        @Suppress("DEPRECATION")
                        val direcciones =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        procesarDireccion(direcciones)
                    } catch (_: Exception) {
                        mostrarErrorUbicacion("Error al obtener nombre de la ciudad")
                    }
                }
            } else {
                mostrarErrorUbicacion("No se pudo obtener ubicación")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarErrorUbicacion(mensaje: String = "No se pudo obtener el clima") {
        binding.tvCiudad.text = "❌ Error"
        binding.tvTemperatura.text = "--°C"
        binding.tvDescripcion.text = "No se pudo obtener el clima"
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
