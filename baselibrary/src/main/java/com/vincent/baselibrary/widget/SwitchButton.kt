@file:Suppress("unused")
package com.vincent.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/02/20
 *    desc   : 开关按钮
 */
private const val STATE_SWITCH_OFF = 1
private const val STATE_SWITCH_OFF2 = 2
private const val STATE_SWITCH_ON = 3
private const val STATE_SWITCH_ON2 = 4

class SwitchButton @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr) {
    // 是否显示按钮阴影
    var isShadow = false
    // 是否选中
    var mChecked = false
    private var isCanVisibleDrawing = false
    // 按钮宽高形状比率(0,1] 不推荐大幅度调整
    var mAspectRatio = 0.68f
    // (0,1]
    var mAnimationSpeed = 0.1f
    // 上一个选中状态
    private var mLastCheckedState: Int = 0
    // 当前的选中状态
    private var mCheckedState: Int = 0

    private val mInterpolator = AccelerateInterpolator(2f)
    private val mPaint = Paint()
    private val mBackgroundPath = Path()
    private val mBarPath = Path()
    private val mBound = RectF()

    var mAccentColor = -0xb4289d // 开启状态背景色
    var mPrimaryDarkColor = -0xc539ae // 开启状态按钮描边色
    var mOffColor = -0x1c1c1d // 关闭状态描边色
    var mOffDarkColor = -0x404041 // 关闭状态按钮描边色
    var mShadowColor = -0xcccccd // 按钮阴影色
    private var mListener: OnCheckedChangeListener? = null

    private var mAnim1: Float = 0f
    private var mAnim2: Float = 0f
    private lateinit var mShadowGradient: RadialGradient

    private var mRight: Float = 0f
    private var mCenterX: Float = 0f
    private var mCenterY: Float = 0f
    private var mScale: Float = 0f

