package com.ua.dronecalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ua.dronecalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat
import kotlin.math.atan
import kotlin.math.tan

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val decimalFormat = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClearListeners()
        setMainButtonsListeners()
    }

    private fun setMainButtonsListeners() {
        with(binding) {
            clearButton.setOnClickListener {
                angleEdit.text.clear()
                distanceEdit.text.clear()
                heightEdit.text.clear()
                clearErrors()
            }
            clearWidthHeightButton.setOnClickListener {
                distanceEdit.text.clear()
                heightEdit.text.clear()
                clearErrors()
            }
            calculateButton.setOnClickListener { calculate() }
        }
    }

    private fun calculate() {
        clearErrors()
        when (getMode()) {
            Mode.ANGLE -> calculateAngle()
            Mode.DISTANCE -> calculateDistance()
            Mode.HEIGHT -> calculateHeight()
            Mode.ERROR -> showErrors()
        }
    }

    private fun showErrors() {
        with(binding) {
            if (angleEdit.text.isEmpty()) {
                angleEdit.error = getString(R.string.error_angle_empty)
            }
            if (distanceEdit.text.isEmpty()) {
                distanceEdit.error = getString(R.string.error_distance_empty)
            }
            if (heightEdit.text.isEmpty()) {
                heightEdit.error = getString(R.string.error_height_empty)
            }
        }
    }

    private fun clearErrors() {
        with(binding) {
            angleEdit.error = null
            distanceEdit.error = null
            heightEdit.error = null
        }
    }

    private fun calculateHeight() {
        with(binding) {
            val distance = distanceEdit.text.toString().toDouble()
            val angle = angleEdit.text.toString().toDouble() / 180 * Math.PI
            val height = distance * tan(angle)
            heightEdit.setText(decimalFormat.format(height))
        }
    }

    private fun calculateDistance() {
        with(binding) {
            val angle = angleEdit.text.toString().toDouble() / 180 * Math.PI
            val height = heightEdit.text.toString().toDouble()
            val distance = height / tan(angle)
            distanceEdit.setText(decimalFormat.format(distance))
        }
    }

    private fun calculateAngle() {
        with(binding) {
            val distance = distanceEdit.text.toString().toDouble()
            val height = heightEdit.text.toString().toDouble()
            val angle: Double = atan(height / distance) * 180 / Math.PI
            angleEdit.setText(decimalFormat.format(angle))
        }
    }


    private fun getMode(): Mode {
        with(binding) {
            if (distanceEdit.text.isNotEmpty() && heightEdit.text.isNotEmpty() && angleEdit.text.isEmpty()) {
                return Mode.ANGLE
            } else if (distanceEdit.text.isEmpty() && heightEdit.text.isNotEmpty() && angleEdit.text.isNotEmpty()) {
                return Mode.DISTANCE
            } else if (heightEdit.text.isEmpty() && distanceEdit.text.isNotEmpty() && angleEdit.text.isNotEmpty()) {
                return Mode.HEIGHT
            } else {
                return Mode.ERROR
            }
        }
    }

    private fun setClearListeners() {
        with(binding) {
            angleClearButton.setOnClickListener { angleEdit.text.clear() }
            distanceClearButton.setOnClickListener { distanceEdit.text.clear() }
            heightClearButton.setOnClickListener { heightEdit.text.clear() }
        }
    }

    private sealed class Mode {
        object ANGLE : Mode()
        object DISTANCE : Mode()
        object HEIGHT : Mode()
        object ERROR : Mode()
    }
}