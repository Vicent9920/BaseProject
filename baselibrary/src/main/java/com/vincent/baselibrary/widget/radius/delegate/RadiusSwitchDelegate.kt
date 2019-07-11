@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded","NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.widget.Switch
import com.vincent.baselibrary.R
import com.vincent.baselibrary.util.ResourceUtil


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/7/11 0011 <p>
 * <p>@update 2019/7/11 0011<p>
 * <p>版本号：1<p>
 *
 */
open class RadiusSwitchDelegate <T : RadiusCompoundDelegate<T>>(val switch: Switch, context: Context, attrs: AttributeSet?) :
    RadiusCompoundDelegate<T>(switch, context, attrs) {


    private lateinit var mStateThumbDrawable: StateListDrawable
    private lateinit var mStateTrackDrawable: StateListDrawable

    /**
     * 以下为xml对应属性解析
     */
    private var mThumbDrawableWidth: Int = 0
    private var mThumbDrawableHeight: Int = 0
    private lateinit var mThumbDrawable: Drawable
    private lateinit var mThumbPressedDrawable: Drawable
    private lateinit var mThumbDisabledDrawable: Drawable
    private lateinit var mThumbSelectedDrawable: Drawable
    private lateinit var mThumbCheckedDrawable: Drawable

    private var mThumbStrokeColor: Int = 0
    private var mThumbStrokePressedColor: Int = 0
    private var mThumbStrokeDisabledColor: Int = 0
    private var mThumbStrokeSelectedColor: Int = 0
    private var mThumbStrokeCheckedColor: Int = 0
    private var mThumbStrokeWidth: Int = 0
    private var mThumbRadius: Float = 0.toFloat()

    private var mTrackDrawableWidth: Int = 0
    private var mTrackDrawableHeight: Int = 0
    private lateinit var mTrackDrawable: Drawable
    private lateinit var mTrackPressedDrawable: Drawable
    private lateinit var mTrackDisabledDrawable: Drawable
    private lateinit var mTrackSelectedDrawable: Drawable
    private lateinit var mTrackCheckedDrawable: Drawable

    private var mTrackStrokeColor: Int = 0
    private var mTrackStrokePressedColor: Int = 0
    private var mTrackStrokeDisabledColor: Int = 0
    private var mTrackStrokeSelectedColor: Int = 0
    private var mTrackStrokeCheckedColor: Int = 0
    private var mTrackStrokeWidth: Int = 0
    private var mTrackRadius: Float = 0.toFloat()
    private var mColorAccent: Int = 0
    private var mColorDefault: Int = 0

    init {
        mColorAccent = ResourceUtil.getAttrColor(context,R.attr.colorAccent)
        mColorDefault = Color.LTGRAY
    }

    override fun initAttributes() {
        mThumbDrawableWidth =
            mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbDrawableWidth, dp2px(24f))
        mThumbDrawableHeight =
            mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbDrawableHeight, dp2px(24f))
        val thumbDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbDrawable)
        val thumbPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbPressedDrawable)
        val thumbDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbDisabledDrawable)
        val thumbSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbSelectedDrawable)
        val thumbCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbCheckedDrawable)
        mThumbStrokeColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeColor, mColorDefault)
        mThumbStrokePressedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokePressedColor, mThumbStrokeColor)
        mThumbStrokeDisabledColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeDisabledColor, mThumbStrokeColor)
        mThumbStrokeSelectedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeSelectedColor, mThumbStrokeColor)
        mThumbStrokeCheckedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeCheckedColor, mColorAccent)
        mThumbStrokeWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbStrokeWidth, dp2px(2f))
        mThumbRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_thumbRadius, 100f)

        //轨道属性
        mTrackDrawableWidth =
            mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackDrawableWidth, dp2px(48f))
        mTrackDrawableHeight =
            mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackDrawableHeight, dp2px(24f))
        val trackDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackDrawable)
        val trackPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackPressedDrawable)
        val trackDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackDisabledDrawable)
        val trackSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackSelectedDrawable)
        val trackCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackCheckedDrawable)
        mTrackStrokeColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeColor, mColorDefault)
        mTrackStrokePressedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokePressedColor, mTrackStrokeColor)
        mTrackStrokeDisabledColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeDisabledColor, mTrackStrokeColor)
        mTrackStrokeSelectedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeSelectedColor, mThumbStrokeColor)
        mTrackStrokeCheckedColor =
            mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeCheckedColor, mColorAccent)
        mTrackStrokeWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackStrokeWidth, dp2px(2f))
        mTrackRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_trackRadius, 100f)

        mThumbDrawable = thumbDrawable ?: ColorDrawable(Color.WHITE)
        mThumbPressedDrawable = if (trackPressedDrawable == null) mThumbDrawable else thumbPressedDrawable
        mThumbDisabledDrawable = thumbDisabledDrawable ?: mThumbDrawable
        mThumbSelectedDrawable = thumbSelectedDrawable ?: mThumbDrawable
        mThumbCheckedDrawable = thumbCheckedDrawable ?: mThumbDrawable

        mTrackDrawable = trackDrawable ?: ColorDrawable(mColorDefault)

        mTrackPressedDrawable = trackPressedDrawable ?: mTrackDrawable
        mTrackDisabledDrawable = trackDisabledDrawable ?: mTrackDrawable
        mTrackSelectedDrawable = trackSelectedDrawable ?: mTrackDrawable
        mTrackCheckedDrawable = trackCheckedDrawable ?: ColorDrawable(mColorAccent)
        super.initAttributes()
    }

    /**
     * 设置滑块宽度
     *
     * @param width
     * @return
     */
    fun setThumbDrawableWidth(width: Int): T {
        mThumbDrawableWidth = width
        return back()
    }

    /**
     * 设置滑块高度
     *
     * @param height
     * @return
     */
    fun setThumbDrawableHeight(height: Int): T {
        mThumbDrawableHeight = height
        return back()
    }

    /**
     * 设置滑块默认状态drawable
     *
     * @param drawable
     * @return
     */
    fun setThumbDrawable(drawable: Drawable?): T {
        drawable?.let {
            mThumbDrawable = it
        }

        return back()
    }

    fun setThumbDrawable(resId: Int): T {
        return setThumbDrawable(getDrawable(resId))
    }

    /**
     * 设置滑块按下状态drawable
     *
     * @param drawable
     * @return
     */
    fun setThumbPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mThumbPressedDrawable = it
        }

        return back()
    }

    fun setThumbPressedDrawable(resId: Int): T {
        return setThumbPressedDrawable(getDrawable(resId))
    }

    fun setThumbDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mThumbDisabledDrawable = it
        }

        return back()
    }

    fun setThumbDisabledDrawable(resId: Int): T {
        return setThumbDisabledDrawable(getDrawable(resId))
    }

    fun setThumbSelectedDrawable(thumbSelectedDrawable: Drawable?): T {
        thumbSelectedDrawable?.let {
            mThumbSelectedDrawable = it
        }

        return back()
    }

    fun setThumbSelectedDrawable(resId: Int): T {
        return setThumbSelectedDrawable(getDrawable(resId))
    }

    /**
     * 设置滑块checked状态drawable
     *
     * @param drawable
     * @return
     */
    fun setThumbCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mThumbCheckedDrawable = it
        }

        return back()
    }

    fun setThumbCheckedDrawable(resId: Int): T {
        return setThumbCheckedDrawable(getDrawable(resId))
    }

    /**
     * 设置滑块边框颜色
     *
     * @param color
     * @return
     */
    fun setThumbStrokeColor(color: Int): T {
        mThumbStrokeColor = color
        return back()
    }

    fun setThumbStrokePressedColor(color: Int): T {
        mThumbStrokePressedColor = color
        return back()
    }

    fun setThumbStrokeDisabledColor(color: Int): T {
        mThumbStrokeDisabledColor = color
        return back()
    }

    fun setThumbStrokeSelectedColor(color: Int): T {
        mThumbStrokeSelectedColor = color
        return back()
    }

    /**
     * 设置滑块边框选中状态颜色
     *
     * @param color
     * @return
     */
    fun setThumbStrokeCheckedColor(color: Int): T {
        mThumbStrokeCheckedColor = color
        return back()
    }

    /**
     * 设置滑块边框宽度
     *
     * @param width
     * @return
     */
    fun setThumbStrokeWidth(width: Int): T {
        mThumbStrokeWidth = width
        return back()
    }

    /**
     * 设置边框圆角弧度
     *
     * @param radius
     * @return
     */
    fun setThumbRadius(radius: Float): T {
        mThumbRadius = radius
        return back()
    }

    /**
     * 设置轨道宽度
     *
     * @param width
     * @return
     */
    fun setTrackDrawableWidth(width: Int): T {
        mTrackDrawableWidth = width
        return back()
    }

    /**
     * 设置轨道高度
     *
     * @param height
     * @return
     */
    fun setTrackDrawableHeight(height: Int): T {
        mTrackDrawableHeight = height
        return back()
    }

    /**
     * 设置轨道默认drawable
     *
     * @param drawable
     * @return
     */
    fun setTrackDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTrackDrawable = it
        }

        return back()
    }

    fun setTrackDrawable(resId: Int): T {
        return setTrackDrawable(getDrawable(resId))
    }

    fun setTrackPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTrackPressedDrawable = it
        }

        return back()
    }

    fun setTrackPressedDrawable(resId: Int): T {
        return setTrackPressedDrawable(getDrawable(resId))
    }

    fun setTrackDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTrackDisabledDrawable = it
        }

        return back()
    }

    fun setTrackDisabledDrawable(resId: Int): T {
        return setTrackDisabledDrawable(getDrawable(resId))
    }

    fun setTrackSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTrackSelectedDrawable = it
        }

        return back()
    }

    fun setTrackSelectedDrawable(resId: Int): T {
        return setTrackSelectedDrawable(getDrawable(resId))
    }

    /**
     * 设置轨道checked状态drawable
     *
     * @param drawable
     * @return
     */
    fun setTrackCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTrackCheckedDrawable = it
        }

        return back()
    }

    fun setTrackCheckedDrawable(resId: Int): T {
        return setTrackCheckedDrawable(getDrawable(resId))
    }

    /**
     * 设置轨道边框颜色
     *
     * @param color
     * @return
     */
    fun setTrackStrokeColor(color: Int): T {
        mTrackStrokeColor = color
        return back()
    }

    fun setTrackStrokePressedColor(color: Int): T {
        mTrackStrokePressedColor = color
        return back()
    }

    fun setTrackStrokeDisabledColor(color: Int): T {
        mTrackStrokeDisabledColor = color
        return back()
    }

    fun setTrackStrokeSelectedColor(color: Int): T {
        mTrackStrokeSelectedColor = color
        return back()
    }

    /**
     * 设置轨道边框checked状态颜色
     *
     * @param color
     * @return
     */
    fun setTrackStrokeCheckedColor(color: Int): T {
        mTrackStrokeCheckedColor = color
        return back()
    }

    /**
     * 设置轨道边框宽度
     *
     * @param width
     * @return
     */
    fun setTrackStrokeWidth(width: Int): T {
        mTrackStrokeWidth = width
        return back()
    }

    /**
     * 设置轨道圆角弧度
     *
     * @param radius
     * @return
     */
    fun setTrackRadius(radius: Float): T {
        mTrackRadius = radius
        return back()
    }

    /**
     * 设置Drawable相关属性
     */
    private fun setSwitchDrawable() {

        mStateThumbDrawable = StateListDrawable()
        mStateTrackDrawable = StateListDrawable()
        mStateThumbDrawable.addState(
            intArrayOf(mStateChecked),
            getStateDrawable(
                mThumbCheckedDrawable,
                mThumbRadius,
                mThumbDrawableWidth,
                mThumbDrawableHeight,
                mThumbStrokeWidth,
                mThumbStrokeCheckedColor
            )
        )
        mStateThumbDrawable.addState(
            intArrayOf(mStateSelected),
            getStateDrawable(
                mThumbSelectedDrawable,
                mThumbRadius,
                mThumbDrawableWidth,
                mThumbDrawableHeight,
                mThumbStrokeWidth,
                mThumbStrokeSelectedColor
            )
        )
        mStateThumbDrawable.addState(
            intArrayOf(mStatePressed),
            getStateDrawable(
                mThumbPressedDrawable,
                mThumbRadius,
                mThumbDrawableWidth,
                mThumbDrawableHeight,
                mThumbStrokeWidth,
                mThumbStrokePressedColor
            )
        )
        mStateThumbDrawable.addState(
            intArrayOf(mStateDisabled),
            getStateDrawable(
                mThumbDisabledDrawable,
                mThumbRadius,
                mThumbDrawableWidth,
                mThumbDrawableHeight,
                mThumbStrokeWidth,
                mThumbStrokeDisabledColor
            )
        )
        mStateThumbDrawable.addState(
            intArrayOf(),
            getStateDrawable(
                mThumbDrawable,
                mThumbRadius,
                mThumbDrawableWidth,
                mThumbDrawableHeight,
                mThumbStrokeWidth,
                mThumbStrokeColor
            )
        )

        mTrackDrawableHeight = 0
        mStateTrackDrawable.addState(
            intArrayOf(mStateChecked),
            getStateDrawable(
                mTrackCheckedDrawable,
                mTrackRadius,
                mTrackDrawableWidth,
                mTrackDrawableHeight,
                mTrackStrokeWidth,
                mTrackStrokeCheckedColor
            )
        )
        mStateTrackDrawable.addState(
            intArrayOf(mStateSelected),
            getStateDrawable(
                mTrackSelectedDrawable,
                mTrackRadius,
                mTrackDrawableWidth,
                mTrackDrawableHeight,
                mTrackStrokeWidth,
                mTrackStrokeSelectedColor
            )
        )
        mStateTrackDrawable.addState(
            intArrayOf(mStatePressed),
            getStateDrawable(
                mTrackPressedDrawable,
                mTrackRadius,
                mTrackDrawableWidth,
                mTrackDrawableHeight,
                mTrackStrokeWidth,
                mTrackStrokePressedColor
            )
        )
        mStateTrackDrawable.addState(
            intArrayOf(mStateDisabled),
            getStateDrawable(
                mTrackDisabledDrawable,
                mTrackRadius,
                mTrackDrawableWidth,
                mTrackDrawableHeight,
                mTrackStrokeWidth,
                mTrackStrokeDisabledColor
            )
        )
        mStateTrackDrawable.addState(
            intArrayOf(),
            getStateDrawable(
                mTrackDrawable,
                mTrackRadius,
                mTrackDrawableWidth,
                mTrackDrawableHeight,
                mTrackStrokeWidth,
                mTrackStrokeColor
            )
        )
        switch.thumbDrawable = mStateThumbDrawable
        switch.trackDrawable = mStateTrackDrawable
    }


    internal fun getStateDrawable(
        source: Drawable,
        radius: Float,
        width: Int,
        height: Int,
        strokeWidth: Int,
        strokeColor: Int
    ): Drawable? {
        var drawable:Drawable? = source
        drawable = getStateDrawable(drawable, radius, width, height)
        if (drawable is GradientDrawable) {
            val gradientDrawable = drawable
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }
        return drawable
    }
}
class RadiusSwitchDelegateImp(switch: Switch, context: Context, attrs: AttributeSet?):
    RadiusSwitchDelegate<RadiusSwitchDelegateImp>(switch, context, attrs)