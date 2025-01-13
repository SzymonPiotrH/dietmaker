// app/src/main/java/com/example/dietmaker/api/NutritionixModels.kt
package com.example.dietmaker.api

data class NutritionixResponse(
    val foods: List<NutritionixFood>
)

data class NutritionixFood(
    val food_name: String,
    val nf_calories: Double,
    val nf_total_carbohydrate: Double,
    val nf_protein: Double,
    val nf_total_fat: Double,
    val serving_qty: Double,
    val serving_unit: String,
    val serving_weight_grams: Double
)