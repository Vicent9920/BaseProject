package com.vincent.baseproject.ui.space

import android.view.View
import com.vincent.baselibrary.helper.SpaceLayout
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.app_toolbar.*

class SpaceInFragmentActivity : UIActivity() {

    private lateinit var space:SpaceFragment
    override fun getLayoutId() = R.layout.activity_space_in_fragment

    override fun initView() {
        tv_title.text = "Fragment 布局测试"
        tv_rightMenu.text = "重置"
        tv_rightMenu.visibility = View.VISIBLE
        initToolBar(app_toolbar)
        space = SpaceFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, space).commit()
    }


    override fun initEvent() {
        super.initEvent()
        tv_rightMenu.setOnClickListener {
            SpaceLayout.onDestroy(space)
        }

    }
}
