package com.vincent.baseproject.common

import android.os.Bundle
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.vincent.baselibrary.base.BaseActivity
import com.vincent.baseproject.R

/**
 * 创建日期：2019/3/6 0006on 下午 3:03
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
abstract class UIActivity :BaseActivity(), BGASwipeBackHelper.Delegate {
    override fun onSwipeBackLayoutExecuted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSwipeBackLayoutCancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isSupportSwipeBack(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mSwipeBackHelper: BGASwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBack()
        super.onCreate(savedInstanceState)
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
            mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
            // 设置是否显示滑动返回的阴影效果。默认值为 true
            mSwipeBackHelper.setIsNeedShowShadow(true)
            // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
            mSwipeBackHelper.setIsShadowAlphaGradient(true)
            // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
            mSwipeBackHelper.setSwipeBackThreshold(0.3f)
            // 设置底部导航条是否悬浮在内容上，默认值为 false
            mSwipeBackHelper.setIsNavigationBarOverlap(false)

    }
}