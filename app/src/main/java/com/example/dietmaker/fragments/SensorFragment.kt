// app/src/main/java/com/example/dietmaker/fragments/SensorFragment.kt
package com.example.dietmaker.fragments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dietmaker.databinding.FragmentSensorBinding
import kotlin.math.sqrt

class SensorFragment : Fragment(), SensorEventListener {
    private lateinit var binding: FragmentSensorBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lightSensor: Sensor? = null
    private var caloriesBurned = 0f
    private val movementThreshold = 15f // Próg wykrycia potrząsania
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSensorBinding.inflate(inflater, container, false)

        setupSensors()
        setupMeasureLightButton()

        return binding.root
    }

    private fun setupSensors() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (accelerometer == null) {
            Toast.makeText(context, "Brak akcelerometru w urządzeniu", Toast.LENGTH_LONG).show()
        }
        if (lightSensor == null) {
            Toast.makeText(context, "Brak czujnika światła w urządzeniu", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupMeasureLightButton() {
        binding.buttonMeasureLight.setOnClickListener {
            if (lightSensor != null) {
                // Rejestrujemy nasłuchiwanie czujnika światła na krótki moment
                sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                binding.textLightValue.text = "Czujnik światła niedostępny"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> handleAccelerometerEvent(event)
            Sensor.TYPE_LIGHT -> handleLightSensorEvent(event)
        }
    }

    private fun handleAccelerometerEvent(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastUpdate) > 100) { // Sprawdzamy co 100ms
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val speed = calculateMovement(x, y, z)

            if (speed > movementThreshold) {
                caloriesBurned += 0.1f // Dodajemy kalorie za ruch
                binding.textCaloriesBurned.text = String.format("%.1f kcal", caloriesBurned)
            }

            lastX = x
            lastY = y
            lastZ = z
            lastUpdate = currentTime
        }
    }

    private fun handleLightSensorEvent(event: SensorEvent) {
        val lightValue = event.values[0]
        binding.textLightValue.text = String.format("%.1f lux", lightValue)
        // Wyrejestrowujemy nasłuchiwanie czujnika światła po otrzymaniu odczytu
        sensorManager.unregisterListener(this, lightSensor)
    }

    private fun calculateMovement(x: Float, y: Float, z: Float): Float {
        val deltaX = x - lastX
        val deltaY = y - lastY
        val deltaZ = z - lastZ
        return sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Nie potrzebujemy implementacji
    }
}