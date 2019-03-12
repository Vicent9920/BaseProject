package com.vincent.baseproject.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.CollapsingToolbarLayout

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 支持监听渐变的CollapsingToolbarLayout
 */
class XCollapsingToolbarLayout : CollapsingToolbarLayout {

     var mListener: OnScrimsListener? = null // 渐变监听
     var isCurrentScrimsShown: Boolean = false  // 当前渐变状态

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setScrimsShown(shown: Boolean, animate: Boolean) {
        super.setScrimsShown(shown, animate)
        if(isCurrentScrimsShown != shown){
            isCurrentScrimsShown = shown
            mListener?.onScrimsStateChange(shown)
        }
    }

    /**
     * CollapsingToolbarLayout渐变监听器
     */
    interface OnScrimsListener {

        /**
         * 渐变状态变化
         *
         * @param shown         渐变开关
         */
        fun onScrimsStateChange(shown: Boolean)
    }


}

