package fr.phlab.notesrapides.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class StringUtilsTest {

    @Test
    fun fromUiToDate() {
        var dateString = "12/11/2023"
        var date = dateString.fromUiToDate(Locale.FRENCH)
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = date!!
        assertEquals(2023, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.NOVEMBER, calendar.get(Calendar.MONTH))
        assertEquals(12, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), calendar.timeZone)

        dateString = "12/11/2023"
        date = dateString.fromUiToDate(Locale.ENGLISH)
        calendar = Calendar.getInstance()
        calendar.time = date!!
        assertEquals(2023, calendar.get(Calendar.YEAR))
        assertEquals(Calendar.DECEMBER, calendar.get(Calendar.MONTH))
        assertEquals(11, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), calendar.timeZone)
    }

    @Test
    fun toUiString() {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Paris")
        calendar.set(2023, Calendar.NOVEMBER, 12)
        val date = calendar.time

        var dateString = date.toUiString(Locale.FRENCH)
        assertEquals("12/11/2023", dateString)
        dateString = date.toUiString(Locale.ENGLISH)
        assertEquals("11/12/23", dateString)
    }

    @Test
    fun toBddString() {
        var calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Paris")
        calendar.set(2023, Calendar.NOVEMBER, 12)
        assertEquals ( "12/11/2023",calendar.time.toBddString())

        calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("America/New_York")
        calendar.set(2023, Calendar.NOVEMBER, 12)
        assertEquals ( "12/11/2023",calendar.time.toBddString())


        calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")
        calendar.set(2023, Calendar.NOVEMBER, 12)
        assertEquals ( "12/11/2023",calendar.time.toBddString())
    }

    @Test
    fun fromBddToDate() {
        var dateString = "12/11/2023"


        var calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("Europe/Paris")
        calendar.set(2023, Calendar.NOVEMBER, 12)

        var calendarEnd = Calendar.getInstance()
        calendarEnd.time = dateString.fromBddToDate()!!

        assertEquals(calendar.get(Calendar.YEAR),  calendarEnd.get(Calendar.YEAR))
        assertEquals(calendar.get(Calendar.MONTH),  calendarEnd.get(Calendar.MONTH))
        assertEquals(calendar.get(Calendar.DAY_OF_YEAR),  calendarEnd.get(Calendar.DAY_OF_YEAR))

        calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("America/New_York")
        calendar.set(2023, Calendar.NOVEMBER, 12)

        calendarEnd = Calendar.getInstance()
        calendarEnd.time = dateString.fromBddToDate()!!

        assertEquals(calendar.get(Calendar.YEAR),  calendarEnd.get(Calendar.YEAR))
        assertEquals(calendar.get(Calendar.MONTH),  calendarEnd.get(Calendar.MONTH))
        assertEquals(calendar.get(Calendar.DAY_OF_YEAR),  calendarEnd.get(Calendar.DAY_OF_YEAR))


    }

}