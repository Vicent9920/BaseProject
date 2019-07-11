@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded","NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.vincent.baselibrary.R
import com.vincent.baselibrary.util.DrawableUtil
import com.vincent.baselibrary.util.ResourceUtil


/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/10 0010 <p>
 * <p>update 2019/7/10 0010<p>
 * <p>版本号：1<p>
 *
 */
open class RadiusTextDelegate<T : RadiusViewDelegate<T>> constructor(
     open val textView: TextView,
    override val context: Context,
    attrs: AttributeSet?
) :
    RadiusViewDelegate<T>(textView, context, attrs) {


    private var mTextColor: Int = 0
    private var mTextPressedColor: Int = 0
    private var mTextDisabledColor: Int = 0
    private var mTextSelectedColor: Int = 0
    private var mTextCheckedColor: Int = 0

    private var mLeftDrawableSystemEnable: Boolean = false
    private var mLeftDrawableColorRadius: Float = 0f
    private var mLeftDrawableColorCircleEnable: Boolean = false
    private var mLeftDrawableWidth: Int = 0
    private var mLeftDrawableHeight: Int = 0
    private lateinit var mLeftDrawable: Drawable
    private lateinit var mLeftPressedDrawable: Drawable
    private lateinit var mLeftDisabledDrawable: Drawable
    private lateinit var mLeftSelectedDrawable: Drawable
    private lateinit var mLeftCheckedDrawable: Drawable

    private var mTopDrawableSystemEnable: Boolean = false
    private var mTopDrawableColorRadius: Float = 0f
    private var mTopDrawableColorCircleEnable: Boolean = false
    private var mTopDrawableWidth: Int = 0
    private var mTopDrawableHeight: Int = 0
    private lateinit var mTopDrawable: Drawable
    private lateinit var mTopPressedDrawable: Drawable
    private lateinit var mTopDisabledDrawable: Drawable
    private lateinit var mTopSelectedDrawable: Drawable
    private lateinit var mTopCheckedDrawable: Drawable


    private var mRightDrawableSystemEnable: Boolean = false
    private var mRightDrawableColorRadius: Float = 0f
    private var mRightDrawableColorCircleEnable: Boolean = false
    private var mRightDrawableWidth: Int = 0
    private var mRightDrawableHeight: Int = 0
    private lateinit var mRightDrawable: Drawable
    private lateinit var mRightPressedDrawable: Drawable
    private lateinit var mRightDisabledDrawable: Drawable
    private lateinit var mRightSelectedDrawable: Drawable
    private lateinit var mRightCheckedDrawable: Drawable

    private var mBottomDrawableSystemEnable: Boolean = false
    private var mBottomDrawableColorRadius: Float = 0f
    private var mBottomDrawableColorCircleEnable: Boolean = false
    private var mBottomDrawableWidth: Int = 0
    private var mBottomDrawableHeight: Int = 0
    private lateinit var mBottomDrawable: Drawable
    private lateinit var mBottomPressedDrawable: Drawable
    private lateinit var mBottomDisabledDrawable: Drawable
    private lateinit var mBottomSelectedDrawable: Drawable
    private lateinit var mBottomCheckedDrawable: Drawable

    init {
        setTextSelector()
        if (!mLeftDrawableSystemEnable) {
            setTextDrawable(
                mLeftDrawable, mLeftCheckedDrawable,
                mLeftSelectedDrawable, mLeftPressedDrawable, mLeftDisabledDrawable, Gravity.LEFT
            )
        }
        if (!mTopDrawableSystemEnable) {
            setTextDrawable(
                mTopDrawable, mTopCheckedDrawable,
                mTopSelectedDrawable, mTopPressedDrawable, mTopDisabledDrawable, Gravity.TOP
            )
        }
        if (!mRightDrawableSystemEnable) {
            setTextDrawable(
                mRightDrawable, mRightCheckedDrawable,
                mRightSelectedDrawable, mRightPressedDrawable, mRightDisabledDrawable, Gravity.RIGHT
            )
        }
        if (!mBottomDrawableSystemEnable) {
            setTextDrawable(
                mBottomDrawable, mBottomCheckedDrawable,
                mBottomSelectedDrawable, mBottomPressedDrawable, mBottomDisabledDrawable, Gravity.BOTTOM
            )
        }

    }

    override fun initAttributes() {
        mTextColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_textColor, Integer.MAX_VALUE)
        mTextColor = if (mTextColor == Integer.MAX_VALUE) textView.textColors.defaultColor else mTextColor
        mTextPressedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_textPressedColor, Integer.MAX_VALUE)
        mTextDisabledColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_textDisabledColor, Integer.MAX_VALUE)
        mTextSelectedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_textSelectedColor, Integer.MAX_VALUE)
        mTextCheckedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_textCheckedColor, Integer.MAX_VALUE)

        mLeftDrawableSystemEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_leftDrawableSystemEnable, false)
        mLeftDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_leftDrawableColorRadius, 0f)
        mLeftDrawableColorCircleEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_leftDrawableColorCircleEnable, false)
        mLeftDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_leftDrawableWidth, -1)
        mLeftDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_leftDrawableHeight, -1)
        mLeftDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_leftDrawable)
        mLeftPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_leftPressedDrawable)
        mLeftDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_leftDisabledDrawable)
        mLeftSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_leftSelectedDrawable)
        mLeftCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_leftCheckedDrawable)

        mTopDrawableSystemEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_topDrawableSystemEnable, false)
        mTopDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_topDrawableColorRadius, 0f)
        mTopDrawableColorCircleEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_topDrawableColorCircleEnable, false)
        mTopDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_topDrawableWidth, -1)
        mTopDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_topDrawableHeight, -1)
        mTopDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_topDrawable)
        mTopPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_topPressedDrawable)
        mTopDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_topDisabledDrawable)
        mTopSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_topSelectedDrawable)
        mTopCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_topCheckedDrawable)

        mRightDrawableSystemEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_rightDrawableSystemEnable, false)
        mRightDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_rightDrawableColorRadius, 0f)
        mRightDrawableColorCircleEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_rightDrawableColorCircleEnable, false)
        mRightDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_rightDrawableWidth, -1)
        mRightDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_rightDrawableHeight, -1)
        mRightDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_rightDrawable)
        mRightPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_rightPressedDrawable)
        mRightDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_rightDisabledDrawable)
        mRightSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_rightSelectedDrawable)
        mRightCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_rightCheckedDrawable)

        mBottomDrawableSystemEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_bottomDrawableSystemEnable, false)
        mBottomDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_bottomDrawableColorRadius, 0f)
        mBottomDrawableColorCircleEnable =
            mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_bottomDrawableColorCircleEnable, false)
        mBottomDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_bottomDrawableWidth, -1)
        mBottomDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_bottomDrawableHeight, -1)
        mBottomDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_bottomDrawable)
        mBottomPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_bottomPressedDrawable)
        mBottomDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_bottomDisabledDrawable)
        mBottomSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_bottomSelectedDrawable)
        mBottomCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_bottomCheckedDrawable)
        super.initAttributes()
    }



    /**
     * 设置文本常态颜色
     *
     * @param color
     * @return
     */
    fun setTextColor(color: Int): T {
        this.mTextColor = color
        return back()
    }

    /**
     * 设置文本按下颜色
     *
     * @param color
     * @return
     */
    fun setTextPressedColor(color: Int): T {
        this.mTextPressedColor = color
        return back()
    }

    /**
     * 设置文本不可点击状态颜色--setEnable(false)时的颜色[View.setEnabled]
     *
     * @param color
     * @return
     */
    fun setTextDisabledColor(color: Int): T {
        this.mTextDisabledColor = color
        return back()
    }

    /**
     * 设置文本selected颜色
     *
     * @param color
     * @return
     */
    fun setTextSelectedColor(color: Int): T {
        this.mTextSelectedColor = color
        return back()
    }

    /**
     * 设置文本checked颜色
     *
     * @param color
     * @return
     */
    fun setTextCheckedColor(color: Int): T {
        this.mTextCheckedColor = color
        return back()
    }

    /**
     * 是否使用系统自带属性
     *
     * @param leftDrawableSystemEnable
     * @return
     */
    fun setLeftDrawableSystemEnable(leftDrawableSystemEnable: Boolean): T {
        mLeftDrawableSystemEnable = leftDrawableSystemEnable
        return back()
    }

    fun setLeftDrawableColorRadius(leftDrawableColorRadius: Float): T {
        mLeftDrawableColorRadius = leftDrawableColorRadius
        return back()
    }

    fun setLeftDrawableColorCircleEnable(leftDrawableColorCircleEnable: Boolean): T {
        mLeftDrawableColorCircleEnable = leftDrawableColorCircleEnable
        return back()
    }

    fun setLeftDrawableWidth(leftDrawableWidth: Int): T {
        mLeftDrawableWidth = leftDrawableWidth
        return back()
    }

    fun setLeftDrawableHeight(leftDrawableHeight: Int): T {
        mLeftDrawableHeight = leftDrawableHeight
        return back()
    }

    /**
     * 设置左边drawable
     *
     * @param drawable
     * @return
     */
    fun setLeftDrawable(drawable: Drawable?): T{
        drawable?.let {
            mLeftDrawable = it
        }

        return back()
    }

    fun setLeftDrawable(resId: Int): T {
        return setLeftDrawable(getDrawable(resId))
    }

    fun setLeftPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mLeftPressedDrawable = it
        }

        return back()
    }

    fun setLeftPressedDrawable(resId: Int): T {
        return setLeftPressedDrawable(getDrawable(resId))
    }

    fun setLeftDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mLeftDisabledDrawable = it
        }

        return back()
    }

    fun setLeftDisabledDrawable(resId: Int): T {
        return setLeftDisabledDrawable(getDrawable(resId))
    }

    fun setLeftSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mLeftSelectedDrawable = it
        }

        return back()
    }

    fun setLeftSelectedDrawable(resId: Int): T {
        return setLeftSelectedDrawable(getDrawable(resId))
    }

    fun setLeftCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mLeftCheckedDrawable = it
        }

        return back()
    }

    fun setLeftCheckedDrawable(resId: Int): T {
        return setLeftCheckedDrawable(getDrawable(resId))
    }

    fun setTopDrawableSystemEnable(topDrawableSystemEnable: Boolean): T {
        mTopDrawableSystemEnable = topDrawableSystemEnable
        return back()
    }

    fun setTopDrawableColorRadius(topDrawableColorRadius: Float): T {
        mTopDrawableColorRadius = topDrawableColorRadius
        return back()
    }

    fun setTopDrawableColorCircleEnable(topDrawableColorCircleEnable: Boolean): T {
        mTopDrawableColorCircleEnable = topDrawableColorCircleEnable
        return back()
    }

    fun setTopDrawableWidth(topDrawableWidth: Int): T {
        mTopDrawableWidth = topDrawableWidth
        return back()
    }

    fun setTopDrawableHeight(topDrawableHeight: Int): T {
        mTopDrawableHeight = topDrawableHeight
        return back()
    }

    /**
     * 设置top drawable
     *
     * @param drawable
     * @return
     */
    fun setTopDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTopDrawable = it
        }

        return back()
    }

    fun setTopDrawable(resId: Int):T {
        return setTopDrawable(getDrawable(resId))
    }

    fun setTopPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTopPressedDrawable = it
        }

        return back()
    }

    fun setTopPressedDrawable(resId: Int): T {
        return setTopPressedDrawable(getDrawable(resId))
    }

    fun setTopDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTopDisabledDrawable = it
        }

        return back()
    }

    fun setTopDisabledDrawable(resId: Int): T {
        return setTopDisabledDrawable(getDrawable(resId))
    }

    fun setTopSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTopSelectedDrawable = it
        }

        return back()
    }

    fun setTopSelectedDrawable(resId: Int): T {
        return setTopSelectedDrawable(getDrawable(resId))
    }

    fun setTopCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mTopCheckedDrawable = it
        }

        return back()
    }

    fun setTopCheckedDrawable(resId: Int): T {
        return setTopCheckedDrawable(getDrawable(resId))
    }

    fun setRightDrawableSystemEnable(rightDrawableSystemEnable: Boolean): T {
        mRightDrawableSystemEnable = rightDrawableSystemEnable
        return back()
    }

    fun setRightDrawableColorRadius(rightDrawableColorRadius: Float): T {
        mRightDrawableColorRadius = rightDrawableColorRadius
        return back()
    }

    fun setRightDrawableColorCircleEnable(rightDrawableColorCircleEnable: Boolean): T {
        mRightDrawableColorCircleEnable = rightDrawableColorCircleEnable
        return back()
    }

    fun setRightDrawableWidth(rightDrawableWidth: Int): T {
        mRightDrawableWidth = rightDrawableWidth
        return back()
    }

    fun setRightDrawableHeight(rightDrawableHeight: Int): T {
        mRightDrawableHeight = rightDrawableHeight
        return back()
    }

    /**
     * 设置right drawable
     *
     * @param drawable
     * @return
     */
    fun setRightDrawable(drawable: Drawable?): T {
        drawable?.let {
            mRightDrawable = it
        }

        return back()
    }

    fun setRightDrawable(resId: Int): T {
        return setRightDrawable(getDrawable(resId))
    }

    fun setRightPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mRightPressedDrawable = it
        }
        return back()
    }

    fun setRightPressedDrawable(resId: Int): T {
        return setRightPressedDrawable(getDrawable(resId))
    }

    fun setRightDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mRightDisabledDrawable = it
        }

        return back()
    }

    fun setRightDisabledDrawable(resId: Int): T {
        return setRightDisabledDrawable(getDrawable(resId))
    }

    fun setRightSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mRightSelectedDrawable = it
        }

        return back()
    }

    fun setRightSelectedDrawable(resId: Int): T {
        return setRightSelectedDrawable(getDrawable(resId))
    }

    fun setRightCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mRightCheckedDrawable = it
        }
        return back()
    }

    fun setRightCheckedDrawable(resId: Int): T {
        return setRightCheckedDrawable(getDrawable(resId))
    }

    fun setBottomDrawableSystemEnable(bottomDrawableSystemEnable: Boolean): T {
        mBottomDrawableSystemEnable = bottomDrawableSystemEnable
        return back()
    }

    fun setBottomDrawableColorRadius(bottomDrawableColorRadius: Float): T {
        mBottomDrawableColorRadius = bottomDrawableColorRadius
        return back()
    }

    fun setBottomDrawableColorCircleEnable(bottomDrawableColorCircleEnable: Boolean): T {
        mBottomDrawableColorCircleEnable = bottomDrawableColorCircleEnable
        return back()
    }

    fun setBottomDrawableWidth(bottomDrawableWidth: Int): T {
        mBottomDrawableWidth = bottomDrawableWidth
        return back()
    }

    fun setBottomDrawableHeight(bottomDrawableHeight: Int): T {
        mBottomDrawableHeight = bottomDrawableHeight
        return back()
    }

    /**
     * 设置bottom drawable
     *
     * @param drawable
     * @return
     */
    fun setBottomDrawable(drawable: Drawable?): T {
        drawable?.let {
            mBottomDrawable = it
        }

        return back()
    }

    fun setBottomDrawable(resId: Int): T {
        return setBottomDrawable(getDrawable(resId))
    }

    fun setBottomPressedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mBottomPressedDrawable = it
        }
        return back()
    }

    fun setBottomPressedDrawable(resId: Int): T {
        return setBottomPressedDrawable(getDrawable(resId))
    }

    fun setBottomDisabledDrawable(drawable: Drawable?): T {
        drawable?.let {
            mBottomDisabledDrawable = it
        }
        return back()
    }

    fun setBottomDisabledDrawable(resId: Int): T {
        return setBottomDisabledDrawable(getDrawable(resId))
    }

    fun setBottomSelectedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mBottomSelectedDrawable = it
        }

        return back()
    }

    fun setBottomSelectedDrawable(resId: Int): T {
        return setBottomSelectedDrawable(getDrawable(resId))
    }

    fun setBottomCheckedDrawable(drawable: Drawable?): T {
        drawable?.let {
            mBottomCheckedDrawable = it
        }
        return back()
    }

    fun setBottomCheckedDrawable(resId: Int): T {
        return setBottomCheckedDrawable(getDrawable(resId))
    }

    /**
     * 设置TextView的Left、Top、Right、Bottom Drawable属性
     *
     * @param normal
     * @param checked
     * @param selected
     * @param pressed
     * @param disabled
     * @param gravity
     */
    private fun setTextDrawable(
        normal: Drawable?, checked: Drawable?, selected: Drawable?, pressed: Drawable?,
        disabled: Drawable?, gravity: Int
    ) {
        var index = 0
        var width = mLeftDrawableWidth
        var height = mLeftDrawableHeight
        var radius = (if (mLeftDrawableColorCircleEnable) width + height / 2 else mLeftDrawableColorRadius).toFloat()
        when (gravity) {
            Gravity.TOP -> {
                index = 1
                width = mTopDrawableWidth
                height = mTopDrawableHeight
                radius = (if (mTopDrawableColorCircleEnable) width + height / 2 else mTopDrawableColorRadius).toFloat()
            }
            Gravity.RIGHT -> {
                index = 2
                width = mRightDrawableWidth
                height = mRightDrawableHeight
                radius =
                    (if (mRightDrawableColorCircleEnable) width + height / 2 else mRightDrawableColorRadius).toFloat()
            }
            Gravity.BOTTOM -> {
                index = 3
                width = mBottomDrawableWidth
                height = mBottomDrawableHeight
                radius =
                    (if (mBottomDrawableColorCircleEnable) width + height / 2 else mBottomDrawableColorRadius).toFloat()
            }
        }
        val drawables = textView.compoundDrawables
        if (normal == null && pressed == null && disabled == null
            && selected == null && checked == null
        ) {
            drawables[index] = null
        } else {
            val stateDrawable = StateListDrawable()
            if (checked != null) {
                stateDrawable.addState(intArrayOf(mStateChecked), getStateDrawable(checked, radius, width, height))
            }
            if (selected != null) {
                stateDrawable.addState(intArrayOf(mStateSelected), getStateDrawable(selected, radius, width, height))
            }
            if (pressed != null) {
                stateDrawable.addState(intArrayOf(mStatePressed), getStateDrawable(pressed, radius, width, height))
            }
            if (disabled != null) {
                stateDrawable.addState(intArrayOf(mStateDisabled), getStateDrawable(disabled, radius, width, height))
            }
            if (normal != null) {
                stateDrawable.addState(intArrayOf(), getStateDrawable(normal, radius, width, height))
            }
            DrawableUtil.setDrawableWidthHeight(stateDrawable, width, height)
            drawables[index] = stateDrawable
        }
        textView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3])
    }

    /**
     * 获取不同状态下的Drawable信息
     *
     * @param source
     * @param radius
     * @param width
     * @param height
     * @return
     */
    internal fun getStateDrawable(source: Drawable?, radius: Float, width: Int, height: Int): Drawable? {
        var drawable = source
        if (drawable == null) return drawable
        if (drawable is ColorDrawable) {
            val colorDrawable = drawable as ColorDrawable?
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(colorDrawable?.color ?:0)
            gradientDrawable.cornerRadius = radius
            gradientDrawable.setSize(width, height)
            drawable = gradientDrawable
        }
        DrawableUtil.setDrawableWidthHeight(drawable, width, height)
        return drawable
    }

    /**
     * 设置文本颜色
     */
    private fun setTextSelector() {

        val colorStateList = getColorSelector(
            mTextColor,
            getTextColor(mTextPressedColor),
            getTextColor(mTextDisabledColor),
            getTextColor(mTextSelectedColor),
            getTextColor(mTextSelectedColor)
        )
        textView.setTextColor(colorStateList)
    }

    private fun getTextColor(source: Int): Int {
        var color = source
        if (color != Integer.MAX_VALUE) {
            return color
        }
        if (textView.isSelected) {
            color = mTextSelectedColor
        } else if (textView is CompoundButton) {
            if ((textView as CompoundButton).isChecked) {
                color = mTextCheckedColor
            }
        }
        color =
            if (color != Integer.MAX_VALUE) color else if (mTextColor == Integer.MAX_VALUE) Color.WHITE else mTextColor
        return if (textView.isPressed && !mRippleEnable) calculateColor(color, mBackgroundPressedAlpha) else color
    }


    private fun getColorSelector(
        normalColor: Int,
        pressedColor: Int,
        disabledColor: Int,
        selectedColor: Int,
        checkedColor: Int
    ): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(mStatePressed),
                intArrayOf(mStateSelected),
                intArrayOf(mStateChecked),
                intArrayOf(mStateDisabled),
                intArrayOf()
            ),
            intArrayOf(pressedColor, selectedColor, checkedColor, disabledColor, normalColor)
        )
    }

    /**
     * 根据drawable资源id返回drawable对象
     *
     * @param resId
     * @return
     */
    internal fun getDrawable(resId: Int): Drawable? {
        return ResourceUtil.getDrawable(context, resId)
    }

}

class RadiusTextDelegateImp(textView: TextView,context: Context,attrs: AttributeSet?):
    RadiusTextDelegate<RadiusTextDelegateImp>(textView, context, attrs)