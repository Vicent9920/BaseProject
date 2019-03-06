package com.vincent.baselibrary.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 创建日期：2019/1/15 0015on 下午 1:34
 * 描述：根据位置设置不同的分割线
 * @author：Vincent
 * QQ：3332168769
 * 备注：分割线工具类
 */
class MarginDecoration(val callback: MarginDecorationCallback) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        parent ?: return
        view ?: return
        callback.onGetItemOffsets(outRect, parent.getChildLayoutPosition(view))
//        if (parent.getChildLayoutPosition(view) == 0) {
//
//            outRect?.set(0, 0, 0, 0)
//        } else {
//
//            val margin = view.context.resources.getDimension(R.dimen.textSize_10).toInt()
//            outRect?.set(0, margin, 0, margin)
//        }
    }


}

interface MarginDecorationCallback {
    fun onGetItemOffsets(outRect: Rect?, position: Int)
}