package com.jodi.companioncompatibility.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import androidx.databinding.ObservableBoolean
import com.jodi.companioncompatibility.data.pref.JodiPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {
    private val mIsLoading = ObservableBoolean()
    private var mNavigator: WeakReference<N>? = null
    val pref by lazy { JodiPreferences() }

    override fun onCleared() {
        clearNavigator()
        super.onCleared()
    }

    private fun clearNavigator() {
        this.mNavigator?.clear()
        this.mNavigator = null
    }

    fun getIsLoading(): ObservableBoolean {
        return mIsLoading
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }

    fun getNavigator(): N? {
        return mNavigator?.get()
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }

    fun execute(dispatcher: CoroutineContext = Dispatchers.Main, job: suspend () -> Unit) =
        viewModelScope.launch(dispatcher) {
            job.invoke()
        }


    fun ignoreCoroutineException(throwable: Throwable, callback: () -> Unit) {
        if (throwable.message?.contains("Standalone") != true)
            callback.invoke()
    }

    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }

}
