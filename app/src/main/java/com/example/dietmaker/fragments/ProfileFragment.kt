// Lokalizacja: app/src/main/java/com/example/dietmaker/fragments/ProfileFragment.kt
package com.example.dietmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dietmaker.databinding.FragmentProfileBinding
import com.example.dietmaker.utils.DataManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var dataManager: DataManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        dataManager = DataManager(requireContext())

        loadUserProfile()
        setupSaveButton()

        return binding.root
    }

    private fun loadUserProfile() {
        val profile = dataManager.getUserProfile()

        binding.editTextName.setText(profile.name)
        binding.editTextCarbs.setText(profile.carbGoal.toString())
        binding.editTextProteins.setText(profile.proteinGoal.toString())
        binding.editTextFats.setText(profile.fatGoal.toString())
        binding.editTextCalories.setText(profile.calorieGoal.toString())
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            try {
                val name = binding.editTextName.text.toString()
                val carbs = binding.editTextCarbs.text.toString().toFloat()
                val proteins = binding.editTextProteins.text.toString().toFloat()
                val fats = binding.editTextFats.text.toString().toFloat()
                val calories = binding.editTextCalories.text.toString().toFloat()

                dataManager.saveUserProfile(name, carbs, proteins, fats, calories)
                Toast.makeText(context, "Profil zapisany", Toast.LENGTH_SHORT).show()

                // Powrót do poprzedniego fragmentu
                parentFragmentManager.popBackStack()
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Wprowadź poprawne wartości", Toast.LENGTH_SHORT).show()
            }
        }
    }
}