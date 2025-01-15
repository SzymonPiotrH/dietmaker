package com.example.dietmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.dietmaker.R
import com.example.dietmaker.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*
import android.widget.LinearLayout
import android.widget.Button
import androidx.core.content.ContextCompat
import java.time.YearMonth


class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private lateinit var calendarLayout: LinearLayout
    private lateinit var tvCurrentMonth: TextView
    private lateinit var buttonPreviousMonth: Button
    private lateinit var buttonNextMonth: Button
    private val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.getDefault())
    private var currentMonth = YearMonth.now()
    private var selectedDate: LocalDate? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarLayout = binding.calendarLayout
        tvCurrentMonth = binding.tvCurrentMonth
        buttonPreviousMonth = binding.buttonPreviousMonth
        buttonNextMonth = binding.buttonNextMonth

        buttonPreviousMonth.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            updateCalendar()
        }
        buttonNextMonth.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            updateCalendar()
        }
        if(selectedDate == null){
            selectedDate = LocalDate.now()
        }
        updateCalendar()

        return binding.root
    }

    private fun updateCalendar() {
        calendarLayout.removeAllViews()
        tvCurrentMonth.text = currentMonth.toString()
        val firstDayOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
        var currentDay = 1 - (firstDayOfWeek % 7)

        for (row in 0..5) {
            val rowLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            for (col in 0..6) {
                val day = currentDay
                if (day > 0 && day <= daysInMonth) {
                    val dayTextView = TextView(requireContext()).apply {
                        text = day.toString()
                        textSize = 16f
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                        setPadding(0,16,0,16)

                        if(selectedDate == currentMonth.atDay(day)){
                            background = ContextCompat.getDrawable(requireContext(), R.drawable.selected_day_background)
                        }
                        setOnClickListener {
                            val newSelectedDate = currentMonth.atDay(day)
                            selectedDate = newSelectedDate
                            updateCalendar() // Odśwież kalendarz

                            setFragmentResult("selectedDate", Bundle().apply {
                                putString("date", newSelectedDate.toString())
                            })
                            // parentFragmentManager.popBackStack()
                        }
                    }
                    rowLayout.addView(dayTextView)
                }
                else{
                    val dayTextView = TextView(requireContext()).apply {
                        text = ""
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                    }
                    rowLayout.addView(dayTextView)
                }
                currentDay++

            }
            calendarLayout.addView(rowLayout)
        }
    }
}