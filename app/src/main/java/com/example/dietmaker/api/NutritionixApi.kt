// app/src/main/java/com/example/dietmaker/api/NutritionixApi.kt
package com.example.dietmaker.api

import com.example.dietmaker.api.NutritionixResponse
import com.example.dietmaker.api.FoodQuery
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Body

/*
interface NutritionixApi {
    @Headers(
        "x-app-id: aa6ed47d",
        "x-app-key: 3e9fcbb613cce2df9e19f641d4630c31"
    )
    @GET("search/instant")
    suspend fun searchFood(
        @Query("query") query: String,
        @Query("detailed") detailed: Boolean = true
    ): Response<NutritionixResponse>
}*/

/*
interface NutritionixApi {
    @Headers(
        "x-app-id: aa6ed47d",
        "x-app-key: 3e9fcbb613cce2df9e19f641d4630c31"
    )
    @GET("search/instant")
    suspend fun searchFood(
        @Query("query") query: String,
        @Query("detailed") detailed: Boolean = true,
        @Query("branded") branded: Boolean = false
    ): Response<NutritionixResponse>
}*/

interface NutritionixApi {
    @Headers(
        "x-app-id: aa6ed47d",
        "x-app-key: 3e9fcbb613cce2df9e19f641d4630c31"
    )
    @POST("natural/nutrients")
    suspend fun searchFood(
        @Body query: FoodQuery
    ): Response<NutritionixResponse>
}
