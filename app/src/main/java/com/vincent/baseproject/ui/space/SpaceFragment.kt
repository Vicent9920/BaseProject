package com.vincent.baseproject.ui.space


import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vincent.baselibrary.base.BaseActivityHandler
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baselibrary.helper.SpaceLayout

import com.vincent.baseproject.R
import kotlinx.android.synthetic.main.fragment_space.*


/**
 * @author Vincent
 * 测试Fragment
 */
class SpaceFragment : BaseLazyFragment() {
    override fun getLayoutId(): Int {
        // 根布局一定需要设置id且必须是ViewGroup
        return R.layout.fragment_space
    }

    override fun initView() {

    }

    override fun initEvent() {
        super.initEvent()
        fragment_btn_empty.setOnClickListener {
            Handler().post {
                SpaceLayout.showEmptyLayout(this)
            }

        }
        fragment_btn_loading.setOnClickListener {
            Handler().post {
                SpaceLayout.showLoadingLayout(this)
            }

        }

        fragment_btn_error.setOnClickListener {
            Handler().post {
                SpaceLayout.setOnRetryClickedListener(R.id.retry,object :SpaceLayout.OnRetryClickedListener{
                    override fun onRetryClick() {
                        SpaceLayout.onDestroy(this@SpaceFragment)
                    }

                })
                SpaceLayout.showNetworkErrorLayout(this)
            }

        }
    }
}
