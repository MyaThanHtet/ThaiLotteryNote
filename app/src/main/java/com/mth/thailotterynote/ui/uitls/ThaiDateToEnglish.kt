package com.mth.thailotterynote.ui.uitls

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun convertThaiDateToEnglish(thaiDate: String): String {
    // Step 1: Define a SimpleDateFormat for the Thai date
    val thaiFormat = SimpleDateFormat("d MMMM yyyy", Locale("th", "TH"))
    val englishFormat = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)

    // Step 2: Parse the input Thai date string into a Date object
    val date = thaiFormat.parse(thaiDate) ?: return "Invalid date"

    // Step 3: Convert the Buddhist year (2568) to Gregorian year (2025)
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.YEAR, -543) // Convert from BE to AD

    // Step 4: Format it back to English
    return englishFormat.format(calendar.time)
}