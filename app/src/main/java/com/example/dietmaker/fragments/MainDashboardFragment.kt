// Lokalizacja: app/src/main/java/com/example/dietmaker/fragments/MainDashboardFragment.kt
package com.example.dietmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.example.dietmaker.MainActivity
import com.example.dietmaker.utils.DataManager
import com.example.dietmaker.R
import com.example.dietmaker.databinding.FragmentMainDashboardBinding
import java.util.*

class MainDashboardFragment : Fragment() {
    private lateinit var binding: FragmentMainDashboardBinding
    private lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainDashboardBinding.inflate(inflater, container, false)
        dataManager = DataManager(requireContext())

        // Dodaj w metodzie onCreateView lub w osobnej metodzie setupButtons
        binding.buttonMeals.setOnClickListener {
            (activity as MainActivity).replaceFragment(MealsFragment())
        }

        // Dodaj w metodzie onCreateView lub w osobnej metodzie setupButtons
        binding.buttonProfile.setOnClickListener {
            (activity as MainActivity).replaceFragment(ProfileFragment())
        }

        binding.buttonSensor.setOnClickListener {
            (activity as MainActivity).replaceFragment(SensorFragment())
        }

        setupUserInfo()
        setupProgressBars()

        return binding.root
    }

    private fun setupUserInfo() {
        val userProfile = dataManager.getUserProfile()
        binding.tvUsername.text = userProfile.name

        // Ustawienie aktualnej daty
        val currentDate = Date()
        binding.tvCurrentDate.text = android.text.format.DateFormat.getDateFormat(requireContext()).format(currentDate)
    }



    private fun setupProgressBars() {
        val userProfile = dataManager.getUserProfile()
        val currentDate = Date()
        val meals = dataManager.getMealsForDate(currentDate)

        // Obliczenie sum makroskładników
        val totalCarbs = meals.sumOf { it.carbohydrates.toDouble() }
        val totalProteins = meals.sumOf { it.proteins.toDouble() }
        val totalFats = meals.sumOf { it.fats.toDouble() }
        val totalCalories = meals.sumOf { it.calories.toDouble() }


        // Ustawienie pasków postępu
        setupProgressBar(
            binding.progressCarbs,
            "Węglowodany",
            totalCarbs.toFloat(),
            userProfile.carbGoal
        )
        setupProgressBar(
            binding.progressProteins,
            "Białka",
            totalProteins.toFloat(),
            userProfile.proteinGoal
        )
        setupProgressBar(
            binding.progressFats,
            "Tłuszcze",
            totalFats.toFloat(),
            userProfile.fatGoal
        )
        setupProgressBar(
            binding.progressCalories,
            "Kalorie",
            totalCalories.toFloat(),
            userProfile.calorieGoal
        )
    }

    private fun setupProgressBar(
        progressBar: RoundCornerProgressBar,
        label: String,
        currentValue: Float,
        goalValue: Float
    ) {
        val percentage = if (goalValue > 0) (currentValue / goalValue * 100).coerceAtMost(100f) else 0f

        progressBar.apply {
            setMax(100f)
            setProgress(percentage)
        }

        // Znajdź TextView do wyświetlenia opisu
        val labelView = when(progressBar.id) {
            R.id.progressCarbs -> binding.tvCarbsLabel
            R.id.progressProteins -> binding.tvProteinsLabel
            R.id.progressFats -> binding.tvFatsLabel
            R.id.progressCalories -> binding.tvCaloriesLabel
            else -> null

        }



        labelView?.text = "$label (${percentage.toInt()}%)"
    }

    override fun onResume() {
        super.onResume()
        setupProgressBars()  // Odświeżamy paski postępu
    }
}