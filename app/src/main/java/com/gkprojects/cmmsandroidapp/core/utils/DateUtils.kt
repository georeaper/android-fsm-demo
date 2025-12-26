package com.gkprojects.cmmsandroidapp.core.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val STANDARD_FORMAT = "dd/MM/yyyy HH:mm:ss"
    private val standardFormatter by lazy { SimpleDateFormat(STANDARD_FORMAT, Locale.getDefault()) }

    /**
     * Format a Date object to the standard format
     */
    fun format(date: Date): String = standardFormatter.format(date)

    /**
     * Parse a string using the standard format
     */
    fun parse(dateString: String): Date? {
        return try {
            standardFormatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Normalize almost any reasonable date string into the standard format.
     * Accepts strings with:
     * - single/double digit day/month
     * - optional time (HH:mm or HH:mm:ss)
     * - different separators: / or -
     */
    fun normalize(dateString: String): String? {
        // Remove extra spaces and standardize separators
        val cleaned = dateString.trim().replace("-", "/").replace("\\s+".toRegex(), " ")

        // Split into date and optional time
        val parts = cleaned.split(" ")
        val datePart = parts.getOrNull(0) ?: return null
        val timePart = parts.getOrNull(1) ?: "00:00:00"

        // Normalize day/month/year
        val dateComponents = datePart.split("/").map { it.padStart(2, '0') }
        if (dateComponents.size != 3) return null
        val day = dateComponents[0]
        val month = dateComponents[1]
        val year = dateComponents[2]

        // Normalize time (HH:mm:ss)
        val timeComponents = timePart.split(":").map { it.padStart(2, '0') }
        val hour = timeComponents.getOrElse(0) { "00" }
        val minute = timeComponents.getOrElse(1) { "00" }
        val second = timeComponents.getOrElse(2) { "00" }

        val normalizedString = "$day/$month/$year $hour:$minute:$second"

        // Validate by parsing
        return try {
            val date = standardFormatter.parse(normalizedString)
            standardFormatter.format(date)
        } catch (_: ParseException) {
            null
        }
    }
}
