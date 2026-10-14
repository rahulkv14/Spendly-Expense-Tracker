package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CurrencyUtils {

    /**
     * Formats an amount into authentic Indian Rupee format with Lakhs/Crores grouping:
     * e.g. 148250 -> "₹1,48,250", 640 -> "₹640.00"
     */
    fun formatInr(amount: Double, includeDecimals: Boolean = false): String {
        val isNegative = amount < 0
        val absAmount = kotlin.math.abs(amount)
        val longVal = absAmount.toLong()
        val str = longVal.toString()

        val formattedInt = if (str.length <= 3) {
            str
        } else {
            val lastThree = str.substring(str.length - 3)
            val rest = str.substring(0, str.length - 3)
            val sb = StringBuilder()
            var count = 0
            for (i in rest.length - 1 downTo 0) {
                sb.append(rest[i])
                count++
                if (count == 2 && i > 0) {
                    sb.append(',')
                    count = 0
                }
            }
            sb.reverse().toString() + "," + lastThree
        }

        val prefix = if (isNegative) "-₹" else "₹"
        return if (includeDecimals) {
            val decimalPart = String.format(Locale.US, "%.2f", absAmount).substringAfter('.')
            "$prefix$formattedInt.$decimalPart"
        } else {
            "$prefix$formattedInt"
        }
    }

    /**
     * Compact Indian format e.g. "₹4.8k", "₹12.1k", "₹1.4L"
     */
    fun formatCompactInr(amount: Double): String {
        val absAmount = kotlin.math.abs(amount)
        return when {
            absAmount >= 100_000 -> String.format(Locale.US, "₹%.1fL", absAmount / 100_000)
            absAmount >= 1_000 -> String.format(Locale.US, "₹%.1fk", absAmount / 1_000)
            else -> formatInr(amount)
        }
    }
}

object DateTimeUtils {
    private val timeFormat = SimpleDateFormat("h:mm a", Locale.ENGLISH)
    private val dateFormat = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
    private val fullDateTimeFormat = SimpleDateFormat("d MMM yyyy · hh:mm a", Locale.ENGLISH)

    fun formatTime(millis: Long): String = timeFormat.format(Date(millis))

    fun formatDate(millis: Long): String = dateFormat.format(Date(millis))

    fun formatFullDateTime(millis: Long): String = fullDateTimeFormat.format(Date(millis))

    fun getDayGroupingHeader(millis: Long): String {
        val nowCal = Calendar.getInstance()
        val itemCal = Calendar.getInstance().apply { timeInMillis = millis }

        val isToday = nowCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                nowCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)

        nowCal.add(Calendar.DAY_OF_YEAR, -1)
        val isYesterday = nowCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                nowCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)

        return when {
            isToday -> {
                val day = SimpleDateFormat("d MMM", Locale.ENGLISH).format(Date(millis)).uppercase()
                "TODAY · $day"
            }
            isYesterday -> {
                val day = SimpleDateFormat("d MMM", Locale.ENGLISH).format(Date(millis)).uppercase()
                "YESTERDAY · $day"
            }
            else -> {
                SimpleDateFormat("d MMM yyyy", Locale.ENGLISH).format(Date(millis)).uppercase()
            }
        }
    }
}
