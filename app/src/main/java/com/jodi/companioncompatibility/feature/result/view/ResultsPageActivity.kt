package com.jodi.companioncompatibility.feature.result.view


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jodi.companioncompatibility.R
import com.jodi.companioncompatibility.BR
import com.jodi.companioncompatibility.base.BaseActivity
import com.jodi.companioncompatibility.base.BaseNavigation
import com.jodi.companioncompatibility.databinding.ActivityResultsPageBinding
import com.jodi.companioncompatibility.feature.home.model.GptResponse
import com.jodi.companioncompatibility.feature.result.viewmodel.ResultsViewModel
import com.jodi.companioncompatibility.utils.extensions.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultsPageActivity : BaseActivity<ActivityResultsPageBinding, ResultsViewModel>(),
    BaseNavigation {

    companion object {
        fun present(context: Context, gptResponse: GptResponse?) {
            val intent = Intent(context, ResultsPageActivity::class.java)
            intent.putExtra(GPT_RESPONSE_KEY, gptResponse)
            context.startActivity(intent)
        }

        private const val GPT_RESPONSE_KEY = "gpt_response_key"
    }

    override fun getBindingVariable() = BR.vm
    override fun getLayoutId() = R.layout.activity_results_page
    private lateinit var binding: ActivityResultsPageBinding

    private val gptResponse by lazy { intent?.parcelable<GptResponse>(GPT_RESPONSE_KEY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        viewModel.setNavigator(this)
        gptResponse?.choices?.get(0).let { choice ->
            viewModel.content.set(choice?.message?.content)
        }
        binding.apply {
            ivBack.setOnClickListener { finish() }
        }
    }

    override fun onViewClicked(view: View?) {

    }

}