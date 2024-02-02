package com.jodi.companioncompatibility.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.jodi.companioncompatibility.data.pref.JodiPreferences
import com.jodi.companioncompatibility.utils.extensions.hideSoftKeyboard
import com.jodi.companioncompatibility.utils.widgets.JodiSnackBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<*>> : Fragment() {

    private var mActivity: BaseActivity<*, *>? = null
    private var mRootView: View? = null
    val pref by lazy { JodiPreferences() }
    private var snackBar: JodiSnackBar? = null

    lateinit var mViewDataBinding: T

    //injecting the viewmodel
    @Inject
    lateinit var viewModel: V

    // Method for getting the binding variable - viewmodel reference
    abstract fun getBindingVariable(): Int

    // Fragment layout reference
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            mActivity = context
            context.onFragmentAttached()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root
        return mRootView
    }

    protected inline infix fun <T> Flow<T>.bindTo(crossinline action: (T) -> Unit) {
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    collect { action(it) }
                }
            }
        }
    }

    protected fun showSnackBar(message: String?, type: JodiSnackBar.SnackType) {
        mViewDataBinding.root.let {
            snackBar = JodiSnackBar
                .Builder()
                .type(type)
                .message(message)
                .setCallBack(snackListener)
                .make(it)
                .showSnack()
        }
    }

    private val snackListener = object : JodiSnackBar.Builder.ISnackListener {
        override fun onClosed(view: View) {
            snackBar?.dismiss()
        }
    }

    fun itemDecoration(value: Int): RecyclerView.ItemDecoration {
        return createItemDecorator(value, value, value, value)
    }

    fun itemDecoration(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ): RecyclerView.ItemDecoration {
        return createItemDecorator(top, bottom, left, right)
    }

    private fun createItemDecorator(
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                with(outRect) {
                    this.top = top
                    this.left = left
                    this.right = right
                    this.bottom = bottom
                }
            }
        }
    }

    private fun createItemDecorator(
        spacing: Int
    ): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val column = position % 2 // Assuming span count is 2
                val spacingPx = spacing

                outRect.apply {
                    top = spacingPx
                    bottom = spacingPx

                    if (column == 0) {
                        left = spacingPx
                        right = spacingPx / 2
                    } else {
                        left = spacingPx / 2
                        right = spacingPx
                    }
                }
            }
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.setVariable(getBindingVariable(), viewModel)
        mViewDataBinding?.lifecycleOwner = this
        mViewDataBinding?.executePendingBindings()
        bindViewModel()
    }

    fun getBaseActivity(): BaseActivity<*, *>? {
        return mActivity
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    fun hideKeyboard() {
        mActivity?.hideSoftKeyboard()
    }

    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }

    protected abstract fun bindViewModel()

    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }

}
