package com.vincent.baseproject.common

import android.view.Gravity
import android.view.LayoutInflater
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.base.BaseLazyFragment

/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/7/21 0021 <p>
 * <p>@update 2019/7/21 0021<p>
 * <p>版本号：1<p>
 *
 */
abstract class BaseUiFragment :BaseLazyFragment(){
    open val toastCustom by lazy {
        // 创建自定义的Toast.
        val layout = LayoutInflater.from(context).inflate(com.vincent.baselibrary.R.layout.toast_custom_layout, null)
        EasyToast.newBuilder(layout, com.vincent.baselibrary.R.id.toast_tv)
            .setGravity(Gravity.CENTER, 0, 0)
            .build()
    }
    fun toast(resString: Int) {
        toastCustom.show(resString)
    }

    fun toast(s: String) {
        toastCustom.show(s)
    }
}