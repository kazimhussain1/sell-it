package com.example.sellit.utilities

import android.annotation.SuppressLint
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat;
import java.util.*

class DateUtils {
    companion object {

        const val YYYY_MM_DD = "yyyy-MM-dd"
        const val DD_MMMM_YYYY = "dd MMMM yyyy"
        const val YYYY = "yyyy"


        const val TIME_HH_MM_SS = "HH:mm:ss"
        const val TIME_HH_MM_A = "hh:mm a"

        fun getDateString(date: Date, format: String): String =
            SimpleDateFormat(format, Locale.getDefault()).format(date)

        @SuppressLint("SimpleDateFormat")
        fun getDateString(inputString: String, inputFormat: String, outputFormat: String): String {

            return try {
                val prevDateFormat = SimpleDateFormat(inputFormat)
                val newDateFormat = SimpleDateFormat(outputFormat)

                val date = prevDateFormat.parse(inputString)
                if (date != null) newDateFormat.format(date) else ""

            } catch (e: Exception) {
                ""
            }

        }

        fun getCurrentDate(format: String): String =
            SimpleDateFormat(format, Locale.getDefault()).format(Date())

        fun getDate(inputString: String, inputFormat: String) = try {
            SimpleDateFormat(inputFormat, Locale.getDefault()).parse(inputString)
        } catch (exception: ParseException) {
            Date()
        }


        fun getHoursMinutesSeconds(datePast: Date, dateFuture: Date): Triple<Int, Int, Int> {

            var difference = dateFuture.time - datePast.time

            val hours = difference / 3600000
            difference %= 3600000
            val minutes = difference / 60000
            difference %= 60000
            val seconds = difference / 1000

            return Triple(hours.toInt(), minutes.toInt(), seconds.toInt())

        }
    }
}