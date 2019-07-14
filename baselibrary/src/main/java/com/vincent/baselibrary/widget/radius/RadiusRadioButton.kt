package com.vincent.baselibrary.widget.radius

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioButton
import com.vincent.baselibrary.widget.radius.delegate.RadiusCompoundDelegateImp
import kotlin.math.max

/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/12 0012 <p>
 * <p>update 2019/7/12 0012<p>
 * <p>版本号：1<p>
 *
 */
class RadiusRadioButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RadioButton(context, attrs, defStyleAttr) {

    val delegate = RadiusCompoundDelegateImp(this, context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate.getWidthHeightEqualEnable() && width > 0 && height > 0) {
            val max = max(width, height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (delegate.getRadiusHalfHeightEnable()) {
            delegate.setRadius(height / 2f)
        }
        delegate.initShape()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        delegate.setSelected(selected)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        delegate.initShape()
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        delegate.initShape()
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        delegate.initShape()
    }
}