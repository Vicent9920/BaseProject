package com.vincent.baseproject.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.vincent.baselibrary.util.SquareDelegate

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 正方形的ImageView
 */
class SquareImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            SquareDelegate.measureWidth(widthMeasureSpec, heightMeasureSpec),
            SquareDelegate.measureHeight(widthMeasureSpec, heightMeasureSpec)
        )
    }
}