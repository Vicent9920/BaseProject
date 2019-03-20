package com.vincent.baseproject.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.os.Bundle
import android.os.Parcelable




/**
 * 创建日期：2019/3/20 0020on 上午 9:40
 * 描述：数字进度条
 * @author：Vincent
 * QQ：3332168769
 * 备注：java版本Kotlin化
 */

private const val INSTANCE_STATE = "saved_instance"
private const val INSTANCE_TEXT_COLOR = "text_color"
private const val INSTANCE_TEXT_SIZE = "text_size"
private const val INSTANCE_REACHED_BAR_HEIGHT = "reached_bar_height"
private const val INSTANCE_REACHED_BAR_COLOR = "reached_bar_color"
private const val INSTANCE_UNREACHED_BAR_HEIGHT = "unreached_bar_height"
private const val INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color"
private const val INSTANCE_MAX = "max"
private const val INSTANCE_PROGRESS = "progress"
private const val INSTANCE_SUFFIX = "suffix"
private const val INSTANCE_PREFIX = "prefix"
private const val INSTANCE_TEXT_VISIBILITY = "text_visibility"
private const val PROGRESS_TEXT_VISIBLE = 0

class NumberProgressBar(ctx:Context,attrs:AttributeSet?,defStyleAttr:Int) : View (ctx,attrs,defStyleAttr){
    var mMaxProgress = 100
    var mCurrentProgress = 0
    var mReachedBarColor = 0
    var mUnreachedBarColor = 0
    var mTextColor = 0
    var mTextSize = 0f
    var mReachedBarHeight = 0f
    var mUnreachedBarHeight = 0f
    var mSuffix = "%"
    var mPrefix = ""
    var mDrawTextStart = 0f
    var mDrawTextEnd = 0f
    var mCurrentDrawText = ""
    var mReachedBarPaint = Paint()
    var mUnreachedBarPaint = Paint()
    var mTextPaint = Paint()
    val mUnreachedRectF = RectF(0f, 0f, 0f, 0f)
    val mReachedRectF = RectF(0f, 0f, 0f, 0f)
    var mOffset = 0f
    var mDrawUnreachedBar = true
    var mDrawReachedBar = true
    var mIfDrawText = true

    var mListener:OnProgressBarListener? = null
    constructor(ctx: Context):this(ctx,null)
    constructor(ctx:Context,attrs:AttributeSet?):this(ctx,attrs,0)

