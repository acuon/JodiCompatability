package com.jodi.companioncompatibility.feature.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.BR
import com.jodi.companioncompatibility.base.BaseActivity
import com.jodi.companioncompatibility.databinding.ActivityHomeBinding
import com.jodi.companioncompatibility.feature.home.NavigationHome
import com.jodi.companioncompatibility.feature.home.viewmodel.HomeViewModel
import com.jodi.companioncompatibility.utils.network.ResultOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), NavigationHome {

    companion object {
        fun present(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

    override fun getBindingVariable() = BR.vm
    override fun getLayoutId() = R.layout.activity_home
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        viewModel.setNavigator(this)
        setupView()
    }

    private fun setupView() {
        binding.apply {
        }
    }

    override fun onViewClicked(view: View?) {

    }

    override fun callGpt() {
//        viewModel.chatCompletion().observe(this) {
//            when (it) {
//                is ResultOf.Success -> {}
//                else -> {}
//            }
//        }
        viewModel._chatCompletion()
    }
}