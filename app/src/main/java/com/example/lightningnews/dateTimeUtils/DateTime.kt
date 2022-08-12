package com.example.lightningnews.dateTimeUtils


import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTime {

    const val DefaultFormatPattern = "dd.MM.yyyy"
    const val DateTimeNV2 = "dd/MM/yyyy HH:mm"
    const val IsoFormatPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    const val DateTimeFormatPattern1 = "yyyy-MM-dd'T'HH:mm:ss"


    fun convertDateString(
            date: String,
            fromFormatPattern: String,
            toFormatPattern: String = DateTimeNV2
    ): String {
        val c = getCalendarFromDateString(date, fromFormatPattern)
        return getDateStringFromCalendar(c, toFormatPattern)
    }

    fun getCalendarInstance(): Calendar {
        return Calendar.getInstance(getLocale())
    }

    private fun getLocale() = Locale.getDefault()

    fun getCalendarFromDateString(
            dateTime: String,
            formatPattern: String = DateTimeNV2
    ): Calendar {
        val cal = getCalendarInstance()

        try {
            val df = SimpleDateFormat(formatPattern, getLocale())
            val date = df.parse(dateTime)
            cal.time = date
        } catch (e: ParseException) {
        }

        return cal
    }

    fun getDateStringFromCalendar(
            c: Calendar,
            formatPattern: String = DateTimeNV2
    ): String {
        val df = SimpleDateFormat(formatPattern, getLocale())
        val date = c.time
        return df.format(date)
    }
}