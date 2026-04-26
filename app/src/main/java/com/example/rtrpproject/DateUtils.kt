package com.example.rtrpproject

import java.util.Calendar

fun getLast7Days(): List<String> {
    val calendar = Calendar.getInstance()
    val dates = mutableListOf<String>()

    for (i in 6 downTo 0) {
        val tempCalendar = calendar.clone() as Calendar
        tempCalendar.add(Calendar.DAY_OF_MONTH, -i)

        val year = tempCalendar.get(Calendar.YEAR)
        val month = tempCalendar.get(Calendar.MONTH) + 1
        val day = tempCalendar.get(Calendar.DAY_OF_MONTH)

        dates.add(String.format("%04d-%02d-%02d", year, month, day))
    }

    return dates
}