    private var mOffset: Float = 0f
    private var mRadius: Float = 0f
    private var mStrokeWidth: Float = 0f
    private var mWidth: Float = 0f
    private var mLeft: Float = 0f
    private var bRight: Float = 0f
    private var mOnLeftX: Float = 0f
    private var mOn2LeftX: Float = 0f
    private var mOff2LeftX: Float = 0f
    private var mOffLeftX: Float = 0f
    private var mShadowReservedHeight: Float = 0f

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val attr = context.obtainStyledAttributes(attrs, com.vincent.baselibrary.R.styleable.SwitchButton)
        mChecked = attr.getBoolean(com.vincent.baselibrary.R.styleable.SwitchButton_android_checked, mChecked)
        isEnabled = attr.getBoolean(com.vincent.baselibrary.R.styleable.SwitchButton_android_enabled, isEnabled)
        attr.recycle()
        mLastCheckedState = if (mChecked) STATE_SWITCH_ON else STATE_SWITCH_OFF
        mCheckedState = mLastCheckedState
        mPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSpec = widthMeasureSpec
        var heightSpec = heightMeasureSpec
        when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> widthSpec =
                MeasureSpec.makeMeasureSpec(
                    (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, resources.displayMetrics)
                            + paddingLeft.toFloat() + paddingRight.toFloat()).toInt(), MeasureSpec.EXACTLY
                )
            MeasureSpec.EXACTLY -> {
            }
        }
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> heightSpec =
                MeasureSpec.makeMeasureSpec(
                    (MeasureSpec.getSize(widthSpec) * mAspectRatio).toInt()
                            + paddingTop + paddingBottom, MeasureSpec.EXACTLY
                )
            MeasureSpec.EXACTLY -> {
            }
        }
        setMeasuredDimension(widthSpec, heightSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        isCanVisibleDrawing = w > paddingStart + paddingEnd && h > paddingTop + paddingBottom
        if (isCanVisibleDrawing) {
            val actuallyDrawingAreaWidth = w - paddingStart - paddingEnd
            val actuallyDrawingAreaHeight = h - paddingTop - paddingBottom
            val actuallyDrawingAreaLeft:Int
            val actuallyDrawingAreaRight:Int
            val actuallyDrawingAreaTop:Int
            val actuallyDrawingAreaBottom:Int
            if (actuallyDrawingAreaWidth * mAspectRatio < actuallyDrawingAreaHeight) {
                actuallyDrawingAreaLeft = paddingLeft
                actuallyDrawingAreaRight = w - paddingRight
                val heightExtraSize = (actuallyDrawingAreaHeight - actuallyDrawingAreaWidth * mAspectRatio).toInt()
                actuallyDrawingAreaTop = paddingTop + heightExtraSize / 2
                actuallyDrawingAreaBottom = height - paddingBottom - heightExtraSize / 2
            } else {
                val widthExtraSize = (actuallyDrawingAreaWidth - actuallyDrawingAreaHeight / mAspectRatio).toInt()
                actuallyDrawingAreaLeft = paddingLeft + widthExtraSize / 2
                actuallyDrawingAreaRight = width - paddingRight - widthExtraSize / 2
                actuallyDrawingAreaTop = paddingTop
                actuallyDrawingAreaBottom = height - paddingBottom
            }
            mShadowReservedHeight = (actuallyDrawingAreaBottom - actuallyDrawingAreaTop) * 0.07f
            val l = actuallyDrawingAreaLeft
            val t = actuallyDrawingAreaTop + mShadowReservedHeight
            mRight = actuallyDrawingAreaRight.toFloat()
            val b = actuallyDrawingAreaBottom - mShadowReservedHeight

            val sHeight = b - t
            mCenterX = (mRight + l) / 2
            mCenterY = (b + t) / 2

            mLeft = l.toFloat()
            mWidth = b - t
            bRight = l + mWidth
            val halfHeightOfS = mWidth / 2 // OfB
            mRadius = halfHeightOfS * 0.95f
            mOffset = mRadius * 0.2f // offset of switching
            mStrokeWidth = (halfHeightOfS - mRadius) * 2
            mOnLeftX = mRight - mWidth
            mOn2LeftX = mOnLeftX - mOffset
            mOffLeftX = l.toFloat()
            mOff2LeftX = mOffLeftX + mOffset
            mScale = 1 - mStrokeWidth / sHeight

            mBackgroundPath.reset()
            val bound = RectF()
            bound.top = t
            bound.bottom = b
            bound.left = l.toFloat()
            bound.right = l + sHeight
            mBackgroundPath.arcTo(bound, 90f, 180f)
            bound.left = mRight - sHeight
            bound.right = mRight
            mBackgroundPath.arcTo(bound, 270f, 180f)
            mBackgroundPath.close()

            mBound.left = mLeft
            mBound.right = bRight
            mBound.top = t + mStrokeWidth / 2  // bTop = sTop
            mBound.bottom = b - mStrokeWidth / 2 // bBottom = sBottom
            val bCenterX = (bRight + mLeft) / 2
            val bCenterY = (b + t) / 2

            val red = mShadowColor shr 16 and 0xFF
            val green = mShadowColor shr 8 and 0xFF
            val blue = mShadowColor and 0xFF
            mShadowGradient = RadialGradient(
                bCenterX, bCenterY, mRadius, Color.argb(200, red, green, blue),
                Color.argb(25, red, green, blue), Shader.TileMode.CLAMP
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!isCanVisibleDrawing) return
        val isOn = (mCheckedState == STATE_SWITCH_ON || mCheckedState == STATE_SWITCH_ON2)
        mPaint.color = if (isOn) mAccentColor else mOffColor
        mPaint.style = Paint.Style.FILL
        canvas?.drawPath(mBackgroundPath, mPaint)

        mAnim1 = if (mAnim1 - mAnimationSpeed > 0) mAnim1 - mAnimationSpeed else 0f
        mAnim2 = if (mAnim2 - mAnimationSpeed > 0) mAnim2 - mAnimationSpeed else 0f
        val dsAnim = mInterpolator.getInterpolation(mAnim1)
        val dbAnim = mInterpolator.getInterpolation(mAnim2)
        val scale = mScale * if (isOn) dsAnim else 1 - dsAnim
        val scaleOffset = (mRight - mCenterX - mRadius) * if (isOn) 1 - dsAnim else dsAnim
        canvas?.save()
        canvas?.scale(scale, scale, mCenterX + scaleOffset, mCenterY)
        mPaint.color = Color.WHITE
        canvas?.drawPath(mBackgroundPath, mPaint)
        canvas?.restore()
        // To prepare center bar path
        canvas?.save()
        canvas?.translate(calcBTranslate(dbAnim), mShadowReservedHeight)
        val isState2 = mCheckedState == STATE_SWITCH_ON2 || mCheckedState == STATE_SWITCH_OFF2
        calcBPath(if (isState2) 1 - dbAnim else dbAnim)
        // Use center bar path to draw shadow
        if (isShadow) {
            mPaint.style = Paint.Style.FILL
            mPaint.shader = mShadowGradient
            canvas?.drawPath(mBarPath, mPaint)
            mPaint.shader = null
        }
        canvas?.translate(0f, -mShadowReservedHeight)
        // draw bar
        canvas?.scale(0.98f, 0.98f, mWidth / 2, mWidth / 2)
        mPaint.style = Paint.Style.FILL
        mPaint.color = -0x1
        canvas?.drawPath(mBarPath, mPaint)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth * 0.5f
        mPaint.color = if (isOn) mPrimaryDarkColor else mOffDarkColor
        canvas?.drawPath(mBarPath, mPaint)
        canvas?.restore()

        mPaint.reset()
        if (mAnim1 > 0 || mAnim2 > 0) invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        if (isEnabled && (mCheckedState == STATE_SWITCH_ON || mCheckedState == STATE_SWITCH_OFF) && (mAnim1 * mAnim2 == 0f)) when (event?.action) {

            MotionEvent.ACTION_UP -> {
                mLastCheckedState = mCheckedState
                mAnim2 = 1f

                if (mCheckedState == STATE_SWITCH_OFF) {
                    setChecked(true, callback = false)
                    if (mListener != null) {
                        mListener?.onCheckedChanged(this, true)
                    }
                } else if (mCheckedState == STATE_SWITCH_ON) {
                    setChecked(false, callback = false)
                    if (mListener != null) {
                        mListener?.onCheckedChanged(this, false)
                    }
                }
            }
        }
        return true
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = SavedState(super.onSaveInstanceState())
        state.checked = mChecked
        return state
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state ?: return
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mChecked = savedState.checked
        mCheckedState = if (mChecked) STATE_SWITCH_ON else STATE_SWITCH_OFF
        invalidate()
    }

    private fun calcBTranslate(percent: Float): Float {
        var result = 0f
        when (mCheckedState - mLastCheckedState) {
            1 -> if (mCheckedState == STATE_SWITCH_OFF2) {
                result = mOffLeftX // off -> off2
            } else if (mCheckedState == STATE_SWITCH_ON) {
                result = mOnLeftX - (mOnLeftX - mOn2LeftX) * percent // on2 -> on
            }
            2 -> if (mCheckedState == STATE_SWITCH_ON) {
                result = mOnLeftX - (mOnLeftX - mOffLeftX) * percent // off2 -> on
            } else if (mCheckedState == STATE_SWITCH_ON2) {
                result = mOn2LeftX - (mOn2LeftX - mOffLeftX) * percent  // off -> on2
            }
            3 -> result = mOnLeftX - (mOnLeftX - mOffLeftX) * percent // off -> on
            -1 -> if (mCheckedState == STATE_SWITCH_ON2) {
                result = mOn2LeftX + (mOnLeftX - mOn2LeftX) * percent // on -> on2
            } else if (mCheckedState == STATE_SWITCH_OFF) {
                result = mOffLeftX  // off2 -> off
            }
            -2 -> if (mCheckedState == STATE_SWITCH_OFF) {
                result = mOffLeftX + (mOn2LeftX - mOffLeftX) * percent  // on2 -> off
            } else if (mCheckedState == STATE_SWITCH_OFF2) {
                result = mOff2LeftX + (mOnLeftX - mOff2LeftX) * percent  // on -> off2
            }
            -3 -> result = mOffLeftX + (mOnLeftX - mOffLeftX) * percent  // on -> off
            0 -> if (mCheckedState == STATE_SWITCH_OFF) {
                result = mOffLeftX //  off -> off
            } else if (mCheckedState == STATE_SWITCH_ON) {
                result = mOnLeftX // on -> on
            }
            else // initShape
            -> if (mCheckedState == STATE_SWITCH_OFF) {
                result = mOffLeftX
            } else if (mCheckedState == STATE_SWITCH_ON) {
                result = mOnLeftX
            }
        }
        return result - mOffLeftX
    }

    private fun calcBPath(percent: Float) {
        mBarPath.reset()
        mBound.left = mLeft + mStrokeWidth / 2
        mBound.right = bRight - mStrokeWidth / 2
        mBarPath.arcTo(mBound, 90f, 180f)
        mBound.left = mLeft + percent * mOffset + mStrokeWidth / 2
        mBound.right = bRight + percent * mOffset - mStrokeWidth / 2
        mBarPath.arcTo(mBound, 270f, 180f)
        mBarPath.close()
    }

    /**
     * 设置选择状态
     */
    fun setChecked(checked: Boolean, callback: Boolean) {
        val newState = if (checked) STATE_SWITCH_ON else STATE_SWITCH_OFF
        if (newState == mCheckedState) {
            return
        }
        if (newState == STATE_SWITCH_ON && (mCheckedState == STATE_SWITCH_OFF || mCheckedState == STATE_SWITCH_OFF2) || newState == STATE_SWITCH_OFF && (mCheckedState == STATE_SWITCH_ON || mCheckedState == STATE_SWITCH_ON2)) {
            mAnim1 = 1f
        }
        mAnim2 = 1f

        if (!mChecked && newState == STATE_SWITCH_ON) {
            mChecked = true
        } else if (mChecked && newState == STATE_SWITCH_OFF) {
            mChecked = false
        }
        mLastCheckedState = mCheckedState
        mCheckedState = newState
        postInvalidate()

        if (callback && mListener != null) {
            mListener?.onCheckedChanged(this, checked)
        }
    }

    fun setColor(newColorPrimary: Int, newColorPrimaryDark: Int) {
        setColor(newColorPrimary, newColorPrimaryDark, mOffColor, mOffDarkColor)
    }

    fun setColor(newColorPrimary: Int, newColorPrimaryDark: Int, newColorOff: Int, newColorOffDark: Int) {
        setColor(newColorPrimary, newColorPrimaryDark, newColorOff, newColorOffDark, mShadowColor)
    }

    fun setColor(
        newColorPrimary: Int,
        newColorPrimaryDark: Int,
        newColorOff: Int,
        newColorOffDark: Int,
        newColorShadow: Int
    ) {
        mAccentColor = newColorPrimary
        mPrimaryDarkColor = newColorPrimaryDark
        mOffColor = newColorOff
        mOffDarkColor = newColorOffDark
        mShadowColor = newColorShadow
        invalidate()
    }

    /**
     * 设置选择状态（默认会回调监听器）
     */
    fun setChecked(checked: Boolean) {
        setChecked(checked, true) // 回调监听器
    }

    companion object {

        interface OnCheckedChangeListener {
            fun onCheckedChanged(button: SwitchButton, isChecked: Boolean)
        }

        private class SavedState : BaseSavedState {
            var checked: Boolean = false

            constructor(superState: Parcelable?) : super(superState)
            constructor(i: Parcel) : super(i) {
                checked = 1 == i.readInt()
            }

            override fun writeToParcel(out: Parcel?, flags: Int) {
                super.writeToParcel(out, flags)
                out?.writeInt(if (checked) 1 else 0)
            }

            override fun describeContents(): Int {
                return 0
            }

            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return Companion.SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return newArray(size)
                }
            }
        }
    }
}