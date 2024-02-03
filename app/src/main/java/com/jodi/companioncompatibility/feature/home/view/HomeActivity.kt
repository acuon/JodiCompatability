package com.jodi.companioncompatibility.feature.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.BR
import com.jodi.companioncompatibility.base.BaseActivity
import com.jodi.companioncompatibility.databinding.ActivityHomeBinding
import com.jodi.companioncompatibility.feature.home.NavigationHome
import com.jodi.companioncompatibility.feature.home.viewmodel.HomeViewModel
import com.jodi.companioncompatibility.feature.result.view.ResultsPageActivity
import com.jodi.companioncompatibility.utils.dialogs.CustomDatePickerDialog
import com.jodi.companioncompatibility.utils.dialogs.CustomTimePickerDialog
import com.jodi.companioncompatibility.utils.extensions.getDrawableRes
import com.jodi.companioncompatibility.utils.extensions.getMyColor
import com.jodi.companioncompatibility.utils.extensions.hideSoftKeyboard
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
            etName.addTextChangedListener {
                viewModel.userName.set(it.toString())
                setButton(viewModel.getButtonVisibility())
            }
            etPartnerName.addTextChangedListener {
                viewModel.partnerName.set(it.toString())
                setButton(viewModel.getButtonVisibility())
            }
            etDob.setOnClickListener {
                CustomDatePickerDialog(this@HomeActivity, this@HomeActivity) {
                    etDob.text = it
                    viewModel.userDob.set(it)
                    setButton(viewModel.getButtonVisibility())
                }.show()
            }
            etPartnersDob.setOnClickListener {
                CustomDatePickerDialog(this@HomeActivity, this@HomeActivity) {
                    etPartnersDob.text = it
                    viewModel.partnerDob.set(it)
                    setButton(viewModel.getButtonVisibility())
                }.show()
            }
            etTob.setOnClickListener {
                CustomTimePickerDialog(this@HomeActivity, this@HomeActivity) {
                    etTob.text = it
                    viewModel.userTob.set(it)
                    setButton(viewModel.getButtonVisibility())
                }.show()
            }
            etPartnersTob.setOnClickListener {
                CustomTimePickerDialog(this@HomeActivity, this@HomeActivity) {
                    etPartnersTob.text = it
                    viewModel.partnerTob.set(it)
                    setButton(viewModel.getButtonVisibility())
                }.show()
            }
            etPlaceOfBirth.addTextChangedListener {
                viewModel.userPob.set(it.toString())
                setButton(viewModel.getButtonVisibility())
            }
            etPartnerPlaceOfBirth.addTextChangedListener {
                viewModel.partnerPob.set(it.toString())
                setButton(viewModel.getButtonVisibility())
            }
        }
    }

    private fun setButton(isChecked: Boolean) {
        if (isChecked) {
            binding.btn.apply {
                setTextColor(context.getMyColor(R.color.white))
                backgroundTintList =
                    ContextCompat.getColorStateList(this@HomeActivity, R.color.app_color_orange)
            }
        } else {
            binding.btn.apply {
                setTextColor(context.getMyColor(R.color.dark_grey))
                backgroundTintList =
                    ContextCompat.getColorStateList(this@HomeActivity, R.color.light_orange)
            }
        }
    }

    override fun onViewClicked(view: View?) {

    }

    private fun clearData() {
        viewModel.userName.set(null)
        viewModel.userDob.set(null)
        viewModel.userTob.set(null)
        viewModel.userPob.set(null)
        viewModel.partnerName.set(null)
        viewModel.partnerDob.set(null)
        viewModel.partnerTob.set(null)
        viewModel.partnerPob.set(null)
        binding.apply {
            etName.setText("")
            etPartnerName.setText("")
            etPlaceOfBirth.setText("")
            etPartnerPlaceOfBirth.setText("")
            etDob.text = ""
            etPartnersDob.text = ""
            etTob.text = ""
            etPartnersTob.text = ""
        }
        setButton(viewModel.getButtonVisibility())
    }

    override fun callGpt() {
        hideSoftKeyboard()
        viewModel.chatCompletion().observe(this) {
            when (it) {
                is ResultOf.Success -> {
                    clearData()
                    ResultsPageActivity.present(this, it.value)
                }

                else -> {}
            }
        }
//        viewModel._chatCompletion()
    }
}