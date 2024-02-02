package com.jodi.companioncompatibility.feature.splash.viewmodel

import com.jodi.companioncompatibility.base.BaseViewModel
import com.jodi.companioncompatibility.feature.splash.NavigationSplash
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel<NavigationSplash>() {

    init {
        startAnimation()
    }

    private fun startAnimation() {
        runDelayed(1900L) { openHomeActivity() }
    }

    private fun openHomeActivity() {
        getNavigator()?.openHomeActivity()
    }

}