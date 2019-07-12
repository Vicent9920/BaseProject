@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded","NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS","LeakingThis")
@file:SuppressLint("Recycle")

package com.vincent.baselibrary.widget.radius.delegate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import com.vincent.baselibrary.R
import com.vincent.baselibrary.util.ResourceUtil
import com.vincent.baselibrary.widget.radius.RadiusSwitch


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/7/10 0010 <p>
 * <p>@update 2019/7/10 0010<p>
 * <p>版本号：1<p>
 *
 */
open class RadiusViewDelegate<T>(
    val view: View,
    open val context: Context,
    open val attrs: AttributeSet?
)where T:RadiusViewDelegate<T>{

    internal var mTypedArray: TypedArray

    private val mBackground = GradientDrawable()
    private val mBackgroundPressed = GradientDrawable()
    private val mBackgroundDisabled = GradientDrawable()
    private val mBackgroundSelected = GradientDrawable()
    private val mBackgroundChecked = GradientDrawable()

    //以下为xml属性对应解析参数

    private var mBackgroundColor: Int = 0
    private var mBackgroundPressedColor: Int = 0
    private var mBackgroundDisabledColor: Int = 0
    private var mBackgroundSelectedColor: Int = 0
    private var mBackgroundCheckedColor: Int = 0
    internal var mBackgroundPressedAlpha = 0

    private var mStrokeColor: Int = 0
    private var mStrokePressedColor: Int = 0
    private var mStrokeDisabledColor: Int = 0
    private var mStrokeSelectedColor: Int = 0
    private var mStrokeCheckedColor: Int = 0
    internal var mStrokePressedAlpha = 0

    private var mStrokeWidth: Int = 0
    private var mStrokeDashWidth: Float = 0f
    private var mStrokeDashGap: Float = 0f

    private var mRadiusHalfHeightEnable: Boolean = false
    private var mWidthHeightEqualEnable: Boolean = false

    private var mRadius: Float = 0f
    private var mTopLeftRadius: Float = 0f
    private var mTopRightRadius: Float = 0f
    private var mBottomLeftRadius: Float = 0f
    private var mBottomRightRadius: Float = 0f

    private var mRippleColor: Int = 0
    protected var mRippleEnable: Boolean = false
    private var mSelected: Boolean = false
    private var mEnterFadeDuration = 0
    private var mExitFadeDuration = 0
    //以上为xml属性对应解析参数

    internal var mStateChecked = android.R.attr.state_checked
    internal var mStateSelected = android.R.attr.state_selected
    internal var mStatePressed = android.R.attr.state_pressed
    internal var mStateDisabled = -android.R.attr.state_enabled
    private val mRadiusArr = FloatArray(8)

    private var mOnSelectedChangeListener: OnSelectedChangeListener? = null

    init {
        this.mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RadiusSwitch)

        initAttributes()
        view.isSelected = mSelected
        setBackgroundPressedAlpha(mBackgroundPressedAlpha)
            .setStrokePressedAlpha(mStrokePressedAlpha)
            .setSelected(mSelected)
    }

    open fun initAttributes() {
        mBackgroundColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_backgroundColor, Integer.MAX_VALUE)
        mBackgroundPressedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_backgroundPressedColor, Integer.MAX_VALUE)
        mBackgroundDisabledColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_backgroundDisabledColor, Integer.MAX_VALUE)
        mBackgroundSelectedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_backgroundSelectedColor, Integer.MAX_VALUE)
        mBackgroundCheckedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_backgroundCheckedColor, Integer.MAX_VALUE)
        mBackgroundPressedAlpha =
            mTypedArray.getInteger(R.styleable.RadiusSwitch_rv_backgroundPressedAlpha, mBackgroundPressedAlpha)

        mStrokeColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_strokeColor, Integer.MAX_VALUE)
        mStrokePressedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_strokePressedColor, Integer.MAX_VALUE)
        mStrokeDisabledColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_strokeDisabledColor, Integer.MAX_VALUE)
        mStrokeSelectedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_strokeSelectedColor, Integer.MAX_VALUE)
        mStrokeCheckedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_strokeCheckedColor, Integer.MAX_VALUE)
        mStrokePressedAlpha =
            mTypedArray.getInteger(R.styleable.RadiusSwitch_rv_strokePressedAlpha, mStrokePressedAlpha)

        mStrokeWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_strokeWidth, 0)
        mStrokeDashWidth = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_strokeDashWidth, 0f)
        mStrokeDashGap = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_strokeDashGap, 0f)

        mRadiusHalfHeightEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_radiusHalfHeightEnable, false)
        mWidthHeightEqualEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_widthHeightEqualEnable, false)

        mRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_radius, 0f)
        mTopLeftRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_topLeftRadius, 0f)
        mTopRightRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_topRightRadius, 0f)
        mBottomLeftRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_bottomLeftRadius, 0f)
        mBottomRightRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_bottomRightRadius, 0f)

        mRippleColor = mTypedArray.getColor(
            R.styleable.RadiusSwitch_rv_rippleColor,
            ResourceUtil.getColor(context, R.color.colorRadiusDefaultRipple)
        )
        mRippleEnable = mTypedArray.getBoolean(
            R.styleable.RadiusSwitch_rv_rippleEnable,
            view.isClickable && !(view is RadiusSwitch)
        )
        mSelected = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_selected, false)
        mEnterFadeDuration = mTypedArray.getInteger(R.styleable.RadiusSwitch_rv_enterFadeDuration, 0)
        mExitFadeDuration = mTypedArray.getInteger(R.styleable.RadiusSwitch_rv_exitFadeDuration, 0)
        mTypedArray.recycle()
    }

    open fun back(): T {
        return this as T
    }

    /**
     * 设置常态背景色
     *
     * @param color
     * @return
     */
    open fun setBackgroundColor(color: Int): T {
        this.mBackgroundColor = color
        return back()
    }

    /**
     * 设置按下状态背景色
     *
     * @param color
     * @return
     */
    fun setBackgroundPressedColor(color: Int): T {
        this.mBackgroundPressedColor = color
        return back()
    }

    /**
     * 设置不可操作状态下背景色
     *
     * @param color
     * @return
     */
    fun setBackgroundDisabledColor(color: Int): T {
        this.mBackgroundDisabledColor = color
        return back()
    }

    /**
     * 设置selected状态下背景色
     *
     * @param color
     * @return
     */
    fun setBackgroundSelectedColor(color: Int): T {
        this.mBackgroundSelectedColor = color
        return back()
    }

    /**
     * 设置checked状态背景色
     *
     * @param color
     * @return
     */
    fun setBackgroundCheckedColor(color: Int): T {
        this.mBackgroundCheckedColor = color
        return back()
    }

    /**
     * 背景色按下状态透明度(0-255默认102 仅当未设置backgroundPressedColor有效)
     *
     * @param source
     * @return
     */
    fun setBackgroundPressedAlpha(source: Int): T {
        var alpha = source
        if (alpha > 255) {
            alpha = 255
        } else if (alpha < 0) {
            alpha = 0
        }
        this.mBackgroundPressedAlpha = alpha
        return back()
    }

    /**
     * 设置边框线常态颜色
     *
     * @param strokeColor
     * @return
     */
    fun setStrokeColor(strokeColor: Int): T {
        this.mStrokeColor = strokeColor
        return back()
    }

    /**
     * 设置边框线按下状态颜色
     *
     * @param strokePressedColor
     * @return
     */
    fun setStrokePressedColor(strokePressedColor: Int): T {
        this.mStrokePressedColor = strokePressedColor
        return back()
    }

    /**
     * 设置边框线不可点击状态下颜色
     *
     * @param strokeDisabledColor
     * @return
     */
    fun setStrokeDisabledColor(strokeDisabledColor: Int): T {
        this.mStrokeDisabledColor = strokeDisabledColor
        return back()
    }

    /**
     * 设置边框线selected状态颜色
     *
     * @param strokeSelectedColor
     * @return
     */
    fun setStrokeSelectedColor(strokeSelectedColor: Int): T {
        this.mStrokeSelectedColor = strokeSelectedColor
        return back()
    }

    /**
     * 设置边框checked状态颜色
     *
     * @param strokeCheckedColor
     * @return
     */
    fun setStrokeCheckedColor(strokeCheckedColor: Int): T {
        this.mStrokeCheckedColor = strokeCheckedColor
        return back()
    }

    /**
     * 边框色按下状态透明度(0-255默认102 仅当未设置strokePressedColor有效)
     *
     * @param source
     * @return
     */
    fun setStrokePressedAlpha(source: Int): T {
        var alpha = source
        if (alpha > 255) {
            alpha = 255
        } else if (alpha < 0) {
            alpha = 0
        }
        this.mStrokePressedAlpha = alpha
        return back()
    }

    /**
     * 设置边框线宽度(粗细)
     *
     * @param strokeWidth
     * @return
     */
    fun setStrokeWidth(strokeWidth: Int): T {
        this.mStrokeWidth = strokeWidth
        return back()
    }

    /**
     * 设置虚线宽度
     *
     * @param strokeDashWidth
     * @return
     */
    fun setStrokeDashWidth(strokeDashWidth: Float): T {
        this.mStrokeDashWidth = strokeDashWidth
        return back()
    }

    /**
     * 设置虚线间隔
     *
     * @param strokeDashGap
     * @return
     */
    fun setStrokeDashGap(strokeDashGap: Float): T {
        this.mStrokeDashGap = strokeDashGap
        return back()
    }

    /**
     * 设置半高度圆角
     *
     * @param enable
     * @return
     */
    fun setRadiusHalfHeightEnable(enable: Boolean): T {
        this.mRadiusHalfHeightEnable = enable
        return back()
    }

    /**
     * 设置宽高相等
     *
     * @param enable
     * @return
     */
    fun setWidthHeightEqualEnable(enable: Boolean): T {
        this.mWidthHeightEqualEnable = enable
        return back()
    }

    /**
     * 设置整体圆角弧度
     *
     * @param radius
     * @return
     */
    fun setRadius(radius: Float): T {
        this.mRadius = radius
        return back()
    }

    /**
     * 设置左上圆角
     *
     * @param radius
     * @return
     */
    fun setTopLeftRadius(radius: Float): T {
        this.mTopLeftRadius = radius
        return back()
    }

    /**
     * 设置右上圆角
     *
     * @param radius
     * @return
     */
    fun setTopRightRadius(radius: Float): T {
        this.mTopRightRadius = radius
        return back()
    }

    /**
     * 设置左下圆角
     *
     * @param radius
     * @return
     */
    fun setBottomLeftRadius(radius: Float): T {
        this.mBottomLeftRadius = radius
        return back()
    }

    /**
     * 设置右下圆角
     *
     * @param radius
     * @return
     */
    fun setBottomRightRadius(radius: Float): T {
        this.mBottomRightRadius = radius
        return back()
    }

    /**
     * 设置水波纹颜色 5.0以上支持
     *
     * @param color
     * @return
     */
    fun setRippleColor(color: Int): T {
        this.mRippleColor = color
        return back()
    }

    /**
     * 设置是否支持水波纹效果--5.0及以上
     *
     * @param enable
     * @return
     */
    fun setRippleEnable(enable: Boolean): T {
        this.mRippleEnable = enable
        return back()
    }

    /**
     * 设置选中状态变换监听
     *
     * @param listener
     * @return
     */
    fun setOnSelectedChangeListener(listener: OnSelectedChangeListener): T {
        this.mOnSelectedChangeListener = listener
        return back()
    }

    /**
     * @param selected
     */
    fun setSelected(selected: Boolean) {
        if (mSelected != selected) {
            mSelected = selected
            mOnSelectedChangeListener?.onSelectedChanged(view, mSelected)

        }
        init()
    }

    /**
     * 设置状态切换延时
     *
     * @param duration
     * @return
     */
    fun setEnterFadeDuration(duration: Int): T {
        if (duration >= 0) {
            mEnterFadeDuration = duration
        }
        return back()
    }

    /**
     * 设置状态切换延时
     *
     * @param duration
     * @return
     */
    fun setExitFadeDuration(duration: Int): T {
        if (duration > 0) {
            mExitFadeDuration = duration
        }
        return back()
    }

    fun getRadius(): Float {
        return mRadius
    }

    fun getRadiusHalfHeightEnable(): Boolean {
        return mRadiusHalfHeightEnable
    }

    fun getWidthHeightEqualEnable(): Boolean {
        return mWidthHeightEqualEnable
    }

    /**
     * 设置 背景Drawable颜色线框色及圆角值
     *
     * @param gd
     * @param color
     * @param strokeColor
     */
    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        //任意值大于0执行
        if (mTopLeftRadius > 0 || mTopRightRadius > 0 || mBottomRightRadius > 0 || mBottomLeftRadius > 0) {
            mRadiusArr[0] = mTopLeftRadius
            mRadiusArr[1] = mTopLeftRadius
            mRadiusArr[2] = mTopRightRadius
            mRadiusArr[3] = mTopRightRadius
            mRadiusArr[4] = mBottomRightRadius
            mRadiusArr[5] = mBottomRightRadius
            mRadiusArr[6] = mBottomLeftRadius
            mRadiusArr[7] = mBottomLeftRadius
            gd.cornerRadii = mRadiusArr
        } else {
            gd.cornerRadius = mRadius
        }
        gd.setStroke(mStrokeWidth, getStrokeColor(strokeColor), mStrokeDashWidth, mStrokeDashGap)
        gd.setColor(getBackColor(color))
    }

    /**
     * 设置shape属性
     * 设置完所有属性后调用设置背景
     */
    fun init() {
        if (view is EditText) {
            Log.i("v", "click:" + view.isClickable() + ";enable:" + view.isEnabled())
        }
        //获取view当前drawable--用于判断是否通过默认属性设置背景
        val mDrawable = view.getBackground()
        //判断是否使用自定义颜色值
        val isSetBg = (mBackgroundColor != Integer.MAX_VALUE
                || mBackgroundPressedColor != Integer.MAX_VALUE
                || mBackgroundDisabledColor != Integer.MAX_VALUE
                || mBackgroundSelectedColor != Integer.MAX_VALUE
                || mStrokeWidth > 0 || mRadius > 0
                || mTopLeftRadius > 0 || mTopLeftRadius > 0 || mBottomLeftRadius > 0 || mBottomRightRadius > 0)

        setDrawable(mBackgroundChecked, mBackgroundCheckedColor, mStrokeCheckedColor)
        setDrawable(mBackgroundSelected, mBackgroundSelectedColor, mStrokeSelectedColor)
        setDrawable(mBackground, mBackgroundColor, mStrokeColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            && mRippleEnable
            && view.isEnabled()
            && view.isClickable()
        ) {//5.0以上且设置水波属性并且可操作
            val rippleDrawable = RippleDrawable(
                ColorStateList(
                    arrayOf(intArrayOf(mStatePressed), intArrayOf()),
                    intArrayOf(
                        if (mRippleColor != Integer.MAX_VALUE) mRippleColor else getBackColor(
                            mBackgroundPressedColor
                        ), mRippleColor
                    )
                ), getContentDrawable(mDrawable, isSetBg), null
            )
            view.setBackground(rippleDrawable)
        }
    }

    /**
     * 获取背景色
     *
     * @param source
     * @return
     */
    private fun getBackColor(source: Int): Int {
        var color = source
        if (color != Integer.MAX_VALUE) {
            return color
        }
        if (view.isSelected()) {
            color = mBackgroundSelectedColor
        } else if (view is CompoundButton) {
            if (view .isChecked) {
                color = mBackgroundCheckedColor
            }
        }
        color =
            if (color != Integer.MAX_VALUE) color else if (mBackgroundColor == Integer.MAX_VALUE) Color.WHITE else mBackgroundColor
        return if (view.isPressed && !mRippleEnable) calculateColor(color, mBackgroundPressedAlpha) else color
    }

    /**
     * 获取边框线颜色
     *
     * @param source
     * @return
     */
    private fun getStrokeColor(source: Int): Int {
        var color = source
        if (color != Integer.MAX_VALUE) {
            return color
        }
        if (view.isSelected) {
            color = mStrokeSelectedColor
        } else if (view is CompoundButton) {
            if (view .isChecked) {
                color = mStrokeCheckedColor
            }
        }
        color =
            if (color != Integer.MAX_VALUE) color else if (mStrokeColor == Integer.MAX_VALUE) Color.TRANSPARENT else mStrokeColor
        return if (view.isPressed() && !mRippleEnable) calculateColor(color, mStrokePressedAlpha) else color
    }

    /**
     * 水波纹效果完成后最终展示的背景Drawable
     *
     * @param mDrawable
     * @param isSetBg
     * @return
     */
    private fun getContentDrawable(mDrawable: Drawable, isSetBg: Boolean): Drawable {
        if (view is CompoundButton) {
            return if (!isSetBg)
                mDrawable
            else if (view .isChecked)
                mBackgroundChecked
            else if (view.isSelected)
                mBackgroundSelected
            else
                mBackground
        }
        return if (!isSetBg) mDrawable else if (view.isSelected()) mBackgroundSelected else mBackground
    }

    protected fun dp2px(dipValue: Float): Int {
        val scale = Resources.getSystem().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 给颜色值添加透明度
     *
     * @param color color值
     * @param alpha alpha值 0-255
     * @return 最终的状态栏颜色
     */
    protected fun calculateColor(color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        red = (red * a + 0.5).toInt()
        green = (green * a + 0.5).toInt()
        blue = (blue * a + 0.5).toInt()
        return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
    }


    interface OnSelectedChangeListener {
        /**
         * Called when the checked state of a View has changed.
         *
         * @param view       The View whose state has changed.
         * @param isSelected The new selected state of buttonView.
         */
        fun onSelectedChanged(view: View, isSelected: Boolean)
    }
}
class RadiusViewDelegateImp( view: View,context: Context, attrs: AttributeSet?) :
    RadiusViewDelegate<RadiusViewDelegateImp>(view,context,attrs)