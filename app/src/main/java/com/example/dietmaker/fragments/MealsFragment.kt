package com.example.dietmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dietmaker.R
import com.example.dietmaker.databinding.FragmentMealsBinding
import com.example.dietmaker.utils.DataManager
import com.example.dietmaker.utils.Meal
import com.example.dietmaker.adapters.MealAdapter
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class MealsFragment : Fragment() {
    private lateinit var binding: FragmentMealsBinding
    private lateinit var dataManager: DataManager
    private lateinit var mealAdapter: MealAdapter
    private var currentDate : Date = Date()
    private var selectedDate: LocalDate = LocalDate.now()


    companion object {
        fun newInstance(date: LocalDate): MealsFragment {
            val fragment = MealsFragment()
            val bundle = Bundle()
            bundle.putString("date", date.toString())
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)
        dataManager = DataManager(requireContext())
        selectedDate = arguments?.getString("date")?.let {
            LocalDate.parse(it)
        } ?: LocalDate.now()
        currentDate = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        setupRecyclerView()
        setupAddButton()
        loadMeals()
        setupSearchButton()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadMeals() // Odśwież listę po powrocie do fragmentu
    }

    private fun setupSearchButton() {
        binding.buttonSearchMeal.setOnClickListener {
            val searchMealFragment = SearchMealFragment()
            val bundle = Bundle()
            bundle.putString("date",selectedDate.toString())
            searchMealFragment.arguments = bundle
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, searchMealFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupRecyclerView() {
        mealAdapter = MealAdapter(
            onDeleteClick = { position ->
                val meals = dataManager.getMealsForDate(currentDate).toMutableList()
                meals.removeAt(position)
                dataManager.saveMealsForDate(currentDate, meals)
                loadMeals()
            }
        )
        binding.recyclerViewMeals.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mealAdapter
        }
    }

    private fun setupAddButton() {
        binding.buttonAddMeal.setOnClickListener {
            val name = binding.editTextMealName.text.toString()
            try {
                val carbs = binding.editTextCarbs.text.toString().toFloat()
                val proteins = binding.editTextProteins.text.toString().toFloat()
                val fats = binding.editTextFats.text.toString().toFloat()
                val calories = binding.editTextCalories.text.toString().toFloat()

                if (name.isNotEmpty()) {
                    val meal = Meal(name, carbs, proteins, fats, calories)
                    val meals = dataManager.getMealsForDate(currentDate).toMutableList()
                    meals.add(meal)
                    dataManager.saveMealsForDate(currentDate, meals)

                    // Wyczyść pola
                    binding.editTextMealName.text.clear()
                    binding.editTextCarbs.text.clear()
                    binding.editTextProteins.text.clear()
                    binding.editTextFats.text.clear()
                    binding.editTextCalories.text.clear()
                    loadMeals()
                } else {
                    Toast.makeText(context, "Wprowadź nazwę posiłku", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Wprowadź poprawne wartości", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMeals() {
        val meals = dataManager.getMealsForDate(currentDate)
        mealAdapter.updateMeals(meals)
    }
}