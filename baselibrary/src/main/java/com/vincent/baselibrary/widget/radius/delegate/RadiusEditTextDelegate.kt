@file:Suppress("unused", "UNCHECKED_CAST", "RtlHardcoded","NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
package com.vincent.baselibrary.widget.radius.delegate

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.EditText
import com.vincent.baselibrary.R

/**
 * <p>文件描述：<p>
 * <p>author 烤鱼<p>
 * <p>date 2019/7/11 0011 <p>
 * <p>update 2019/7/11 0011<p>
 * <p>版本号：1<p>
 *
 */
open class RadiusEditTextDelegate<T>  constructor(editText: EditText, context: Context, attrs: AttributeSet?) :
    RadiusTextDelegate<T>(editText, context, attrs) where T:RadiusTextDelegate<T>{

    private var mSelectionEndEnable: Boolean = false
    private var mSelectionEndOnceEnable: Boolean = false

    override fun initAttributes(typedArray: TypedArray) {
        mSelectionEndEnable = typedArray.getBoolean(R.styleable.RadiusView_rv_selectionEndEnable, true)
        mSelectionEndOnceEnable = typedArray.getBoolean(R.styleable.RadiusView_rv_selectionEndOnceEnable, false)
        super.initAttributes(typedArray)
    }



    fun isSelectionEndEnable(): Boolean {
        return mSelectionEndEnable
    }

    /**
     * 是否调用setText后光标默认移动至结尾处
     *
     * @param selectionEndEnable
     * @return
     */
    fun setSelectionEndEnable(selectionEndEnable: Boolean): T {
        mSelectionEndEnable = selectionEndEnable
        return back()
    }

    fun isSelectionEndOnceEnable(): Boolean {
        return mSelectionEndOnceEnable
    }

    /**
     * 是否调用setText后光标位置移动结尾效果只执行一次setSelectionEndEnable(true)后该方法方有意义
     *
     * @param selectionEndOnceEnable
     * @return
     */
    fun setSelectionEndOnceEnable(selectionEndOnceEnable: Boolean): T {
        mSelectionEndOnceEnable = selectionEndOnceEnable
        return back()
    }
}
class RadiusEditTextDelegateImp(editText: EditText, context: Context, attrs: AttributeSet?) :
    RadiusEditTextDelegate<RadiusEditTextDelegateImp>(editText, context, attrs)