package com.jodi.companioncompatibility.feature.result.viewmodel

import androidx.databinding.ObservableField
import com.jodi.companioncompatibility.base.BaseNavigation
import com.jodi.companioncompatibility.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor() :
    BaseViewModel<BaseNavigation>() {

    val content = ObservableField<String?>()
}