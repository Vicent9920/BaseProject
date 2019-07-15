package com.vincent.baselibrary.widget.radius

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.haoge.easyandroid.easy.EasyFormatter
import com.haoge.easyandroid.easy.EasyLog
import com.vincent.baselibrary.widget.radius.delegate.RadiusTextDelegateImp
import kotlin.math.max

/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/12 0012 <p>
 * <p>update 2019/7/12 0012<p>
 * <p>版本号：1<p>
 *
 */
class RadiusTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs) {

    init {
        EasyLog.DEFAULT.e(EasyFormatter.DEFAULT.format(attrs))
    }
    val delegate: RadiusTextDelegateImp? = RadiusTextDelegateImp(this, context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (delegate?.getWidthHeightEqualEnable() == true && width > 0 && height > 0) {
            val max = max(width, height)
            val measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY)
            super.onMeasure(measureSpec, measureSpec)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
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

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        delegate?.initShape()
    }

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        delegate?.initShape()
    }
}