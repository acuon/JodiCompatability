package com.jodi.companioncompatibility.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.jodi.companioncompatibility.utils.widgets.JodiSnackBar
import com.jodi.companioncompatibility.data.pref.JodiPreferences
import com.jodi.companioncompatibility.utils.extensions.hideSoftKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseFragment2<B : ViewBinding> : Fragment(), BaseFragmentCallbacks {

    lateinit var binding: B
    var hasInitializedRootView = false
    var savedInstanceStateBundle: Bundle? = null
    val pref by lazy { JodiPreferences() }
    private var snackBar: JodiSnackBar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        (this as? BaseFragmentCallbacks)?.let { initViewModel(savedInstanceState) }
        savedInstanceStateBundle = savedInstanceState
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (this as? BaseFragmentCallbacks)?.let {
            setupView()
            bindViewEvents()
            bindViewModel()
        }
    }


    protected fun showSnackBar(message: String?, type: JodiSnackBar.SnackType) {
        binding.root.let {
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

    fun itemDecoration(_value: Int): RecyclerView.ItemDecoration {
        return createItemDecorator(_value, _value, _value, _value)
    }

    fun itemDecoration(
        _top: Int,
        _bottom: Int,
        _left: Int,
        _right: Int
    ): RecyclerView.ItemDecoration {
        return createItemDecorator(_top, _bottom, _left, _right)
    }

    private fun createItemDecorator(
        _top: Int,
        _bottom: Int,
        _left: Int,
        _right: Int
    ): RecyclerView.ItemDecoration {
        return object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                with(outRect) {
                    top = _top
                    left = _left
                    right = _right
                    bottom = _bottom
                }
            }
        }
    }

    override fun initViewModel(savedInstanceState: Bundle?) {}

    @CallSuper
    override fun bindViewEvents() {
        requireNotNull(view).setOnClickListener {
            requireActivity().hideSoftKeyboard()
        }
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


    protected val onClickListener = View.OnClickListener {
        requireActivity().hideSoftKeyboard()
        onClick(it)
    }

    open fun onBackPressed() {}

    protected abstract fun onClick(view: View)

    abstract fun getViewBinding(): B

    fun runDelayed(delayMilliSec: Long, job: suspend () -> Unit) =
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(delayMilliSec)
                withContext(Dispatchers.Main) {
                    job.invoke()
                }
            }
        }


}