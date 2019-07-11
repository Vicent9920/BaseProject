package com.vincent.baselibrary.widget.radius

import android.content.Context
import android.util.AttributeSet
import android.widget.Switch
import com.vincent.baselibrary.widget.radius.delegate.RadiusTextDelegate

/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/7/10 0010 <p>
 * <p>@update 2019/7/10 0010<p>
 * <p>版本号：1<p>
 *
 */
class RadiusSwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Switch(context, attrs, defStyleAttr) {

    init {

        val radiusTextDelegate = RadiusTextDelegate(textView = this, context = context, attrs = attrs)
    }



}