package com.vincent.baseproject.ui.home.fragment


import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baseproject.R
import com.vincent.baseproject.common.BaseUiFragment
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.home_fragment_b.*


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragmentB : BaseUiFragment() {

    override fun getLayoutId(): Int {
        return R.layout.home_fragment_b
    }

    override fun initView() {
        tv_title.text = "发现"
        initToolBar(app_toolbar)
    }

    override fun initEvent() {
        super.initEvent()
        homeB_cv_countdown.setOnClickListener {
            toast(R.string.countdown_code_send_succeed)

        }
    }

}
