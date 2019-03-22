package com.vincent.baselibrary.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import com.haoge.easyandroid.easy.EasyLog
import me.jessyan.autosize.AutoSizeCompat


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
    private var mToolbar:Toolbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(isStatusBarEnabled())initStatusBar()
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
    open fun initEvent() {
        // 设置 app:navigationIcon="@mipmap/ic_back" 才能使用下面的方法，未设置的话需要使用 onOptionsItemSelected
//        mToolbar?.setNavigationOnClickListener {
//            finish()
//        }
    }

    /**
     * 默认开启沉浸式状态栏
     */
    open fun isStatusBarEnabled():Boolean{
        return true
    }

    override fun finish() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        val view = currentFocus
        if (view != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        super.finish()
    }


    private fun initStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 初始化Toolbar
     * @param toolbar 需要改变的toolbar
     * @param isShowBack 是否需要显示返回键
     * @param isCloseTitle 是否需要关闭默认title
     */

    fun initToolBar(toolbar: Toolbar?, isShowBack:Boolean = true, isCloseTitle:Boolean = false){

//        toolbar?:return
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled( isShowBack)
        supportActionBar?.setDisplayShowTitleEnabled(isCloseTitle)
        if(isStatusBarEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            toolbar?.setPadding(toolbar.paddingStart,toolbar.paddingTop+getSystemBarHeight(),toolbar.paddingEnd,toolbar.paddingBottom)
            toolbar?.layoutParams?.height = toolbar?.layoutParams?.height?.plus(getSystemBarHeight())
        }
        mToolbar = toolbar
    }



    private fun getSystemBarHeight(): Int {
        val resources = resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }


}