// app/src/main/java/com/example/dietmaker/adapters/SearchResultAdapter.kt
package com.example.dietmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dietmaker.api.NutritionixFood
import com.example.dietmaker.databinding.ItemSearchResultBinding

class SearchResultAdapter(
    private val onFoodClick: (NutritionixFood) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private var foods: List<NutritionixFood> = listOf()

    inner class SearchResultViewHolder(
        private val binding: ItemSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(food: NutritionixFood) {
            binding.foodName.text = food.food_name
            binding.foodDetails.text = String.format(
                "Kalorie: %.0f, W: %.1fg, B: %.1fg, T: %.1fg",
                food.nf_calories,
                food.nf_total_carbohydrate,
                food.nf_protein,
                food.nf_total_fat
            )
            binding.root.setOnClickListener { onFoodClick(food) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    override fun getItemCount() = foods.size

    fun updateResults(newFoods: List<NutritionixFood>) {
        foods = newFoods
        notifyDataSetChanged()
    }
}