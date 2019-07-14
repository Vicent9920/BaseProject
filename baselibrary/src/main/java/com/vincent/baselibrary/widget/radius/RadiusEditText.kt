package com.vincent.baselibrary.widget.radius

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import com.vincent.baselibrary.widget.radius.delegate.RadiusEditTextDelegateImp
import kotlin.math.max

/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/12 0012 <p>
 * <p>update 2019/7/12 0012<p>
 * <p>版本号：1<p>
 *
 */
class RadiusEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : EditText(context, attrs, defStyleAttr) {

    /**
     * 是否设置完成光标标识
     */
    private var mSelectionEndDone: Boolean = false
    val delegate = RadiusEditTextDelegateImp(this, context, attrs)

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        text ?: return
        if (!delegate.isSelectionEndEnable()) {
            return
        }
        if (mSelectionEndDone) return

        setSelection(text.length)
        mSelectionEndDone = delegate.isSelectionEndOnceEnable()
    }

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
}