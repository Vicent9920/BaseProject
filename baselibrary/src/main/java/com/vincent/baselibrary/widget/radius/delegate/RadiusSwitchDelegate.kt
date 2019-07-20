@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
import android.content.res.TypedArray
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
open class RadiusSwitchDelegate<out T> constructor(private val switch: Switch, context: Context, attrs: AttributeSet?) :
    RadiusCompoundDelegate<T>(switch, context, attrs) where T : RadiusCompoundDelegate<T> {


    private var mStateThumbDrawable: StateListDrawable? = null
    private var mStateTrackDrawable: StateListDrawable? = null

    /**
     * 以下为xml对应属性解析
     */
    private var mThumbDrawableWidth: Int = 0
    private var mThumbDrawableHeight: Int = 0
    private var mThumbDrawable: Drawable? = null
    private var mThumbPressedDrawable: Drawable? = null
    private var mThumbDisabledDrawable: Drawable? = null
    private var mThumbSelectedDrawable: Drawable? = null
    private var mThumbCheckedDrawable: Drawable? = null

    private var mThumbStrokeColor: Int = 0
    private var mThumbStrokePressedColor: Int = 0
    private var mThumbStrokeDisabledColor: Int = 0
    private var mThumbStrokeSelectedColor: Int = 0
    private var mThumbStrokeCheckedColor: Int = 0
    private var mThumbStrokeWidth: Int = 0
    private var mThumbRadius: Float = 0f

    private var mTrackDrawableWidth: Int = 0
    private var mTrackDrawableHeight: Int = 0
    private var mTrackDrawable: Drawable? = null
    private var mTrackPressedDrawable: Drawable? = null
    private var mTrackDisabledDrawable: Drawable? = null
    private var mTrackSelectedDrawable: Drawable? = null
    private var mTrackCheckedDrawable: Drawable? = null

    private var mTrackStrokeColor: Int = 0
    private var mTrackStrokePressedColor: Int = 0
    private var mTrackStrokeDisabledColor: Int = 0
    private var mTrackStrokeSelectedColor: Int = 0
    private var mTrackStrokeCheckedColor: Int = 0
    private var mTrackStrokeWidth: Int = 0
    private var mTrackRadius: Float = 0f
    private var mColorAccent: Int = 0
    private var mColorDefault: Int = 0

    init {
        setSwitchDrawable()
    }

    override fun initAttributes(typedArray: TypedArray) {

        mColorAccent = ResourceUtil.getAttrColor(context, R.attr.colorAccent)
        mColorDefault = Color.LTGRAY
        mThumbDrawableWidth =
            typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_thumbDrawableWidth, dp2px(24f))
        mThumbDrawableHeight =
            typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_thumbDrawableHeight, dp2px(24f))
        mThumbDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_thumbDrawable)
        mThumbPressedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_thumbPressedDrawable)
        mThumbDisabledDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_thumbDisabledDrawable)
        mThumbSelectedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_thumbSelectedDrawable)
        mThumbCheckedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_thumbCheckedDrawable)
        mThumbStrokeColor = typedArray.getColor(R.styleable.RadiusView_rv_thumbStrokeColor, mColorDefault)
        mThumbStrokePressedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_thumbStrokePressedColor, mThumbStrokeColor)
        mThumbStrokeDisabledColor =
            typedArray.getColor(R.styleable.RadiusView_rv_thumbStrokeDisabledColor, mThumbStrokeColor)
        mThumbStrokeSelectedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_thumbStrokeSelectedColor, mThumbStrokeColor)
        mThumbStrokeCheckedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_thumbStrokeCheckedColor, mColorAccent)
        mThumbStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_thumbStrokeWidth, dp2px(2f))
        mThumbRadius = typedArray.getDimension(R.styleable.RadiusView_rv_thumbRadius, 100f)

        //轨道属性
        mTrackDrawableWidth =
            typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_trackDrawableWidth, dp2px(48f))
        mTrackDrawableHeight =
            typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_trackDrawableHeight, dp2px(24f))
        mTrackDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_trackDrawable)
        mTrackPressedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_trackPressedDrawable)
        mTrackDisabledDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_trackDisabledDrawable)
        mTrackSelectedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_trackSelectedDrawable)
        mTrackCheckedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_trackCheckedDrawable)
        mTrackStrokeColor = typedArray.getColor(R.styleable.RadiusView_rv_trackStrokeColor, mColorDefault)
        mTrackStrokePressedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_trackStrokePressedColor, mTrackStrokeColor)
        mTrackStrokeDisabledColor =
            typedArray.getColor(R.styleable.RadiusView_rv_trackStrokeDisabledColor, mTrackStrokeColor)
        mTrackStrokeSelectedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_trackStrokeSelectedColor, mThumbStrokeColor)
        mTrackStrokeCheckedColor =
            typedArray.getColor(R.styleable.RadiusView_rv_trackStrokeCheckedColor, mColorAccent)
        mTrackStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_trackStrokeWidth, dp2px(2f))
        mTrackRadius = typedArray.getDimension(R.styleable.RadiusView_rv_trackRadius, 100f)

        mThumbDrawable = if (mThumbDrawable == null) ColorDrawable(Color.WHITE) else mThumbDrawable
        mThumbPressedDrawable = if (mTrackPressedDrawable == null) mThumbDrawable else mThumbPressedDrawable
        mThumbDisabledDrawable = if (mThumbDisabledDrawable == null) mThumbDrawable else mThumbDisabledDrawable
        mThumbSelectedDrawable = if (mThumbSelectedDrawable == null) mThumbDrawable else mThumbSelectedDrawable
        mThumbCheckedDrawable = if (mThumbCheckedDrawable == null) mThumbDrawable else mThumbCheckedDrawable

        mTrackDrawable = if (mTrackDrawable == null) ColorDrawable(mColorDefault) else mTrackDrawable
        mTrackPressedDrawable = if (mTrackPressedDrawable == null) mTrackDrawable else mTrackPressedDrawable
        mTrackDisabledDrawable = if (mTrackDisabledDrawable == null) mTrackDrawable else mTrackDisabledDrawable
        mTrackSelectedDrawable = if (mTrackSelectedDrawable == null) mTrackDrawable else mTrackSelectedDrawable
        mTrackCheckedDrawable =
            if (mTrackCheckedDrawable == null) ColorDrawable(mColorAccent) else mTrackCheckedDrawable
        super.initAttributes(typedArray)
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
        mThumbCheckedDrawable?.let {
            mStateThumbDrawable?.addState(
                intArrayOf(mStateChecked),
                getStateDrawable(
                    it,
                    mThumbRadius,
                    mThumbDrawableWidth,
                    mThumbDrawableHeight,
                    mThumbStrokeWidth,
                    mThumbStrokeCheckedColor
                )
            )
        }

        mThumbSelectedDrawable?.let {
            mStateThumbDrawable?.addState(
                intArrayOf(mStateSelected),
                getStateDrawable(
                    it,
                    mThumbRadius,
                    mThumbDrawableWidth,
                    mThumbDrawableHeight,
                    mThumbStrokeWidth,
                    mThumbStrokeSelectedColor
                )
            )
        }
        mThumbPressedDrawable?.let {
            mStateThumbDrawable?.addState(
                intArrayOf(mStatePressed),
                getStateDrawable(
                    it,
                    mThumbRadius,
                    mThumbDrawableWidth,
                    mThumbDrawableHeight,
                    mThumbStrokeWidth,
                    mThumbStrokePressedColor
                )
            )
        }
        mThumbDisabledDrawable?.let {
            mStateThumbDrawable?.addState(
                intArrayOf(mStateDisabled),
                getStateDrawable(
                    it,
                    mThumbRadius,
                    mThumbDrawableWidth,
                    mThumbDrawableHeight,
                    mThumbStrokeWidth,
                    mThumbStrokeDisabledColor
                )
            )
        }
        mThumbDrawable?.let {
            mStateThumbDrawable?.addState(
                intArrayOf(),
                getStateDrawable(
                    it,
                    mThumbRadius,
                    mThumbDrawableWidth,
                    mThumbDrawableHeight,
                    mThumbStrokeWidth,
                    mThumbStrokeColor
                )
            )
        }


        mTrackDrawableHeight = 0
        mTrackCheckedDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(mStateChecked),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokeCheckedColor
                )
            )
        }
        mTrackSelectedDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(mStateSelected),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokeSelectedColor
                )
            )
        }
        mTrackPressedDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(mStatePressed),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokePressedColor
                )
            )
        }

        mTrackDisabledDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(mStateDisabled),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokeDisabledColor
                )
            )
        }
        mTrackDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokeColor
                )
            )
        }
        mTrackDrawable?.let {
            mStateTrackDrawable?.addState(
                intArrayOf(),
                getStateDrawable(
                    it,
                    mTrackRadius,
                    mTrackDrawableWidth,
                    mTrackDrawableHeight,
                    mTrackStrokeWidth,
                    mTrackStrokeColor
                )
            )
        }

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
        var drawable: Drawable? = source
        drawable = getStateDrawable(drawable, radius, width, height)
        if (drawable is GradientDrawable) {
            val gradientDrawable = drawable
            gradientDrawable.setStroke(strokeWidth, strokeColor)
        }
        return drawable
    }
}

class RadiusSwitchDelegateImp(switch: Switch, context: Context, attrs: AttributeSet?) :
    RadiusSwitchDelegate<RadiusSwitchDelegateImp>(switch, context, attrs)