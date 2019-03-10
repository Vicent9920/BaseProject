package com.vincent.baseproject.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.app_toolbar.*

class RegisterActivity : UIActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        tv_leftMenu.text = "登录"
        tv_leftMenu.visibility = View.VISIBLE
        initToolBar(app_toolbar,false)
    }


    override fun isSupportSwipeBack(): Boolean {
        // 注册页不建议开起侧滑
        return false
    }
}
