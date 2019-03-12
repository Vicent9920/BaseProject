package com.vincent.baseproject.ui.home.fragment


import android.content.Intent
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baseproject.R
import com.vincent.baseproject.ui.DialogActivity
import com.vincent.baseproject.ui.WebActivity
import com.vincent.baseproject.ui.login.LoginActivity
import com.vincent.baseproject.ui.login.RegisterActivity
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.home_fragment_d.*


class HomeFragmentD : BaseLazyFragment() {
    override fun getLayoutId() = R.layout.home_fragment_d

    override fun initView() {
       tv_title.text = "我的"
        initToolBar(app_toolbar)
    }

    override fun initEvent() {
        super.initEvent()
        homeD_btn_dialog.setOnClickListener {
            startActivity(Intent(mActivity, DialogActivity::class.java))
        }
        homeD_btn_login.setOnClickListener {
            startActivity(Intent(mActivity,LoginActivity::class.java))
        }
        homeD_btn_register.setOnClickListener {
            startActivity(Intent(mActivity,RegisterActivity::class.java))
        }
        homeD_btn_browser.setOnClickListener {
            startActivity(Intent(mActivity,WebActivity::class.java).putExtra("url","https://www.pgyer.com/he3F"))
        }
    }

}
