package com.vincent.baseproject.ui.space

import android.view.View
import com.vincent.baselibrary.helper.SpaceLayout
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.activity_space.*
import kotlinx.android.synthetic.main.app_toolbar.*

class SpaceActivity : UIActivity() {
    override fun getLayoutId() = R.layout.activity_space

    override fun initView() {
        tv_title.text = "布局测试"
        tv_rightMenu.text = "重置"
        tv_rightMenu.visibility = View.VISIBLE
        initToolBar(app_toolbar)
    }

    /**
     * 返回之前都先关掉弹窗
     */
    override fun onBackPressed() {
        SpaceLayout.onDestroy(windowManager)
        super.onBackPressed()
    }

    override fun initEvent() {
        super.initEvent()
        tv_rightMenu.setOnClickListener {
            SpaceLayout.onDestroy(windowManager)
        }
        ll_btn_empty.setOnClickListener {
            SpaceLayout.showEmptyLayout(ll_content, windowManager)
        }
        ll_btn_loading.setOnClickListener {
            SpaceLayout.showLoadingLayout(ll_content, windowManager)
        }

        ll_btn_error.setOnClickListener {
            // 防止回调事件错乱 因此回调事件的作用只有一次 即重置的时候对事件进行了回收
            SpaceLayout.setOnRetryClickedListener(R.id.retry,object :SpaceLayout.OnRetryClickedListener{
                override fun onRetryClick() {
                    SpaceLayout.onDestroy(windowManager)
                }

            })
            SpaceLayout.showNetworkErrorLayout(ll_content, windowManager)
        }
        ll_btn_fragment.setOnClickListener {
            easyStart(SpaceInFragmentActivity::class.java)
        }

    }

}
