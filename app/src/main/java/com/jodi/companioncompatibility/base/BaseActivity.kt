package com.jodi.companioncompatibility.base

import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.jodi.companioncompatibility.utils.widgets.FlavumSnackBar
import com.jodi.companioncompatibility.utils.extensions.hideSoftKeyboard
import com.jodi.companioncompatibility.data.pref.JodiPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding?, V : BaseViewModel<*>> :
    AppCompatActivity(), BaseFragment.Callback {
    private var viewDataBinding: T? = null

    val pref by lazy { JodiPreferences() }
    private var snackBar: FlavumSnackBar? = null

    //injecting the viewmodel
    @Inject
    lateinit var viewModel: V

    //method for getting the binding variable - viewmodel reference
//    abstract val bindingVariable: Int
//
//    @get:LayoutRes
//    abstract val layoutId: Int

    // Method for getting the binding variable - viewmodel reference
    abstract fun getBindingVariable(): Int

    // Fragment layout reference
    @LayoutRes
    abstract fun getLayoutId(): Int

    fun showToast(str: String?) {
        Toast.makeText(viewDataBinding!!.root.context, str, Toast.LENGTH_SHORT).show()
    }

    override fun onFragmentAttached() {}
    override fun onFragmentDetached(tag: String) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
    }

    protected fun showSnackBar(message: String?, type: FlavumSnackBar.SnackType) {
        viewDataBinding?.root?.let {
            snackBar = FlavumSnackBar
                .Builder()
                .type(type)
                .message(message)
                .setCallBack(snackListener)
                .make(it)
                .showSnack()
        }
    }

    private val snackListener = object : FlavumSnackBar.Builder.ISnackListener {
        override fun onClosed(view: View) {
            snackBar?.dismiss()
        }
    }

    fun getViewDataBinding() = viewDataBinding!!

    fun hasPermission(permission: String?): Boolean {
        return checkSelfPermission(permission!!) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionsSafely(permissions: Array<String?>?, requestCode: Int) {
        requestPermissions(permissions!!, requestCode)
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

    protected abstract fun onViewClicked(view: View?)
    var clickListener =
        View.OnClickListener { view ->
            hideSoftKeyboard()
            onViewClicked(view)
        }

    //setting the binding variable
    private fun performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        viewDataBinding!!.setVariable(getBindingVariable(), viewModel)
        viewDataBinding!!.executePendingBindings()
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

}



