package com.vincent.baseproject.ui.home.fragment

import android.view.View
import androidx.core.content.ContextCompat
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baseproject.R
import com.vincent.baseproject.widget.XCollapsingToolbarLayout
import kotlinx.android.synthetic.main.home_fragment_a.*

/**
 * 创建日期：2019/3/6 0006on 下午 4:32
 * 描述：首页
 * @author：Vincent
 * QQ：3332168769
 * 备注：滑动到指定距离头部区域改变
 */
class HomeFragmentA : BaseLazyFragment() {
    override fun getLayoutId(): Int {
        return R.layout.home_fragment_a
    }

    override fun initView() {
        setTitleBar(top_toolbar)
        ctl_top_bar.mListener = object : XCollapsingToolbarLayout.OnScrimsListener {
            override fun onScrimsStateChange(shown: Boolean) {
                if (shown) {
                    homeA_tv_address.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                } else {
                    homeA_tv_address.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                }
                homeA_tv_search.isSelected = shown
            }

        }
    }


    private fun setTitleBar(view: View) {
        val layoutParams = view.layoutParams
        if (layoutParams.height != -2 && layoutParams.height != -1) {
            layoutParams.height += getSystemBarHeight()
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + getSystemBarHeight(),
                view.paddingRight,
                view.paddingBottom
            )
        } else {
            view.post {
                layoutParams.height = view.height + getSystemBarHeight()
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop + getSystemBarHeight(),
                    view.paddingRight,
                    view.paddingBottom
                )
            }
        }
    }


}