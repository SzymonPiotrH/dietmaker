// Lokalizacja: app/src/main/java/com/example/dietmaker/adapters/MealAdapter.kt
package com.example.dietmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dietmaker.databinding.ItemMealBinding
import com.example.dietmaker.utils.Meal

class MealAdapter(
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    private var meals: List<Meal> = emptyList()

    fun updateMeals(newMeals: List<Meal>) {
        meals = newMeals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val binding = ItemMealBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun getItemCount() = meals.size

    inner class MealViewHolder(
        private val binding: ItemMealBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonDelete.setOnClickListener {
                onDeleteClick(adapterPosition)
            }
        }

        fun bind(meal: Meal) {
            binding.textViewMealName.text = meal.name
            binding.textViewMealDetails.text = buildString {
                append("Węglowodany: ${meal.carbohydrates}g\n")
                append("Białka: ${meal.proteins}g\n")
                append("Tłuszcze: ${meal.fats}g\n")
                append("Kalorie: ${meal.calories}")
            }
        }
    }
}