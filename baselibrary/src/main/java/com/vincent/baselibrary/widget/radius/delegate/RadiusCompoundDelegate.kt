@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.widget.CompoundButton
import com.vincent.baselibrary.R
import com.vincent.baselibrary.util.DrawableUtil


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/7/10 0010 <p>
 * <p>@update 2019/7/10 0010<p>
 * <p>版本号：1<p>
 *
 */
open class RadiusCompoundDelegate<out T> constructor(
    private val compoundButton: CompoundButton,
    context: Context,
    attrs: AttributeSet?
) : RadiusTextDelegate<T>(compoundButton, context, attrs) where T : RadiusTextDelegate<T> {

    private var mStateButtonDrawable: StateListDrawable? = null

    private var mButtonDrawableSystemEnable: Boolean = false
    private var mButtonDrawableColorRadius: Float = 0f
    private var mButtonDrawableColorCircleEnable: Boolean = false
    private var mButtonDrawableWidth: Int = 0
    private var mButtonDrawableHeight: Int = 0
    private var mButtonDrawable: Drawable? = null
    private var mButtonPressedDrawable: Drawable? = null
    private var mButtonDisabledDrawable: Drawable? = null
    private var mButtonSelectedDrawable: Drawable? = null
    private var mButtonCheckedDrawable: Drawable? = null

    init {
        setButtonDrawable()
    }

    override fun initAttributes(typedArray: TypedArray) {

        mButtonDrawableSystemEnable =
            typedArray.getBoolean(R.styleable.RadiusView_rv_buttonDrawableSystemEnable, false)
        mButtonDrawableColorRadius =
            typedArray.getDimension(R.styleable.RadiusView_rv_buttonDrawableColorRadius, 0f)
        mButtonDrawableColorCircleEnable =
            typedArray.getBoolean(R.styleable.RadiusView_rv_buttonDrawableColorCircleEnable, false)
        mButtonDrawableWidth = typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_buttonDrawableWidth, -1)
        mButtonDrawableHeight = typedArray.getDimensionPixelSize(R.styleable.RadiusView_rv_buttonDrawableHeight, -1)
        mButtonDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_buttonDrawable)
        mButtonPressedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_buttonPressedDrawable)
        mButtonDisabledDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_buttonDisabledDrawable)
        mButtonSelectedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_buttonSelectedDrawable)
        mButtonCheckedDrawable = typedArray.getDrawable(R.styleable.RadiusView_rv_buttonCheckedDrawable)
        super.initAttributes(typedArray)
    }


    /**
     * 是否系统自带ButtonDrawable
     *
     * @param buttonDrawableSystemEnable
     * @return
     */
    fun setButtonDrawableSystemEnable(buttonDrawableSystemEnable: Boolean): T {
        mButtonDrawableSystemEnable = buttonDrawableSystemEnable
        return back()
    }

    /**
     * colorDrawable 圆角
     *
     * @param buttonDrawableColorRadius
     * @return
     */
    fun setButtonDrawableColorRadius(buttonDrawableColorRadius: Float): T {
        mButtonDrawableColorRadius = buttonDrawableColorRadius
        return back()
    }

    /**
     * 是否ColorDrawable 为圆形
     *
     * @param buttonDrawableColorCircleEnable
     * @return
     */
    fun setButtonDrawableColorCircleEnable(buttonDrawableColorCircleEnable: Boolean): T {
        mButtonDrawableColorCircleEnable = buttonDrawableColorCircleEnable
        return back()
    }

    /**
     * 设置drawable宽度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableWidth
     * @return
     */
    fun setButtonDrawableWidth(drawableWidth: Int): T {
        mButtonDrawableWidth = drawableWidth
        return back()
    }

    /**
     * 设置drawable高度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableHeight
     * @return
     */
    fun setButtonDrawableHeight(drawableHeight: Int): T {
        mButtonDrawableHeight = drawableHeight
        return back()
    }

    /**
     * 设置默认状态Drawable
     *
     * @param drawable
     */
    fun setButtonDrawable(drawable: Drawable?): T {
        drawable?.let {
            mButtonDrawable = it
        }

        return back()
    }

    fun setButtonDrawable(resId: Int): T {
        return setButtonDrawable(getDrawable(resId))
    }

    /**
     * 设置按下效果Drawable
     *
     * @param drawable
     */
    fun setButtonPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mButtonPressedDrawable = it
        }

        return back()
    }

    fun setButtonPressedDrawable(resId: Int): T {
        return setButtonPressedDrawable(getDrawable(resId))
    }

    /**
     * 设置不可操作效果Drawable
     *
     * @param drawable
     */
    fun setButtonDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mButtonDisabledDrawable = it
        }

        return back()
    }

    fun setButtonDisabledDrawable(resId: Int): T {
        return setButtonDisabledDrawable(getDrawable(resId))
    }

    /**
     * 设置选中效果Drawable
     *
     * @param drawable
     * @return
     */
    fun setButtonSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mButtonSelectedDrawable = it
        }

        return back()
    }

    fun setButtonSelectedDrawable(resId: Int): T {
        return setButtonSelectedDrawable(getDrawable(resId))
    }

    /**
     * 设置Checked状态Drawable
     *
     * @param drawable
     * @return
     */
    fun setButtonCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mButtonCheckedDrawable = it
        }

        return back()
    }

    fun setButtonCheckedDrawable(resId: Int): T {
        return setButtonCheckedDrawable(getDrawable(resId))
    }

    /**
     * 设置CompoundButton的setButtonDrawable属性
     */
    private fun setButtonDrawable() {

        if (mButtonDrawableSystemEnable) {
            return
        }
        if (mButtonDrawable == null
            && mButtonPressedDrawable == null
            && mButtonDisabledDrawable == null
            && mButtonSelectedDrawable == null
            && mButtonCheckedDrawable == null
        ) {
            compoundButton.buttonDrawable = null
            return
        }
        val radius = if (mButtonDrawableColorCircleEnable)
            (mButtonDrawableWidth + mButtonDrawableHeight / 2).toFloat()
        else
            mButtonDrawableColorRadius
        mStateButtonDrawable = StateListDrawable()
        mStateButtonDrawable?.addState(
            intArrayOf(mStateChecked),
            getStateDrawable(mButtonCheckedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight)
        )
        mStateButtonDrawable?.addState(
            intArrayOf(mStateSelected),
            getStateDrawable(mButtonSelectedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight)
        )
        mStateButtonDrawable?.addState(
            intArrayOf(mStatePressed),
            getStateDrawable(mButtonPressedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight)
        )
        mStateButtonDrawable?.addState(
            intArrayOf(mStateDisabled),
            getStateDrawable(mButtonDisabledDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight)
        )
        mStateButtonDrawable?.addState(
            intArrayOf(),
            getStateDrawable(mButtonDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight)
        )
        DrawableUtil.setDrawableWidthHeight(mStateButtonDrawable, mButtonDrawableWidth, mButtonDrawableHeight)
        compoundButton.buttonDrawable = mStateButtonDrawable
    }

}

class RadiusCompoundDelegateImp(
    compoundButton: CompoundButton,
    context: Context,
    attrs: AttributeSet?
) :
    RadiusCompoundDelegate<RadiusCompoundDelegateImp>(compoundButton, context, attrs)