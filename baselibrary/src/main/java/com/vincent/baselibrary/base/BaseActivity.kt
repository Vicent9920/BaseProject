package com.vincent.baselibrary.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * 创建日期：2019/3/6 0006on 上午 10:58
 * 描述： Activity 基类
 * @author：Vincent
 * QQ：3332168769
 * 备注：Activity的跳转推荐使用 EasyActivityResult 避免多次点击跳转
 * https://github.com/yjfnypeu/EasyAndroid/blob/master/app/src/main/java/com/haoge/sample/easyandroid/activities/EasyResultActivity.kt
 */
abstract class BaseActivity : AppCompatActivity() {
    val HANDLER = @SuppressLint("HandlerLeak")
    object : BaseActivityHandler<BaseActivity>(this) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        initData()
        initEvent()
    }

    // 引入布局
    abstract fun getLayoutId(): Int

    // 初始化控件
    protected abstract fun initView()

    // 初始化数据
    open fun initData() {}

    // 初始化响应事件
    open fun initEvent() {}

    override fun finish() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        val view = currentFocus
        if (view != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        super.finish()
    }
}