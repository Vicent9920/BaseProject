package com.vincent.baseproject.ui.home.fragment


import android.view.Gravity
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baseproject.R
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.home_fragment_b.*


/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragmentB : BaseLazyFragment() {
    open val toastCustom by lazy {
        // 创建自定义的Toast.
        val layout = LayoutInflater.from(context).inflate(com.vincent.baselibrary.R.layout.toast_custom_layout, null)
        EasyToast.newBuilder(layout, com.vincent.baselibrary.R.id.toast_tv)
            .setGravity(Gravity.CENTER, 0, 0)
            .build()
    }
    fun toast(resString: Int) {
        toastCustom.show(resString)
    }

    fun toast(s: String) {
        toastCustom.show(s)
    }
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
