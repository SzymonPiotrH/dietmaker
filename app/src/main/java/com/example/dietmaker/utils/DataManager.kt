package com.example.dietmaker.utils

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

// Model pojedynczego posiłku
data class Meal(
    val name: String,
    val carbohydrates: Float,
    val proteins: Float,
    val fats: Float,
    val calories: Float
)

// Klasa do zarządzania danymi przy pomocy SharedPreferences
class DataManager(private val context: Context) {
    private val sharedPrefs = context.getSharedPreferences("DietMakerPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    //!!! experimental
//    private val _mealsData = MutableLiveData<List<Meal>>()
//    val mealsData: LiveData<List<Meal>> = _mealsData
//!!!
    // Formatowanie daty jako klucza
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Zapis posiłków dla konkretnej daty
    fun saveMealsForDate(date: Date, meals: List<Meal>) {
        val dateKey = dateFormat.format(date)
        val mealsJson = gson.toJson(meals)
        sharedPrefs.edit().putString(dateKey, mealsJson).apply()
    }

    // Odczyt posiłków dla konkretnej daty
    fun getMealsForDate(date: Date): List<Meal> {
        val dateKey = dateFormat.format(date)
        val mealsJson = sharedPrefs.getString(dateKey, null)
        return if (mealsJson != null) {
            val type = object : TypeToken<List<Meal>>() {}.type
            gson.fromJson(mealsJson, type)
        } else {
            emptyList()
        }
    }
    // Odczyt posiłków dla konkretnej daty
    fun getMealsForDate(localDate: LocalDate): List<Meal> {
        val date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return getMealsForDate(date) // Wywołanie istniejącej metody
    }


    // Zapis informacji o użytkowniku
    fun saveUserProfile(name: String, carbGoal: Float, proteinGoal: Float, fatGoal: Float, calorieGoal: Float) {
        sharedPrefs.edit().apply {
            putString("userName", name)
            putFloat("carbGoal", carbGoal)
            putFloat("proteinGoal", proteinGoal)
            putFloat("fatGoal", fatGoal)
            putFloat("calorieGoal", calorieGoal)
        }.apply()
    }

    // Odczyt informacji o użytkowniku
    fun getUserProfile(): UserProfile {
        return UserProfile(
            name = sharedPrefs.getString("userName", "no name") ?: "no name",
            carbGoal = sharedPrefs.getFloat("carbGoal", 0f),
            proteinGoal = sharedPrefs.getFloat("proteinGoal", 0f),
            fatGoal = sharedPrefs.getFloat("fatGoal", 0f),
            calorieGoal = sharedPrefs.getFloat("calorieGoal", 0f)
        )
    }
}

// Model profilu użytkownika
data class UserProfile(
    val name: String,
    val carbGoal: Float,
    val proteinGoal: Float,
    val fatGoal: Float,
    val calorieGoal: Float
)