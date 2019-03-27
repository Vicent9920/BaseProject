package com.vincent.dialoglibrary.base


import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatDialog
import com.vincent.dialoglibrary.R


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/2/26 0026 <p>
 * <p>@update 2019/2/26 0026<p>
 * <p>版本号：1<p>
 *
 */
class BaseDialog(context: Context?, val themeResId: Int = R.style.BaseDialogStyle) :
    AppCompatDialog(context, themeResId) {

    var mCancelable = true
        set(value) {
            super.setCancelable(value)
            field = value
        }


    open class Builder<B : Builder<B>> constructor(val mContext: Context, val themeResId: Int = -1) {
         lateinit var mDialog: BaseDialog


        // Dialog 布局
        lateinit var mContentView: View

        // Dialog Cancel 监听
         var mOnCancelListener: DialogInterface.OnCancelListener? = null
        // Dialog Dismiss 监听
         var mOnDismissListener: DialogInterface.OnDismissListener? = null
        // Dialog Key 监听
         var mOnKeyListener: DialogInterface.OnKeyListener? = null

        // 点击空白是否能够取消  默认点击阴影可以取消
        var mCancelable = true

        private val mTextArray = SparseArray<CharSequence>()
        private val mVisibilityArray = SparseArray<Int>()
        private val mBackgroundArray = SparseArray<Drawable>()
        private val mImageArray = SparseArray<Drawable>()
        private val mClickArray = SparseArray<OnClickListener<View>>()

        // 主题
        private val mThemeResId = -1
        // 动画
         var mAnimations = -1
        // 位置
        private var mGravity = Gravity.CENTER
        // 宽度和高度
         var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
         var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        // 垂直和水平边距
        private val mVerticalMargin: Float = 0f
        private val mHorizontalMargin: Float = 0f

        /**
         * 根据 id 查找 View（仅供子类调用）
         */
        protected fun <T : View> findViewById(@IdRes id: Int): T {
            return mContentView.findViewById(id)
        }

        /**
         * 销毁当前 Dialog（仅供子类调用）
         */
        open fun dismiss() {
            mDialog?.dismiss()
        }



        /**
         * dp转px（仅供子类调用）
         */
        protected fun dp2px(dpValue: Float): Int {
            val scale = mContext.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * sp转px（仅供子类调用）
         */
        protected fun sp2px(spValue: Float): Int {
            val fontScale = mContext.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * 设置布局
         */
        fun setContentView(layoutId: View): B {
            mContentView = layoutId
            return this as B
        }

        /**
         * 设置布局
         */
        fun setContentView(layoutId: Int): B {
            return setContentView(LayoutInflater.from(mContext).inflate(layoutId, null))
        }

        /**
         * 设置重心位置
         */
        fun setGravity(gravity: Int): B {
            var gravity = Gravity.getAbsoluteGravity(gravity, mContext.resources.configuration.layoutDirection)
            mGravity = gravity
            if (mAnimations == -1) {
                when (mGravity) {
                    Gravity.TOP -> mAnimations = AnimStyle.TOP
                    Gravity.BOTTOM -> mAnimations = AnimStyle.BOTTOM
                    Gravity.START -> mAnimations = AnimStyle.LEFT
                    Gravity.END -> mAnimations = AnimStyle.RIGHT
                }
            }
            return this as B
        }

        /**
         * 占满宽度
         */
        fun fullWidth(): B {
            mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this as B
        }

        /**
         * 占满高度
         */
        fun fullHeight(): B {
            mHeight = ViewGroup.LayoutParams.MATCH_PARENT
            return this as B
        }

        /**
         * 设置文本
         */
        fun setText(@IdRes id: Int, resId: Int): B {
            return setText(id, mContext.resources.getString(resId))
        }

        /**
         * 设置文本
         */
        fun setText(@IdRes id: Int, text: CharSequence): B {
            mTextArray.put(id, text)
            return this as B
        }

        /**
         * 设置可见状态
         */
        fun setVisibility(@IdRes id: Int, visibility: Int): B {
            mVisibilityArray.put(id, visibility)
            return this as B
        }

        /**
         * 设置设置背景
         */
        fun setBackground(@IdRes id: Int, resId: Int): B {
            return setBackground(id, mContext.resources.getDrawable(resId))
        }

        /**
         * 设置背景
         */
        fun setBackground(@IdRes id: Int, drawable: Drawable): B {
            mBackgroundArray.put(id, drawable)
            return this as B
        }

        /**
         * 设置设置背景
         */
        fun setImageDrawable(@IdRes id: Int, resId: Int): B {
            return setBackground(id, mContext.resources.getDrawable(resId))
        }

        /**
         * 设置背景
         */
        fun setImageDrawable(@IdRes id: Int, drawable: Drawable): B {
            mImageArray.put(id, drawable)
            return this as B
        }

        /**
         * 设置点击事件
         */
        fun setOnClickListener(@IdRes id: Int, listener: OnClickListener<View>): B {
            mClickArray.put(id, listener)
            return this as B
        }

        /**
         * 创建
         */
        open fun create(): BaseDialog {

            val layoutParams = mContentView.layoutParams
            layoutParams?.let {
                if (mWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    mWidth = layoutParams.width
                }
                if (mHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    mHeight = layoutParams.height
                }
            }


            // 判断有没有设置主题
            if (mThemeResId == -1) {
                mDialog = BaseDialog(mContext)
            } else {
                mDialog = BaseDialog(mContext, mThemeResId)
            }

            mDialog.setContentView(mContentView)

            mDialog.setCancelable(mCancelable)
            if (mCancelable) {
                mDialog.setCanceledOnTouchOutside(true)
            }
            mOnCancelListener?.let {
                mDialog.setOnCancelListener(mOnCancelListener)
            }
            mOnDismissListener?.let {
                mDialog.setOnDismissListener(mOnDismissListener)
            }
            mOnKeyListener?.let {
                mDialog.setOnKeyListener(mOnKeyListener)
            }


            // 判断有没有设置动画
            if (mAnimations == -1) {
                // 没有的话就设置默认的动画
                mAnimations = AnimStyle.DEFAULT
            }

            // 设置参数
            val params = mDialog.window.attributes
            params.width = mWidth
            params.height = mHeight
            params.gravity = mGravity
            params.windowAnimations = mAnimations
            params.horizontalMargin = mHorizontalMargin
            params.verticalMargin = mVerticalMargin
            mDialog.window.attributes = params

            // 设置文本
            for (i in 0 until mTextArray.size()) {
                (mContentView.findViewById(mTextArray.keyAt(i)) as TextView).text = mTextArray.valueAt(i)
            }

            // 设置可见状态
            for (i in 0 until mVisibilityArray.size()) {
                mContentView.findViewById<View>(mVisibilityArray.keyAt(i)).setVisibility(mVisibilityArray.valueAt(i))
            }

            // 设置背景
            for (i in 0 until mBackgroundArray.size()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentView.findViewById<View>(mBackgroundArray.keyAt(i))
                        .setBackground(mBackgroundArray.valueAt(i))
                }
            }

            // 设置图片
            for (i in 0 until mImageArray.size()) {
                (mContentView.findViewById(mImageArray.keyAt(i)) as ImageView).setImageDrawable(mImageArray.valueAt(i))
            }

            // 设置点击事件
            for (i in 0 until mClickArray.size()) {
                ViewClickHandler(
                    mDialog,
                    mContentView.findViewById<View>(mClickArray.keyAt(i)),
                    mClickArray.valueAt(i)
                )
            }

            return mDialog
        }

        /**
         * 显示
         */
        open fun show(): BaseDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }


    }

    object AnimStyle {

        // 默认动画效果
        internal val DEFAULT = R.style.DialogScaleAnim

        // 缩放动画
        val SCALE = R.style.DialogScaleAnim

        // IOS 动画
        val IOS = R.style.DialogIOSAnim

        // 吐司动画
        val TOAST = android.R.style.Animation_Toast

        // 顶部弹出动画
        val TOP = R.style.DialogTopAnim

        // 底部弹出动画
        val BOTTOM = R.style.DialogBottomAnim

        // 左边弹出动画
        val LEFT = R.style.DialogLeftAnim

        // 右边弹出动画
        val RIGHT = R.style.DialogRightAnim
    }

    interface OnClickListener<V : View> {
        fun onClick(dialog: Dialog, view: V)
    }

    /**
     * 处理点击事件类
     */
    private class ViewClickHandler internal constructor(
        private val mDialog: BaseDialog,
        view: View,
        private val mListener: OnClickListener<View>
    ) : View.OnClickListener {

        init {

            view.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            mListener.onClick(mDialog, v)
        }
    }

}

