package com.vincent.baseproject.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.vincent.baseproject.R
import kotlinx.android.synthetic.main.view_setting_bar.view.*

/**
 * 创建日期：2019/3/18 0018on 下午 1:29
 * 描述：设置条自定义控件
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class SettingBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    init {
        LayoutInflater.from(context).inflate(R.layout.view_setting_bar, this)
        val attr = context.obtainStyledAttributes(attrs, R.styleable.SettingBar)
        if (attr.hasValue(R.styleable.SettingBar_bar_leftText)) {
            setLeftText(attr.getString(R.styleable.SettingBar_bar_leftText))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_rightText)) {
            setRightText(attr.getString(R.styleable.SettingBar_bar_rightText))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_leftHint)) {
            setLeftHint(attr.getString(R.styleable.SettingBar_bar_leftHint))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_rightHint)) {
            setRightHint(attr.getString(R.styleable.SettingBar_bar_rightHint))
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_leftIcon)) {
            setLeftIcon(ContextCompat.getDrawable(context, attr.getResourceId(R.styleable.SettingBar_bar_leftIcon, 0)))
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_rightIcon)) {
            setRightIcon(
                ContextCompat.getDrawable(
                    context,
                    attr.getResourceId(R.styleable.SettingBar_bar_rightIcon, 0)
                )
            )
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_leftColor)) {
            setLeftColor(attr.getColor(R.styleable.SettingBar_bar_leftColor, 0))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_rightColor)) {
            setRightColor(attr.getColor(R.styleable.SettingBar_bar_rightColor, 0))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_leftSize)) {
            setLeftSize(TypedValue.COMPLEX_UNIT_PX, attr.getDimensionPixelSize(R.styleable.SettingBar_bar_leftSize, 0))
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_rightSize)) {
            setRightSize(
                TypedValue.COMPLEX_UNIT_PX,
                attr.getDimensionPixelSize(R.styleable.SettingBar_bar_rightSize, 0)
            )
        }
        if (attr.hasValue(R.styleable.SettingBar_bar_lineColor)) {
            setLineDrawable(attr.getDrawable(R.styleable.SettingBar_bar_lineColor))
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_lineVisible)) {
            setLineVisible(attr.getBoolean(R.styleable.SettingBar_bar_lineVisible, true))
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_lineSize)) {
            setLineSize(attr.getDimensionPixelSize(R.styleable.SettingBar_bar_lineSize, 0))
        }

        if (attr.hasValue(R.styleable.SettingBar_bar_lineMargin)) {
            setLineMargin(attr.getDimensionPixelSize(R.styleable.SettingBar_bar_lineMargin, 0))
        }
        attr.recycle()

        if (background == null) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.selector_selectable_white)
            background = drawable
        }
    }

    fun setLeftText(text: String?) {
        tv_setting_bar_left.text = text
    }

    fun setLeftText(textRes: Int) {
        tv_setting_bar_left.setText(textRes)
    }

    fun setLeftHint(text: String?) {
        tv_setting_bar_left.hint = text
    }

    fun setLeftHint(textRes: Int) {
        tv_setting_bar_left.setHint(textRes)
    }

    fun setLeftIcon(drawable: Drawable?) {
        tv_setting_bar_left.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }

    fun setLeftIcon(drawableRes: Int) {
        tv_setting_bar_left.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)
    }

    fun setLeftColor(color: Int) {
        tv_setting_bar_left.setTextColor(color)
    }

    fun setLeftSize(unit: Int, size: Int) {
        tv_setting_bar_left.setTextSize(unit, size.toFloat())
    }

    fun setRightText(text: String?) {
        tv_setting_bar_right.text = text
    }

    fun setRightText(textRes: Int) {
        tv_setting_bar_right.setText(textRes)
    }

    fun setRightHint(text: String?) {
        tv_setting_bar_right.hint = text
    }

    fun setRightHint(textRes: Int) {
        tv_setting_bar_right.setHint(textRes)
    }

    fun setRightIcon(drawable: Drawable?) {
        tv_setting_bar_right.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    fun setRightIcon(res: Int) {
        tv_setting_bar_right.setCompoundDrawablesWithIntrinsicBounds(0, 0, res, 0)
    }

    fun setRightColor(color: Int) {
        tv_setting_bar_right.setTextColor(color)
    }

    fun setRightSize(unit: Int, size: Int) {
        tv_setting_bar_right.setTextSize(unit, size.toFloat())
    }

    fun setLineDrawable(drawable: Drawable?) {
        v_setting_bar_line.background = drawable
    }

    fun setLineColor(color: Int) {
        v_setting_bar_line.background = ColorDrawable(color)
    }

    fun setLineSize(size: Int) {
        v_setting_bar_line.layoutParams.height = size
    }

    fun setLineMargin(margin: Int) {
        val params = v_setting_bar_line.layoutParams as FrameLayout.LayoutParams
        params.marginStart = margin
        params.marginEnd = margin
        v_setting_bar_line.layoutParams = params
    }

    fun setLineVisible(visible: Boolean) {
        v_setting_bar_line.visibility = if (visible) View.VISIBLE else View.GONE
    }

}