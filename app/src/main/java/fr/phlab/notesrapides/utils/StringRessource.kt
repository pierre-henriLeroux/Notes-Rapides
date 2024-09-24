package fr.phlab.notesrapides.utils

import android.content.Context

sealed interface StringResource {

    fun resolve(context: Context?): String

    fun isEmpty(): Boolean

    fun isNotEmpty(): Boolean = !isEmpty()

    data class ResId(val resId: Int) : StringResource {
        override fun resolve(context: Context?): String {
            return context?.getString(resId) ?: ""
        }

        override fun isEmpty(): Boolean = false
    }

    data class Formatted(val resId: Int, val args: List<Any>) : StringResource {
        override fun resolve(context: Context?): String {
            return context?.getString(resId, *args.toTypedArray()) ?: ""
        }

        override fun isEmpty(): Boolean = false
    }

    data class Text(val text: String) : StringResource {
        override fun resolve(context: Context?): String {
            return text
        }

        override fun isEmpty(): Boolean {
            return text.isEmpty()
        }
    }
}