    init {

        val attr = ctx.obtainStyledAttributes(attrs, com.vincent.baseproject.R.styleable.XUpdate_ProgressBar)
        mReachedBarColor = attr.getColor(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_reached_color,Color.rgb(66,45,241))
        mUnreachedBarColor = attr.getColor(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_unreached_color,Color.rgb(204,204,204))

        mTextColor = attr.getColor(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_text_color,Color.rgb(66,145,241))
        mTextSize = attr.getDimension(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_text_size,sp2px(10f))

        mReachedBarHeight = attr.getDimension(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_reached_bar_height,dp2px(1.5f))
        mOffset = attr.getDimension(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_text_offset,dp2px(3.0f))

        val textVisible = attr.getInt(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_text_visibility,PROGRESS_TEXT_VISIBLE)
        mIfDrawText = textVisible == PROGRESS_TEXT_VISIBLE

        setMax(attr.getInt(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_max,100))
        setProgress(attr.getInt(com.vincent.baseproject.R.styleable.XUpdate_ProgressBar_progress_current,0))
        attr.recycle()
        initializePainters()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec,true),measure(heightMeasureSpec,false))
    }

    override fun getSuggestedMinimumWidth(): Int {
        return mTextSize.toInt()
    }

    override fun getSuggestedMinimumHeight(): Int {
        val h1 = if(mUnreachedBarHeight>mReachedBarHeight) mUnreachedBarHeight else mReachedBarHeight
        val h2 = if(mTextSize>h1) mTextSize else h1
        return h2.toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        if (mIfDrawText) {
            calculateDrawRectF()
        } else {
            calculateDrawRectFWithoutProgressText()
        }

        if (mDrawReachedBar) {
            canvas?.drawRect(mReachedRectF, mReachedBarPaint);
        }

        if (mDrawUnreachedBar) {
            canvas?.drawRect(mUnreachedRectF, mUnreachedBarPaint)
        }

        if (mIfDrawText) {
            canvas?.drawText(mCurrentDrawText, mDrawTextStart, mDrawTextEnd, mTextPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putInt(INSTANCE_TEXT_COLOR, mTextColor)
        bundle.putFloat(INSTANCE_TEXT_SIZE, mTextSize)
        bundle.putFloat(INSTANCE_REACHED_BAR_HEIGHT, mReachedBarHeight)
        bundle.putFloat(INSTANCE_UNREACHED_BAR_HEIGHT, mUnreachedBarHeight)
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, mReachedBarColor)
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, mUnreachedBarColor)
        bundle.putInt(INSTANCE_MAX, mMaxProgress)
        bundle.putInt(INSTANCE_PROGRESS, mCurrentProgress)
        bundle.putString(INSTANCE_SUFFIX, mSuffix)
        bundle.putString(INSTANCE_PREFIX, mPrefix)
        bundle.putBoolean(INSTANCE_TEXT_VISIBILITY, mIfDrawText)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            mTextColor = state.getInt(INSTANCE_TEXT_COLOR)
            mTextSize = state.getFloat(INSTANCE_TEXT_SIZE)
            mReachedBarHeight = state.getFloat(INSTANCE_REACHED_BAR_HEIGHT)
            mUnreachedBarHeight = state.getFloat(INSTANCE_UNREACHED_BAR_HEIGHT)
            mReachedBarColor = state.getInt(INSTANCE_REACHED_BAR_COLOR)
            mUnreachedBarColor = state.getInt(INSTANCE_UNREACHED_BAR_COLOR)
            initializePainters()
            setMax(state.getInt(INSTANCE_MAX))
            setProgress(state.getInt(INSTANCE_PROGRESS))
            mPrefix = state.getString(INSTANCE_PREFIX)?:""
            mSuffix = state.getString(INSTANCE_SUFFIX)?:""
            setProgressTextVisibility(if (state.getBoolean(INSTANCE_TEXT_VISIBILITY)) ProgressTextVisibility.VISIBLE else ProgressTextVisibility.INVISIBLE)
            super.onRestoreInstanceState(state.getParcelable(INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    private fun calculateDrawRectFWithoutProgressText() {
        mReachedRectF.left = paddingLeft.toFloat()
        mReachedRectF.top = height / 2.0f - mReachedBarHeight / 2.0f
        mReachedRectF.right = (width - paddingLeft - paddingRight) / mMaxProgress.toFloat() * mCurrentProgress + paddingLeft
        mReachedRectF.bottom = height / 2.0f + mReachedBarHeight / 2.0f

        mUnreachedRectF.left = mReachedRectF.right
        mUnreachedRectF.right = (width - paddingRight).toFloat()
        mUnreachedRectF.top = height / 2.0f + -mUnreachedBarHeight / 2.0f
        mUnreachedRectF.bottom = height / 2.0f + mUnreachedBarHeight / 2.0f
    }

    private fun calculateDrawRectF() {
        mCurrentDrawText = String.format("%d", mCurrentProgress * 100 / mMaxProgress)
        mCurrentDrawText = mPrefix + mCurrentDrawText + mSuffix
        /*
         The width of the text that to be drawn.
        */
        val drawTextWidth = mTextPaint.measureText(mCurrentDrawText)

        if (mCurrentProgress == 0) {
            mDrawReachedBar = false
            mDrawTextStart = paddingLeft.toFloat()
        } else {
            mDrawReachedBar = true
            mReachedRectF.left = paddingLeft.toFloat()
            mReachedRectF.top = height / 2.0f - mReachedBarHeight / 2.0f
            mReachedRectF.right =
                (width - paddingLeft - paddingRight) / (mMaxProgress * 1.0f) * mCurrentProgress - mOffset + paddingLeft
            mReachedRectF.bottom = height / 2.0f + mReachedBarHeight / 2.0f
            mDrawTextStart = mReachedRectF.right + mOffset
        }

        mDrawTextEnd = (height / 2.0f - (mTextPaint.descent() + mTextPaint.ascent()) / 2.0f)

        if (mDrawTextStart + drawTextWidth >= width - paddingRight) {
            mDrawTextStart = width.toFloat() - paddingRight.toFloat() - drawTextWidth
            mReachedRectF.right = mDrawTextStart - mOffset
        }

        val unreachedBarStart = mDrawTextStart + drawTextWidth + mOffset
        if (unreachedBarStart >= width - paddingRight) {
            mDrawUnreachedBar = false
        } else {
            mDrawUnreachedBar = true
            mUnreachedRectF.left = unreachedBarStart
            mUnreachedRectF.right = (width - paddingRight).toFloat()
            mUnreachedRectF.top = height / 2.0f + -mUnreachedBarHeight / 2.0f
            mUnreachedRectF.bottom = height / 2.0f + mUnreachedBarHeight / 2.0f
        }
    }


    private fun measure(measureSpec: Int, isWidth: Boolean): Int {
        var result: Int
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        val padding = if (isWidth) paddingLeft + paddingRight else paddingTop + paddingBottom
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = if (isWidth) suggestedMinimumWidth else suggestedMinimumHeight
            result += padding
            if (mode == View.MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size)
                } else {
                    result = Math.min(result, size)
                }
            }
        }
        return result
    }

    /**
     * 单独使用方法初始化是由于屏幕刷新（横竖屏切换）的时候初始化属性
     */
    private fun initializePainters() {
        mReachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mReachedBarPaint.color = mReachedBarColor

        mUnreachedBarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mUnreachedBarPaint.color = mUnreachedBarColor

        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = mTextColor
        mTextPaint.textSize = mTextSize

    }

    fun setProgress(progress:Int){
        if(!(progress > mMaxProgress || progress < 0)){
            this.mCurrentProgress = progress
            invalidate()
        }
    }

    fun setMax(maxProgress:Int){
        if(maxProgress>0){
            this.mMaxProgress = maxProgress
            invalidate()
        }
    }

    fun setUnreachedBarColor(barColor: Int){
        this.mUnreachedBarColor = barColor
        mUnreachedBarPaint.color = mReachedBarColor
        invalidate()
    }

    fun setReachedBarColor(progressColor:Int){
        this.mReachedBarColor = progressColor
        mReachedBarPaint.color = mReachedBarColor
        invalidate()
    }

    fun setProgressTextColor(textColor:Int){
        this.mTextColor = textColor
        mTextPaint.color = mTextColor
        invalidate()
    }

    fun incrementProgressBy(by:Int){
        if (by > 0) {
            setProgress(mCurrentProgress + by)
        }
        mListener?.onProgressChange(mCurrentProgress,mMaxProgress)
    }

    fun setProgressTextVisibility(visibility:ProgressTextVisibility){
        mIfDrawText = visibility == ProgressTextVisibility.VISIBLE
        invalidate()
    }

    fun dp2px(dp: Float): Float {
        val scale = resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(sp: Float): Float {
        val scale = resources.displayMetrics.scaledDensity
        return sp * scale
    }
    enum class ProgressTextVisibility {
        VISIBLE, INVISIBLE
    }

    interface OnProgressBarListener {

        fun onProgressChange(current: Int, max: Int)
    }
}