package com.vincent.dialoglibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * 创建日期：2019/3/20 0020on 下午 1:45
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class Test : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    internal object A {
        fun a() {
            B.b()
        }
    }

    internal object B {
        fun b() {

        }
    }
}