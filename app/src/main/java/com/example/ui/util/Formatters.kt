package com.example.ui.util

import java.text.NumberFormat
import java.util.Locale

private val indianLocale = Locale("en", "IN")

fun formatInr(amount: Long): String {
    val formatter = NumberFormat.getNumberInstance(indianLocale)
    return "₹${formatter.format(amount)}"
}
