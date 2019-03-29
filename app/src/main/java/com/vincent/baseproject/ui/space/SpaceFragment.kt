package com.vincent.baseproject.ui.space


import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return R.layout.fragment_space
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null && getLayoutId() > 0) {
            mRootView = layoutInflater.inflate(getLayoutId(), container, false)
        }
        container?.removeView(mRootView)
        return SpaceLayout.LoadLayout(mRootView!!)
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
                SpaceLayout.setOnFragmentRetryClickedListener(
                    this@SpaceFragment,
                    R.id.retry,
                    object : SpaceLayout.OnRetryClickedListener {
                        override fun onRetryClick() {
                            SpaceLayout.onDestroy(this@SpaceFragment)
                        }

                    })
                SpaceLayout.showNetworkErrorLayout(this)
            }

        }
    }
}
