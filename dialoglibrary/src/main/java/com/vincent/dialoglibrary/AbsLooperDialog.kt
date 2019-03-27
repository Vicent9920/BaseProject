package com.vincent.dialoglibrary

import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.vincent.dialoglibrary.base.BaseDialog
import com.vincent.dialoglibrary.base.BaseDialogFragment
import com.vincent.dialoglibrary.widget.LoopView


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/12/17
 * desc   : 滚动选择列表基类
 */
abstract class AbsLooperDialog {

    abstract class Builder(activity: FragmentActivity) : BaseDialogFragment.Builder<Builder>(activity),
        View.OnClickListener {

        private val mCancelView: TextView
        private val mTitleView: TextView
        private val mConfirmView: TextView
        private val mLinearLayout: LinearLayout

        init {

            setContentView(R.layout.dialog_wheel)
            setGravity(Gravity.BOTTOM)
            mAnimations = BaseDialog.AnimStyle.BOTTOM

            val displayMetrics = DisplayMetrics()

            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
                displayMetrics
            )
            mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            mHeight = ViewGroup.LayoutParams.WRAP_CONTENT

            mCancelView = findViewById(R.id.tv_dialog_wheel_cancel)
            mTitleView = findViewById(R.id.tv_dialog_wheel_title)
            mConfirmView = findViewById(R.id.tv_dialog_wheel_confirm)
            mLinearLayout = findViewById(R.id.ll_dialog_wheel_list)

            mCancelView.setOnClickListener(this)
            mConfirmView.setOnClickListener(this)
        }

        fun setTitle(resId: Int): Builder {
            return setTitle(activity.resources.getText(resId))
        }

        fun setTitle(text: CharSequence): Builder {
            mTitleView.text = text
            return this
        }

        fun setCancel(resId: Int): Builder {
            return setCancel(activity.getText(resId))
        }

        fun setCancel(text: CharSequence): Builder {
            mCancelView.text = text
            return this
        }

        fun setConfirm(resId: Int): Builder {
            return setConfirm(activity.getText(resId))
        }

        fun setConfirm(text: CharSequence): Builder {
            mConfirmView.text = text
            return this
        }

        override fun onClick(v: View) {
            if (v == mCancelView) {
                onCancel()
            } else if (v == mConfirmView) {
                onConfirm()
            }
        }

        protected abstract fun onConfirm()

        protected abstract fun onCancel()

        protected fun createLoopView(): LoopView {

            val loopView = LoopView(activity)
            loopView.setTextSize(20f)
            val layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.weight = 1f
            loopView.layoutParams = layoutParams
            mLinearLayout.addView(loopView)

            return loopView
        }
    }
}