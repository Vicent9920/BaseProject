package com.vincent.dialoglibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.vincent.dialoglibrary.R
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


/**
 * author : brucetoo
 * github : https://github.com/brucetoo/PickView
 * time   : 2019/02/17
 * desc   : 循环滚动列表自定义控件
 */
class LoopView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mScheduledFuture: ScheduledFuture<*>? = null

    private var mListener: LoopScrollListener? = null

    private var mData: List<String>? = null

    private val mTopBottomTextPaint: Paint
    private val mCenterTextPaint: Paint
    private val mCenterLinePaint: Paint

    private var mTotalScrollY: Int = 0
    private val mGestureDetector: GestureDetector
    var selectedItem: Int = 0
        private set
    private var mTextSize: Int = 0

    private var mMaxTextWidth: Int = 0
    private var mMaxTextHeight: Int = 0

    private var mTopBottomTextColor: Int = 0

    private var mCenterTextColor: Int = 0
    private var mCenterLineColor: Int = 0

    private var mLineSpacingMultiplier: Float = 0.toFloat()
    private var mCanLoop: Boolean = false

    private var mTopLineY: Float = 0.toFloat()
    private var mBottomLineY: Float = 0.toFloat()

    private var mCurrentIndex: Int = 0
    private var mInitPosition: Int = 0

    private var mHorizontalPadding: Float = 0.toFloat()
    private var mVerticalPadding: Float = 0.toFloat()

    private var mItemHeight: Float = 0.toFloat()
    private var mDrawItemsCount: Int = 0
    private var mItemTempArray: Array<String?>? = null

    private var mCircularDiameter: Float = 0.toFloat()
    private var mCircularRadius: Float = 0.toFloat()

    init {

        val array = context.obtainStyledAttributes(attrs, R.styleable.LoopView)
        if (array != null) {
            mTopBottomTextColor = array.getColor(R.styleable.LoopView_topBottomTextColor, -0x505051)
            mCenterTextColor = array.getColor(R.styleable.LoopView_centerTextColor, -0xcececf)
            mCenterLineColor = array.getColor(R.styleable.LoopView_lineColor, -0x3a3a3b)
            mCanLoop = array.getBoolean(R.styleable.LoopView_canLoop, true)
            mInitPosition = array.getInt(R.styleable.LoopView_initPosition, -1)
            mTextSize = array.getDimensionPixelSize(
                R.styleable.LoopView_textSize,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    18f,
                    getContext().resources.displayMetrics
                ).toInt()
            )
            mDrawItemsCount = array.getInt(R.styleable.LoopView_drawItemCount, 7)
            mItemTempArray = arrayOfNulls(mDrawItemsCount)
            array.recycle()
        }

        mLineSpacingMultiplier = 3f

        mTopBottomTextPaint = Paint()
        mCenterTextPaint = Paint()
        mCenterLinePaint = Paint()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        mGestureDetector = GestureDetector(context, LoopViewGestureListener())
        mGestureDetector.setIsLongpressEnabled(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec

        when (View.MeasureSpec.getMode(widthSpec)) {
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> widthSpec =
                View.MeasureSpec.makeMeasureSpec(mMaxTextWidth, View.MeasureSpec.EXACTLY)
            View.MeasureSpec.EXACTLY -> {
            }
        }

        when (View.MeasureSpec.getMode(heightSpec)) {
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> heightSpec =
                View.MeasureSpec.makeMeasureSpec(mCircularDiameter.toInt(), View.MeasureSpec.EXACTLY)
            View.MeasureSpec.EXACTLY -> {
            }
        }
        setMeasuredDimension(widthSpec, heightSpec)

        val width = View.MeasureSpec.getSize(widthSpec)
        val height = View.MeasureSpec.getSize(heightSpec)

        mItemHeight = mLineSpacingMultiplier * mMaxTextHeight
        // auto calculate the text's left/right value when draw
        mHorizontalPadding = ((width - mMaxTextWidth) / 2).toFloat()
        mVerticalPadding = (height - mCircularDiameter) / 2

        // topLineY = diameter/2 - itemHeight(mItemHeight) / 2 + mVerticalPadding
        mTopLineY = (mCircularDiameter - mItemHeight) / 2 + mVerticalPadding
        mBottomLineY = (mCircularDiameter + mItemHeight) / 2 + mVerticalPadding
    }

    override fun onDraw(canvas: Canvas) {

        if (mData == null) return

        // the length of single item is mItemHeight
        val mChangingItem = (mTotalScrollY / mItemHeight).toInt()
        mCurrentIndex = mInitPosition + mChangingItem % mData!!.size
        if (!mCanLoop) { // can loop
            if (mCurrentIndex < 0) {
                mCurrentIndex = 0
            }
            if (mCurrentIndex > mData!!.size - 1) {
                mCurrentIndex = mData!!.size - 1
            }
        } else { // can not loop
            if (mCurrentIndex < 0) {
                mCurrentIndex += mData!!.size
            }
            if (mCurrentIndex > mData!!.size - 1) {
                mCurrentIndex -= mData!!.size
            }
        }

        var count = 0
        // reconfirm each item's value from dataList according to currentIndex,
        while (count < mDrawItemsCount) {
            var templateItem = mCurrentIndex - (mDrawItemsCount / 2 - count)
            when {
                mCanLoop -> {
                    if (templateItem < 0) {
                        templateItem += mData!!.size
                    }
                    if (templateItem > mData!!.size - 1) {
                        templateItem -= mData!!.size
                    }
                    mItemTempArray!![count] = mData!![templateItem]
                }
                templateItem < 0 -> mItemTempArray!![count] = ""
                templateItem > mData!!.size - 1 -> mItemTempArray!![count] = ""
                else -> mItemTempArray!![count] = mData!![templateItem]
            }
            count++
        }

        // draw top and bottom line
        canvas.drawLine(0f, mTopLineY, measuredWidth.toFloat(), mTopLineY, mCenterLinePaint)
        canvas.drawLine(0f, mBottomLineY, measuredWidth.toFloat(), mBottomLineY, mCenterLinePaint)

        count = 0
        val changingLeftY = (mTotalScrollY % mItemHeight).toInt()
        while (count < mDrawItemsCount) {
            canvas.save()
            // L= å * r -> å = rad
            val itemHeight = mMaxTextHeight * mLineSpacingMultiplier
            // get radian  L = (itemHeight * count - changingLeftY),r = mCircularRadius
            val radian = ((itemHeight * count - changingLeftY) / mCircularRadius).toDouble()
            // a = rad * 180 / π
            // get angle
            val angle = (radian * 180 / Math.PI).toFloat()

            // when angle >= 180 || angle <= 0 don't draw
            if (angle >= 180f || angle <= 0f) {
                canvas.restore()
            } else {
                // translateY = r - r*cos(å) -
                // (Math.sin(radian) * mMaxTextHeight) / 2 this is text offset
                val translateY =
                    (mCircularRadius.toDouble() - Math.cos(radian) * mCircularRadius - Math.sin(radian) * mMaxTextHeight / 2).toFloat() + mVerticalPadding
                canvas.translate(0.0f, translateY)
                // scale offset = Math.sin(radian) -> 0 - 1
                canvas.scale(1.0f, Math.sin(radian).toFloat())
                if (translateY <= mTopLineY) {
                    // draw text y between 0 -> mTopLineY,include incomplete text
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), mTopLineY - translateY)
                    canvas.drawText(
                        mItemTempArray!![count],
                        mHorizontalPadding,
                        mMaxTextHeight.toFloat(),
                        mTopBottomTextPaint
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(0f, mTopLineY - translateY, measuredWidth.toFloat(), itemHeight.toInt().toFloat())
                    canvas.drawText(
                        mItemTempArray!![count],
                        mHorizontalPadding,
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint
                    )
                    canvas.restore()
                } else if (mMaxTextHeight + translateY >= mBottomLineY) {
                    // draw text y between  mTopLineY -> mBottomLineY ,include incomplete text
                    canvas.save()
                    canvas.clipRect(0f, 0f, measuredWidth.toFloat(), mBottomLineY - translateY)
                    canvas.drawText(
                        mItemTempArray!![count],
                        mHorizontalPadding,
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        mBottomLineY - translateY,
                        measuredWidth.toFloat(),
                        itemHeight.toInt().toFloat()
                    )
                    canvas.drawText(
                        mItemTempArray!![count],
                        mHorizontalPadding,
                        mMaxTextHeight.toFloat(),
                        mTopBottomTextPaint
                    )
                    canvas.restore()
                } else if (translateY >= mTopLineY && mMaxTextHeight + translateY <= mBottomLineY) {
                    // draw center complete text
                    canvas.clipRect(0, 0, measuredWidth, itemHeight.toInt())
                    canvas.drawText(
                        mItemTempArray!![count],
                        mHorizontalPadding,
                        mMaxTextHeight.toFloat(),
                        mCenterTextPaint
                    )
                    // center one indicate selected item
                    selectedItem = mData!!.indexOf(mItemTempArray!![count])
                }
                canvas.restore()
            }
            count++
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(motionevent: MotionEvent): Boolean {

        when (motionevent.action) {
            MotionEvent.ACTION_UP -> if (!mGestureDetector.onTouchEvent(motionevent)) {
                startSmoothScrollTo()
            }
            else -> if (!mGestureDetector.onTouchEvent(motionevent)) {
                startSmoothScrollTo()
            }
        }
        return true
    }

    fun setCanLoop(canLoop: Boolean) {
        mCanLoop = canLoop
        invalidate()
    }

    /**
     * set text size
     *
     * @param size size indicate sp,not px
     */
    fun setTextSize(size: Float) {
        if (size > 0) {
            mTextSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.resources.displayMetrics).toInt()
        }
    }

    fun setLineSpacingMultiplier(spacing: Float) {
        this.mLineSpacingMultiplier = spacing
    }

    fun setInitPosition(initPosition: Int) {
        var position = initPosition
        mData ?: return
        if (position > mData!!.size) {
            position = mData!!.size - 1
        }
        mInitPosition = position
        invalidate()
        mListener?.onItemSelect(this, position)
    }

    fun setLoopListener(l: LoopScrollListener) {
        mListener = l
    }

    /**
     * All public method must be called before this method
     * @param data data list
     */
    fun setData(data: List<String>) {
        mData = data

        if (mData == null) {
            throw IllegalArgumentException("data list must not be null!")
        }
        mTopBottomTextPaint.color = mTopBottomTextColor
        mTopBottomTextPaint.isAntiAlias = true
        mTopBottomTextPaint.typeface = Typeface.MONOSPACE
        mTopBottomTextPaint.textSize = mTextSize.toFloat()

        mCenterTextPaint.color = mCenterTextColor
        mCenterTextPaint.isAntiAlias = true
        mCenterTextPaint.textScaleX = 1.05f
        mCenterTextPaint.typeface = Typeface.MONOSPACE
        mCenterTextPaint.textSize = mTextSize.toFloat()

        mCenterLinePaint.color = mCenterLineColor
        mCenterLinePaint.isAntiAlias = true
        mCenterLinePaint.typeface = Typeface.MONOSPACE
        mCenterLinePaint.textSize = mTextSize.toFloat()

        // measureTextWidthHeight
        val rect = Rect()
        for (i in mData!!.indices) {
            val text = mData!![i]
            mCenterTextPaint.getTextBounds(text, 0, text.length, rect)

            val textWidth = rect.width()
            if (textWidth > mMaxTextWidth) {
                mMaxTextWidth = textWidth
            }

            //int textHeight = rect.height();
            //if (textHeight > mMaxTextHeight) {
            //    mMaxTextHeight = textHeight;
            //}

            val fontMetrics = mCenterTextPaint.fontMetrics
            mMaxTextHeight = (fontMetrics.bottom - fontMetrics.top).toInt() * 2 / 3
        }

        // 计算半圆周 -- mMaxTextHeight * mLineSpacingMultiplier 表示每个item的高度  mDrawItemsCount = 7
        // 实际显示5个,留两个是在圆周的上下面
        // lineSpacingMultiplier是指text上下的距离的值和maxTextHeight一样的意思 所以 = 2
        // mDrawItemsCount - 1 代表圆周的上下两面各被剪切了一半 相当于高度少了一个 mMaxTextHeight
        val halfCircumference =
            (mMaxTextHeight.toFloat() * mLineSpacingMultiplier * (mDrawItemsCount - 1).toFloat()).toInt()
        // the diameter of circular 2πr = cir, 2r = height
        mCircularDiameter = (halfCircumference * 2 / Math.PI).toInt().toFloat()
        // the radius of circular
        mCircularRadius = (halfCircumference / Math.PI).toInt().toFloat()
        // FIXME: 7/8/16  通过控件的高度来计算圆弧的周长

        if (mInitPosition == -1) {
            mInitPosition = if (mCanLoop) {
                (mData!!.size + 1) / 2
            } else {
                0
            }
        }
        mCurrentIndex = mInitPosition
        invalidate()
    }

    private fun cancelSchedule() {

        if (mScheduledFuture != null && !mScheduledFuture!!.isCancelled) {
            mScheduledFuture?.cancel(true)
            mScheduledFuture = null
        }
    }

    private fun startSmoothScrollTo() {
        val offset = (mTotalScrollY % mItemHeight).toInt()
        cancelSchedule()
        mScheduledFuture = mExecutor.scheduleWithFixedDelay(HalfHeightRunnable(offset), 0, 10, TimeUnit.MILLISECONDS)
    }

    private fun startSmoothScrollTo(velocityY: Float) {
        cancelSchedule()
        val velocityFling = 20
        mScheduledFuture =
            mExecutor.scheduleWithFixedDelay(FlingRunnable(velocityY), 0, velocityFling.toLong(), TimeUnit.MILLISECONDS)
    }

    internal inner class LoopViewGestureListener : android.view.GestureDetector.SimpleOnGestureListener() {

        override fun onDown(motionevent: MotionEvent): Boolean {
            cancelSchedule()
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            startSmoothScrollTo(velocityY)
            return true
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mTotalScrollY = (mTotalScrollY.toFloat() + distanceY).toInt()
            if (!mCanLoop) {
                val initPositionCircleLength = (mInitPosition * mItemHeight).toInt()
                val initPositionStartY = -1 * initPositionCircleLength
                if (mTotalScrollY < initPositionStartY) {
                    mTotalScrollY = initPositionStartY
                }

                val circleLength = ((mData!!.size - 1 - mInitPosition).toFloat() * mItemHeight).toInt()
                if (mTotalScrollY >= circleLength) {
                    mTotalScrollY = circleLength
                }
            }

            invalidate()
            return true
        }
    }

    internal inner class SelectedRunnable : Runnable {

        override fun run() {
            mListener?.onItemSelect(this@LoopView, selectedItem)
        }
    }

    /**
     * Use in ACTION_UP
     */
    private inner class HalfHeightRunnable internal constructor(internal var offset: Int) : Runnable {

        internal var realTotalOffset: Int = 0
        internal var realOffset: Int = 0

        init {
            realTotalOffset = Integer.MAX_VALUE
            realOffset = 0
        }

        override fun run() {
            // first in
            if (realTotalOffset == Integer.MAX_VALUE) {

                realTotalOffset = if (offset.toFloat() > mItemHeight / 2.0f) {
                    // move to next item
                    (mItemHeight - offset.toFloat()).toInt()
                } else {
                    // move to pre item
                    -offset
                }
            }

            realOffset = (realTotalOffset.toFloat() * 0.1f).toInt()

            if (realOffset == 0) {

                realOffset = if (realTotalOffset < 0) {
                    -1
                } else {
                    1
                }
            }
            if (Math.abs(realTotalOffset) <= 0) {
                cancelSchedule()
                post {
                    if (mListener != null) {
                        postDelayed(SelectedRunnable(), 200L)
                    }
                }
            } else {
                mTotalScrollY += realOffset
                postInvalidate()
                realTotalOffset -= realOffset
            }
        }
    }

    /**
     * Use in [LoopViewGestureListener.onFling]
     */
    private inner class FlingRunnable internal constructor(internal val velocityY: Float) : Runnable {

        internal var velocity: Float = 0.toFloat()

        init {
            this.velocity = Integer.MAX_VALUE.toFloat()
        }

        override fun run() {
            if (velocity == Integer.MAX_VALUE.toFloat()) {
                velocity = if (Math.abs(velocityY) > 2000f) {
                    if (velocityY > 0.0f) {
                        2000f
                    } else {
                        -2000f
                    }
                } else {
                    velocityY
                }
            }
            if (Math.abs(velocity) in 0.0f..20f) {
                cancelSchedule()
                post { startSmoothScrollTo() }
                return
            }
            val i = (velocity * 10f / 1000f).toInt()
            mTotalScrollY -= i
            if (!mCanLoop) {
                val itemHeight = mLineSpacingMultiplier * mMaxTextHeight
                if (mTotalScrollY <= ((-mInitPosition).toFloat() * itemHeight).toInt()) {
                    velocity = 40f
                    mTotalScrollY = ((-mInitPosition).toFloat() * itemHeight).toInt()
                } else if (mTotalScrollY >= ((mData!!.size - 1 - mInitPosition).toFloat() * itemHeight).toInt()) {
                    mTotalScrollY = ((mData!!.size - 1 - mInitPosition).toFloat() * itemHeight).toInt()
                    velocity = -40f
                }
            }
            velocity = if (velocity < 0.0f) {
                velocity + 20f
            } else {
                velocity - 20f
            }
            postInvalidate()
        }
    }

    interface LoopScrollListener {
        fun onItemSelect(loopView: LoopView, position: Int)
    }

}