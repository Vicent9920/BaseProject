@file:Suppress("unused")
package com.vincent.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 验证码倒计时
 */
// 秒数单位文本
private const val  TIME_UNIT = "S"
class CountdownView: TextView, Runnable {


    /**
     * 倒计时秒数
     */
     var mTotalTime = 60
    /**
     * 当前秒数
     */
    private var mCurrentTime: Int = 0
    /**
     * 记录原有的文本
     */
    private var mRecordText: CharSequence? = null
    /**
     * 标记是否重置了倒计控件
     */
    private var mFlag: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 重置倒计时控件
     */
    fun resetState() {
        mFlag = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //设置点击的属性
        isClickable = true
    }

    override fun onDetachedFromWindow() {
        // 移除延迟任务，避免内存泄露
        removeCallbacks(this)
        super.onDetachedFromWindow()
    }

    override fun performClick(): Boolean {
        val click = super.performClick()
        mRecordText = text
        isEnabled = false
        mCurrentTime = mTotalTime
        post(this)
        return click
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        if (mCurrentTime == 0 || mFlag) {
            text = mRecordText
            isEnabled = true
            mFlag = false
        } else {
            mCurrentTime--
            text = mCurrentTime.toString() + "\t" + TIME_UNIT
            postDelayed(this, 1000)
        }
    }
}