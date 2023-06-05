package com.example.projectomoviles.calendario

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

open class calendario_utils {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        var selectedDate: LocalDate = LocalDate.now()
        @RequiresApi(Build.VERSION_CODES.O)
        open fun formattedDate(date: LocalDate): String? {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return date.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        open fun formattedTime(time: LocalTime): String? {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formattedShortTime(time: LocalTime): String? {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            return time.format(formatter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun monthYearFromDate(date: LocalDate): String? {
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }



        @RequiresApi(Build.VERSION_CODES.O)
        fun daysInMonthArray(): ArrayList<LocalDate>? {
            val daysInMonthArray: ArrayList<LocalDate> = ArrayList()
            val yearMonth: YearMonth = YearMonth.from(selectedDate)
            val daysInMonth: Int = yearMonth.lengthOfMonth()
            val prevMonth = selectedDate.minusMonths(1)
            val nextMonth = selectedDate.plusMonths(1)
            val prevYearMonth: YearMonth = YearMonth.from(prevMonth)
            val prevDaysInMonth: Int = prevYearMonth.lengthOfMonth()
            val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
            val dayOfWeek = firstOfMonth.dayOfWeek.value
            for (i in 1..42) {
                if (i <= dayOfWeek) daysInMonthArray.add(
                    LocalDate.of(
                        prevMonth.year,
                        prevMonth.month,
                        prevDaysInMonth + i - dayOfWeek
                    )
                ) else if (i > daysInMonth + dayOfWeek) daysInMonthArray.add(
                    LocalDate.of(nextMonth.year, nextMonth.month, i - dayOfWeek - daysInMonth)
                ) else daysInMonthArray.add(
                    LocalDate.of(selectedDate.year, selectedDate.month, i - dayOfWeek)
                )
            }
            return daysInMonthArray
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate?>? {
            val days: ArrayList<LocalDate?> = ArrayList()
            var current = sundayForDate(selectedDate)
            val endDate = current!!.plusWeeks(1)
            while (current!!.isBefore(endDate)) {
                days.add(current)
                current = current.plusDays(1)
            }
            return days
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun sundayForDate(current: LocalDate): LocalDate? {
            var current = current
            val oneWeekAgo = current.minusWeeks(1)
            while (current.isAfter(oneWeekAgo)) {
                if (current.dayOfWeek == DayOfWeek.SUNDAY) return current
                current = current.minusDays(1)
            }
            return null
        }
        @RequiresApi(Build.VERSION_CODES.O)
        open fun monthDayFromDate(date: LocalDate): String? {
            val formatter = DateTimeFormatter.ofPattern("MMMM dd")
            return date.format(formatter)
        }
    }


}