package com.vincent.baselibrary.widget.radius

import android.content.Context
import android.util.AttributeSet
import android.widget.Switch
import com.vincent.baselibrary.widget.radius.delegate.RadiusSwitchDelegateImp
import kotlin.math.max


/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/10 0010 <p>
 * <p>update 2019/7/10 0010<p>
 * <p>版本号：1<p>
 *
 */
class RadiusSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Switch(context, attrs, defStyleAttr) {

    val delegate: RadiusSwitchDelegateImp? = RadiusSwitchDelegateImp(this, context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate?.getWidthHeightEqualEnable() == true && width > 0 && height > 0) {
            val max = max(width, height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (delegate?.getRadiusHalfHeightEnable() == true) {
            delegate.setRadius(height / 2f)
        }
        delegate?.initShape()
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        delegate?.setSelected(selected)
    }

}