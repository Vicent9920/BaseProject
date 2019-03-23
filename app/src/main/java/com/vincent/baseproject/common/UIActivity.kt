package com.vincent.baseproject.common

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.haoge.easyandroid.easy.EasyLog
import com.orhanobut.hawk.Hawk
import com.vincent.baselibrary.base.BaseActivity
import com.vincent.baselibrary.dao.NetworkChangeEvent
import com.vincent.baselibrary.util.NetUtils
import com.vincent.baseproject.ui.WebActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 创建日期：2019/3/6 0006on 下午 3:03
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
abstract class UIActivity : BaseActivity(), BGASwipeBackHelper.Delegate {


    private lateinit var mSwipeBackHelper: BGASwipeBackHelper
    private val netErrorView: View by lazy {
        val inflater = layoutInflater
        inflater.inflate(com.vincent.baseproject.R.layout.network_error_layout, null) //提示View布局

    }
    private val mLayoutParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
        PixelFormat.TRANSLUCENT
        ).apply {
            this.x = 0
            this.y = 0
            this.gravity = Gravity.TOP
        }
    }
    private val mWindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBack()
        EventBus.getDefault().register(this)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        checkNetWork(NetUtils.isConnected)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        checkNetWork(event.isConnected);
    }

    private fun checkNetWork(isConnected:Boolean) {
        if (isConnected) {
            if (netErrorView.parent != null) {
                mWindowManager.removeViewImmediate(netErrorView)
                netErrorView.setOnClickListener (null)
            }
        } else {
            if (netErrorView.parent == null) {
                mWindowManager.addView(netErrorView, mLayoutParams)

            }
        }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBack() {

        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(com.vincent.baseproject.R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)

    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    override fun isSupportSwipeBack(): Boolean {
        return Hawk.get(Contsant.SWIPE_ENABLED, true)
    }


    override fun initEvent() {
        super.initEvent()
        netErrorView.setOnTouchListener { v, event ->
            false }
        netErrorView.setOnClickListener {
            EasyLog.DEFAULT.e("setOnClickListener")
            startActivity(Intent(Settings.ACTION_SETTINGS)) }
    }

}