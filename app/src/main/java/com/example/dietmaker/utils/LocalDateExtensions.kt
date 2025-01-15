package com.example.dietmaker.utils

import java.time.LocalDate
import kotlinx.datetime.LocalDate as KxLocalDate

fun KxLocalDate.toJavaLocalDate(): LocalDate {
    return LocalDate.of(year, monthNumber, dayOfMonth)
}