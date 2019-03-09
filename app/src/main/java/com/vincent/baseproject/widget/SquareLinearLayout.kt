package com.vincent.baseproject.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.vincent.baselibrary.util.SquareDelegate

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 正方形的LinearLayout
 */
class SquareLinearLayout : LinearLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            SquareDelegate.measureWidth(widthMeasureSpec, heightMeasureSpec),
            SquareDelegate.measureHeight(widthMeasureSpec, heightMeasureSpec)
        )
    }
}