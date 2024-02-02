package com.jodi.companioncompatibility.utils.extensions

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jodi.companioncompatibility.utils.GridLayoutManagerWrapper
import com.jodi.companioncompatibility.utils.LinearLayoutManagerWrapper

fun RecyclerView.verticalView(context: Context, stackEnd: Boolean = false) {
    layoutManager = LinearLayoutManagerWrapper(context).apply { stackFromEnd = stackEnd }
    layoutManager
}

fun RecyclerView.horizontalView(context: Context) {
    layoutManager = LinearLayoutManagerWrapper(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.gridView(context: Context, spanCount: Int) {
    layoutManager = GridLayoutManagerWrapper(context, spanCount)
}

fun RecyclerView.addDecoration(decorator: RecyclerView.ItemDecoration) {
    if (itemDecorationCount == 0) addItemDecoration(decorator)
}