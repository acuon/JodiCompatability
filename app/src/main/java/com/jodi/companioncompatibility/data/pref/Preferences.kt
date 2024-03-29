package com.example.flavum.data.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

abstract class Preferences {

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * Initialize PrefDelegate with authView Context reference.
         *
         * **This method needs to be called before any other usage of PrefDelegate!!**
         */

        fun init(context: Context) {
            Companion.context = context
        }
    }

    private val prefs: SharedPreferences by lazy {
        if (context != null) context!!.getSharedPreferences(
            javaClass.simpleName,
            Context.MODE_PRIVATE
        )
        else throw IllegalStateException("Context was not initialized. Call Preferences.init(context) before using it")
    }

    private val listeners = mutableListOf<SharedPrefsListener>()

    abstract class PrefDelegate<T>(val prefKey: String?) {
        abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
        abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
    }

    interface SharedPrefsListener {
        fun onSharedPrefChanged(property: KProperty<*>)
    }

    /*fun addListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.add(sharedPrefsListener)
    }

    fun removeListener(sharedPrefsListener: SharedPrefsListener) {
        listeners.remove(sharedPrefsListener)
    }

    fun clearListeners() = listeners.clear()*/

    fun stringPref(prefKey: String? = null, defaultValue: String? = null) =
        StringPrefDelegate(prefKey, defaultValue)

    inner class StringPrefDelegate(prefKey: String? = null, private val defaultValue: String?) :
        PrefDelegate<String?>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): String? =
            prefs.getString(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
            prefs.edit().putString(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }


    fun intPref(prefKey: String? = null, defaultValue: Int = 0) =
        IntPrefDelegate(prefKey, defaultValue)

    inner class IntPrefDelegate(prefKey: String? = null, private val defaultValue: Int) :
        PrefDelegate<Int>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getInt(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
            prefs.edit().putInt(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }

    fun floatPref(prefKey: String? = null, defaultValue: Float = 0f) =
        FloatPrefDelegate(prefKey, defaultValue)

    inner class FloatPrefDelegate(prefKey: String? = null, private val defaultValue: Float) :
        PrefDelegate<Float>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getFloat(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Float) {
            prefs.edit().putFloat(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }


    fun booleanPref(prefKey: String? = null, defaultValue: Boolean = false) =
        BooleanPrefDelegate(prefKey, defaultValue)

    inner class BooleanPrefDelegate(prefKey: String? = null, private val defaultValue: Boolean) :
        PrefDelegate<Boolean>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getBoolean(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
            prefs.edit().putBoolean(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }


    fun longPref(prefKey: String? = null, defaultValue: Long = 0L) =
        LongPrefDelegate(prefKey, defaultValue)

    inner class LongPrefDelegate(prefKey: String? = null, private val defaultValue: Long) :
        PrefDelegate<Long>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>) =
            prefs.getLong(prefKey ?: property.name, defaultValue)

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
            prefs.edit().putLong(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }


    /*fun stringSetPref(prefKey: String? = null, defaultValue: Set<String> = HashSet()) = StringSetPrefDelegate(prefKey, defaultValue)

    inner class StringSetPrefDelegate(prefKey: String? = null, private val defaultValue: Set<String>) : PrefDelegate<Set<String>>(prefKey) {
        override fun getValue(thisRef: Any?, property: KProperty<*>): Set<String> = prefs.getStringSet(prefKey ?: property.name, defaultValue)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<String>) {
            prefs.edit().putStringSet(prefKey ?: property.name, value).apply()
            onPrefChanged(property)
        }
    }*/

    fun logoutUser() {

        //  prefs.edit().clear().apply()
    }

    private fun onPrefChanged(property: KProperty<*>) {
        listeners.forEach { it.onSharedPrefChanged(property) }
    }

}