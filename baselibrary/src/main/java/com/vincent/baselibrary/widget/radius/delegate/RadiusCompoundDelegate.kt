@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded","NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
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
open class RadiusCompoundDelegate<T : RadiusTextDelegate<T>> constructor(
    open val compoundButton: CompoundButton,
    override val context: Context,
    attrs: AttributeSet?
) : RadiusTextDelegate<T>(compoundButton, context, attrs) {

    private lateinit var mButton: CompoundButton
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

    override fun initAttributes() {
        mButtonDrawableSystemEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_buttonDrawableSystemEnable, false)
        mButtonDrawableColorRadius =
            mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_buttonDrawableColorRadius, 0f)
        mButtonDrawableColorCircleEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_buttonDrawableColorCircleEnable, false)
        mButtonDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_buttonDrawableWidth, -1)
        mButtonDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_buttonDrawableHeight, -1)
        mButtonDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonDrawable)
        mButtonPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonPressedDrawable)
        mButtonDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonDisabledDrawable)
        mButtonSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonSelectedDrawable)
        mButtonCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonCheckedDrawable)
        super.initAttributes()
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
    fun setButtonDrawableColorCircleEnable(buttonDrawableColorCircleEnable: Boolean):T {
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
        mButton = compoundButton
        if (mButtonDrawableSystemEnable) {
            return
        }
        if (mButtonDrawable == null
            && mButtonPressedDrawable == null
            && mButtonDisabledDrawable == null
            && mButtonSelectedDrawable == null
            && mButtonCheckedDrawable == null
        ) {
            mButton.buttonDrawable = null
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
        mButton.buttonDrawable = mStateButtonDrawable
    }

}
class RadiusCompoundDelegateImp( compoundButton: CompoundButton,context: Context,attrs: AttributeSet?):
    RadiusCompoundDelegate<RadiusCompoundDelegateImp>(compoundButton, context, attrs)