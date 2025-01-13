// Lokalizacja: app/src/main/java/com/example/dietmaker/MainActivity.kt
package com.example.dietmaker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dietmaker.databinding.ActivityMainBinding
import com.example.dietmaker.fragments.MainDashboardFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        try {
            println("Rozpoczynam onCreate")
            binding = ActivityMainBinding.inflate(layoutInflater)
            println("Binding załadowany")
            setContentView(binding.root)
            println("Layout ustawiony")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Domyślnie ładujemy główny fragment dashboardu
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainDashboardFragment())
                .commit()
            //binding.fragmentContainer.setBackgroundColor(Color.RED) // Testowy widok

        }
    }


    // Metoda do podmiany fragmentów
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}