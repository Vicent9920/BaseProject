package com.vincent.baselibrary.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.R

/**
 * 创建日期：2019/3/6 0006on 上午 11:17
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：Fragment 懒加载基类
 */
@SuppressLint("InflateParams")
abstract class BaseLazyFragment : Fragment() {
    // Activity对象
    lateinit var mActivity: AppCompatActivity
    // 根布局
    var mRootView: View? = null
    // 是否进行过懒加载
    private var isLazyLoad: Boolean = false
    // Fragment 是否可见
    private var isFragmentVisible: Boolean = false
    // 是否是 replace Fragment 的形式
    private var isReplaceFragment: Boolean = false

    open val toastCustom by lazy {
        // 创建自定义的Toast.
        val layout = LayoutInflater.from(context).inflate(R.layout.toast_custom_layout, null)
        EasyToast.newBuilder(layout, R.id.toast_tv)
            .setGravity(Gravity.CENTER, 0, 0)
            .build()
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null && getLayoutId() > 0) {
            mRootView = layoutInflater.inflate(getLayoutId(), container, false)
        }
        container?.removeView(mRootView)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isReplaceFragment) {
            if (isFragmentVisible) {
                initLazyLoad()
            }
        } else {
            initLazyLoad()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isReplaceFragment = true
        this.isFragmentVisible = isVisibleToUser
        if (isVisibleToUser && view != null) {
            if (!isLazyLoad) {
                initLazyLoad()
            } else {
                // 从不可见到可见
                onRestart()
            }
        }
    }


    // 引入布局
    abstract fun getLayoutId(): Int

    // 初始化控件
    protected abstract fun initView()

    // 初始化数据
    open fun initData() {}

    // 初始化响应事件
    open fun initEvent() {}

    /**
     * 跟 Activity 的同名方法效果一样
     */
    open fun onRestart() {
        // 从可见的状态变成不可见状态，再从不可见状态变成可见状态时回调
    }

    open fun initLazyLoad() {
        if (!isLazyLoad) {
            isLazyLoad = true
            initFragment()
        }
    }

    open fun initFragment() {
        initView()
        initData()
        initEvent()
    }

    /**
     * Fragment返回键被按下时回调
     */
    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        //默认不拦截按键事件，传递给Activity
        return false
    }

    /**
     * 初始化Toolbar
     * @param toolbar 需要改变的toolbar
     * @param isShowBack 是否需要显示返回键
     * @param isCloseTitle 是否需要关闭默认title
     */

    fun initToolBar(toolbar: Toolbar, isShowBack: Boolean = false, isCloseTitle: Boolean = false) {
        val ac = mActivity as BaseActivity
        if (ac.isStatusBarEnabled() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val layoutParams = toolbar.layoutParams
            if (layoutParams.height != -2 && layoutParams.height != -1) {
                layoutParams.height += getSystemBarHeight()
                toolbar.setPadding(
                    toolbar.paddingLeft,
                    toolbar.paddingTop + getSystemBarHeight(),
                    toolbar.paddingRight,
                    toolbar.paddingBottom
                )
            } else {
                toolbar.post {
                    layoutParams.height = toolbar.height + getSystemBarHeight()
                    toolbar.setPadding(
                        toolbar.paddingLeft,
                        toolbar.paddingTop + getSystemBarHeight(),
                        toolbar.paddingRight,
                        toolbar.paddingBottom
                    )
                }
            }
        }
        mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(isShowBack)
        mActivity.supportActionBar?.setDisplayShowTitleEnabled(isCloseTitle)
    }

    fun toast(resString: Int) {
        toastCustom.show(resString)
    }

    fun toast(s: String) {
        toastCustom.show(s)
    }

    fun getSystemBarHeight(): Int {
        val resources = resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }


}