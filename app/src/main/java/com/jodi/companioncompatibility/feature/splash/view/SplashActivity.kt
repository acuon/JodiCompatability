package com.jodi.companioncompatibility.feature.splash.view

import android.os.Bundle
import android.view.View
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.BR
import com.jodi.companioncompatibility.base.BaseActivity
import com.jodi.companioncompatibility.databinding.ActivitySplashBinding
import com.jodi.companioncompatibility.feature.home.view.HomeActivity
import com.jodi.companioncompatibility.feature.splash.NavigationSplash
import com.jodi.companioncompatibility.feature.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), NavigationSplash {
    override fun getBindingVariable() = BR.splashvm

    override fun getLayoutId() = R.layout.activity_splash

    override fun onViewClicked(view: View?) {
    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setNavigator(this)
        binding = getViewDataBinding()
    }

    override fun openHomeActivity() {
        HomeActivity.present(this)
        finish()
    }
}