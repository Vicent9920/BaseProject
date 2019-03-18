package com.vincent.baseproject.ui

import com.vincent.baselibrary.base.BaseActivity
import com.vincent.baseproject.R
import kotlinx.android.synthetic.main.app_toolbar.*

class SettingActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_setting

    override fun initView() {
        tv_title.text = "设置"
        initToolBar(app_toolbar)
    }


}
