package fr.phlab.notesrapides.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

    fun String.fromUiToDate(locate: Locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?: Locale.getDefault()): Date? {
        return DateFormat.getDateInstance(DateFormat.SHORT, locate).parse(this)
    }

    fun Date.toBddString(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(this)
    }

    fun String.fromBddToDate(): Date? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.parse(this)
    }


    @SuppressLint("SimpleDateFormat")
    fun Date.toUiString(locate: Locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]?: Locale.getDefault()): String {
        return DateFormat.getDateInstance(DateFormat.SHORT, locate).format(this)
    }
