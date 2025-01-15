package com.example.dietmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dietmaker.api.NutritionixClient
import com.example.dietmaker.databinding.FragmentSearchMealBinding
import com.example.dietmaker.utils.DataManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import com.example.dietmaker.api.FoodQuery
import com.example.dietmaker.api.NutritionixFood
import com.example.dietmaker.adapters.SearchResultAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dietmaker.utils.Meal
import java.time.LocalDate
import java.time.ZoneId


class SearchMealFragment : Fragment() {
    private lateinit var binding: FragmentSearchMealBinding
    private lateinit var dataManager: DataManager
    private var searchJob: Job? = null
    private lateinit var searchAdapter: SearchResultAdapter
    private var currentDate: Date = Date()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMealBinding.inflate(inflater, container, false)
        dataManager = DataManager(requireContext())
        currentDate = arguments?.getString("date")?.let {
            Date.from(LocalDate.parse(it).atStartOfDay(ZoneId.systemDefault()).toInstant())
        } ?: Date()


        setupRecyclerView()
        setupSearchField()
        setupBackButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchResultAdapter { food ->
            val meal = Meal(
                name = food.food_name,
                carbohydrates = food.nf_total_carbohydrate.toFloat(),
                proteins = food.nf_protein.toFloat(),
                fats = food.nf_total_fat.toFloat(),
                calories = food.nf_calories.toFloat()
            )
            val currentMeals = dataManager.getMealsForDate(currentDate).toMutableList()
            currentMeals.add(meal)
            dataManager.saveMealsForDate(currentDate, currentMeals)

            // Wróć do poprzedniego fragmentu
            parentFragmentManager.popBackStack()
        }

        binding.searchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


    private fun setupSearchField() {
        binding.searchInput.addTextChangedListener { editable ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500) // Debounce search
                editable?.toString()?.let { query ->
                    if (query.length >= 3) {
                        showLoading()
                        searchFood(query)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.searchResults.visibility = View.GONE
        binding.noResultsText.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun showNoResults() {
        binding.noResultsText.visibility = View.VISIBLE
        binding.searchResults.visibility = View.GONE
    }

    private fun showResults() {
        binding.noResultsText.visibility = View.GONE
        binding.searchResults.visibility = View.VISIBLE
    }

    private suspend fun searchFood(query: String) {
        try {
            val response = NutritionixClient.api.searchFood(FoodQuery(query))
            hideLoading()

            if (response.isSuccessful) {
                val foods = response.body()?.foods ?: emptyList()
                if (foods.isEmpty()) {
                    showNoResults()
                } else {
                    showResults()
                    displaySearchResults(foods)
                }
            } else {
                showNoResults()
                Toast.makeText(context, "Błąd wyszukiwania: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            hideLoading()
            showNoResults()
            Toast.makeText(context, "Błąd połączenia: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displaySearchResults(foods: List<NutritionixFood>) {
        searchAdapter.updateResults(foods)
    }
}