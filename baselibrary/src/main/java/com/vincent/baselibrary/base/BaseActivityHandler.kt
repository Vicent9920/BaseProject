package com.vincent.baselibrary.base

import android.app.Activity
import android.os.Handler
import android.os.Looper
import java.lang.ref.WeakReference

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 在 Activity 中优化 Handler 基类
 */
abstract class BaseActivityHandler<T : Activity>(activity: T) : Handler(Looper.getMainLooper()) {
    private val mActivity = WeakReference<T>(activity)
    /**
     * 判断当前Handler是否可用
     */
    fun isEnabled(): Boolean {
        return mActivity.get() != null && mActivity.get()?.isFinishing ?: true
    }

    /**
     * 在Activity销毁前移除所有的任务
     */
    fun onDestroy() {
        //删除所有的回调函数和消息
        removeCallbacksAndMessages(null)
    }
}