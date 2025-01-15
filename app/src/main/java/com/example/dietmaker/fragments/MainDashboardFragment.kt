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
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.text.SimpleDateFormat
import android.util.Log

class MainDashboardFragment : Fragment() {
    private lateinit var binding: FragmentMainDashboardBinding
    private lateinit var dataManager: DataManager
    private val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    var currentDate : LocalDate = LocalDate.now()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MainDashboardFragment", "onCreateView called")

        binding = FragmentMainDashboardBinding.inflate(inflater, container, false)
        dataManager = DataManager(requireContext())

        // Dodaj w metodzie onCreateView lub w osobnej metodzie setupButtons
        binding.buttonMeals.setOnClickListener {
            val mealsFragment = MealsFragment.newInstance(currentDate)
            (activity as MainActivity).replaceFragment(mealsFragment)
        }

        // Dodaj w metodzie onCreateView lub w osobnej metodzie setupButtons
        binding.buttonProfile.setOnClickListener {
            (activity as MainActivity).replaceFragment(ProfileFragment())
        }

        binding.buttonSensor.setOnClickListener {
            (activity as MainActivity).replaceFragment(SensorFragment())
        }

        binding.buttonCalendar.setOnClickListener{ //Dodanie przycisku kalendarza
            (activity as MainActivity).replaceFragment(CalendarFragment())
        }

        parentFragmentManager.setFragmentResultListener("selectedDate", viewLifecycleOwner) { key, bundle ->
            Log.d("MainDashboardFragment", "setFragmentResultListener called")
            val selectedDateString = bundle.getString("date")
            Log.d("MainDashboardFragment", "Received date string: $selectedDateString")
            val selectedDate = LocalDate.parse(selectedDateString)
            Log.d("MainDashboardFragment", "Parsed date: $selectedDate")
            currentDate = selectedDate
            updateDateTextView(currentDate)
            setupProgressBars(currentDate)


        }
        setupUserInfo()
        setupProgressBars(currentDate)


        return binding.root
    }

    private fun setupUserInfo() {
        val userProfile = dataManager.getUserProfile()
        binding.tvUsername.text = userProfile.name

        // Ustawienie aktualnej daty
        Log.d("MainDashboardFragment", "setupUserInfo called")
        updateDateTextView(currentDate)


    }

    private fun updateDateTextView(date: LocalDate){
        val dateToConvert: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        binding.tvCurrentDate.text = dateFormat.format(dateToConvert)
        Log.d("MainDashboardFragment", "updateDateTextView called with date: $date")
    }
    private fun setupProgressBars(date: LocalDate) {
        val userProfile = dataManager.getUserProfile()
        val dateToConvert: Date = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
        val meals = dataManager.getMealsForDate(dateToConvert)
        Log.d("MainDashboardFragment", "setupProgressBars called with date: $date, meals size: ${meals.size}")


        // Obliczenie sum makroskładników
        val totalCarbs = meals.sumOf { it.carbohydrates.toDouble() }
        val totalProteins = meals.sumOf { it.proteins.toDouble() }
        val totalFats = meals.sumOf { it.fats.toDouble() }
        val totalCalories = meals.sumOf { it.calories.toDouble() }
        Log.d("MainDashboardFragment", "Total calories: $totalCalories, carbs: $totalCarbs, proteins: $totalProteins, fats: $totalFats")


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
        Log.d("MainDashboardFragment", "onResume called")
        setupProgressBars(currentDate)

    }
}