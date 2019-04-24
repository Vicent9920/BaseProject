package com.vincent.baselibrary.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

/**
 * 创建日期：2019/3/28 0028on 上午 9:48
 * 描述：空数据等页面布局
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
@SuppressLint("StaticFieldLeak")
object SpaceLayout {

    private lateinit var emptyLayout: View
    private lateinit var loadingLayout: View
    private lateinit var networkErrorLayout: View
    private var currentLayout: View? = null
    private lateinit var mContext: Context
    private var isAresShowing = false
    private var onRetryClickedListener: OnRetryClickedListener? = null
    private var retryId = 0


    /**
     * 初始化
     */
    fun init(context: Context) {
        mContext = context
    }

    /**
     * 设置空数据界面的布局
     */
    fun setEmptyLayout(resId: Int) {
        emptyLayout = getLayout(resId)
        emptyLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * 设置加载中界面的布局
     */
    fun setLoadingLayout(resId: Int) {
        loadingLayout = getLayout(resId)
        loadingLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * 设置网络错误界面的布局
     */
    fun setNetworkErrorLayout(resId: Int) {
        networkErrorLayout = getLayout(resId)
        networkErrorLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    }

    /**
     * 展示空数据界面
     * target的大小及位置决定了window界面在实际屏幕中的展示大小及位置
     */
    fun showEmptyLayout(target: View, wm: WindowManager) {
        if (currentLayout != null) {
            wm.removeView(currentLayout)
        }
        isAresShowing = true
        currentLayout = emptyLayout
        wm.addView(currentLayout, setLayoutParams(target))
    }


    /**
     * 展示加载中界面
     * target的大小及位置决定了window界面在实际屏幕中的展示大小及位置
     */
    fun showLoadingLayout(target: View, wm: WindowManager) {
        if (currentLayout != null) {
            wm.removeView(currentLayout)
        }
        isAresShowing = true
        currentLayout = loadingLayout
        wm.addView(currentLayout, setLayoutParams(target))
    }


    /**
     * 展示网络错误界面
     * target的大小及位置决定了window界面在实际屏幕中的展示大小及位置
     */
    fun showNetworkErrorLayout(target: View, wm: WindowManager) {
        if (currentLayout != null) {
            wm.removeView(currentLayout)
        }
        isAresShowing = true
        onRetryClickedListener?.let { listener ->
            networkErrorLayout.findViewById<View>(retryId).setOnClickListener {
                listener.onRetryClick()
            }
        }

        currentLayout = networkErrorLayout
        wm.addView(currentLayout, setLayoutParams(target))
    }






    private fun setLayoutParams(target: View): WindowManager.LayoutParams {
        val wlp = WindowManager.LayoutParams()
        wlp.format = PixelFormat.TRANSPARENT
        wlp.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        val location = IntArray(2)
        target.getLocationOnScreen(location)
        wlp.x = location[0]
        wlp.y = location[1]
        wlp.height = target.height
        wlp.width = target.width
        wlp.type = WindowManager.LayoutParams.FIRST_SUB_WINDOW
        wlp.gravity = Gravity.START or Gravity.TOP
        return wlp
    }

    private fun getLayout(resId: Int): ViewGroup {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(resId, null) as ViewGroup
    }

    interface OnRetryClickedListener {
        fun onRetryClick()
    }

    fun setOnRetryClickedListener(id: Int, listener: OnRetryClickedListener) {
        retryId = id
        onRetryClickedListener = listener
    }

    fun onDestroy(wm: WindowManager) {
        isAresShowing = false
        currentLayout?.let {
            wm.removeView(currentLayout)
            currentLayout = null
        }
        // 重置 防止在不同的页面调用相同回调事件
        retryId = 0
        onRetryClickedListener = null
    }

    /**
     * 展示空数据界面
     *
     */
    fun showEmptyLayout(target: Fragment, empty: View = emptyLayout) {
        showFragmentLayout(false, target, empty)
    }

    /**
     * 展示加载中界面
     *
     */
    fun showLoadingLayout(target: Fragment, empty: View = loadingLayout) {
        showFragmentLayout(false, target, empty)
    }

    /**
     * 展示网络错误界面
     *
     */
    fun showNetworkErrorLayout(
        target: Fragment,
        empty: View = networkErrorLayout,
        id: Int = 0,
        listener: OnRetryClickedListener? = null
    ) {
        if (id != 0) {
            setOnFragmentRetryClickedListener(target, id, listener)

        }
        showFragmentLayout(true, target, empty)
    }

    fun setOnFragmentRetryClickedListener(target: Fragment, id: Int = 0, listener: OnRetryClickedListener? = null) {
        if (target.view!! !is LoadLayout) {
            throw RuntimeException("请在 onCreateView 方法处将根View替换为 LoadLayout")
        }
        val loadLayout = target.view as LoadLayout
        loadLayout.setListener(id, listener)
    }

    /**
     * 重置 Fragment 状态
     */
    fun onDestroy(target: Fragment) {
        if (target.view!! !is LoadLayout) {
            throw RuntimeException("请在 onCreateView 方法处将根View替换为 LoadLayout")
        }
        val loadLayout = target.view as LoadLayout
        loadLayout.restView()
    }

    /**
     * Fragment 显示状态View
     * fragment Root View 必须设置 id
     */
    private fun showFragmentLayout(isRetry: Boolean, target: Fragment, empty: View) {
        if (target.view!! !is LoadLayout) {
            throw RuntimeException("请在 onCreateView 方法处将根View替换为 LoadLayout")
        }
        val loadLayout = target.view as LoadLayout
        if (isRetry) {
            loadLayout.showNetworkErrorLayout(empty)
        } else {
            loadLayout.showView(empty)
        }


    }


    @SuppressLint("ViewConstructor")
    class LoadLayout(private val mView: View) : FrameLayout(mView.context) {
        private var retryId = 0
        private var mListener: OnRetryClickedListener? = null

        init {
            addView(mView)
        }

        fun setListener(id: Int, listener: OnRetryClickedListener?) {
            this.retryId = id
            this.mListener = listener
        }

        fun showNetworkErrorLayout(spaceView: View) {
            if (retryId != 0) {
                spaceView.findViewById<View>(retryId).setOnClickListener {
                    mListener?.onRetryClick()
                }
            }
            showView(spaceView)
        }

        fun showView(spaceView: View) {
            mView.visibility = View.GONE
            if (childCount > 1) {
                removeViewAt(1)
            }
            addView(spaceView, 1)
        }

        fun restView() {
            mView.visibility = View.VISIBLE
            if (childCount > 1) {
                removeViewAt(1)
            }
        }

    }

}