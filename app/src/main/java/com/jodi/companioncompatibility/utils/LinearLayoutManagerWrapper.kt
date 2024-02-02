package com.jodi.companioncompatibility.utils

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class LinearLayoutManagerWrapper : LinearLayoutManager {

    private lateinit var mContext : Context

    constructor(context: Context) : super(context){
        mContext = context
    }
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun canScrollHorizontally(): Boolean {
        return false
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }


    //Override this method? Check.
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView?,
        state: RecyclerView.State?, position: Int
    ) {

        //Create your RecyclerView.SmoothScroller instance? Check.
        val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(mContext) {
            //Automatically implements this method on instantiation.
            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return null
            }
        }

        //Docs do not tell us anything about this,
        //but we need to set the position we want to scroll to.
        smoothScroller.targetPosition = position

        //Call startSmoothScroll(SmoothScroller)? Check.
        startSmoothScroll(smoothScroller)
    }
}