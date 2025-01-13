// app/src/main/java/com/example/dietmaker/api/NutritionixClient.kt
package com.example.dietmaker.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NutritionixClient {
    private const val BASE_URL = "https://trackapi.nutritionix.com/v2/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: NutritionixApi = retrofit.create(NutritionixApi::class.java)
}