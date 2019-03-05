package com.vincent.dialoglibrary

import android.app.Dialog
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment


/**
 * 创建日期：2019/3/5 0005on 下午 2:29
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object MessageDialog {

    class Builder(activity: FragmentActivity, themeResId: Int = -1) :
        BaseDialogFragment.Builder<Builder>(activity, themeResId),
        View.OnClickListener {


         var mListener: OnListener? = null
         var mAutoDismiss = true // 设置点击按钮后自动消失

        private lateinit var mTitleView: TextView
        private lateinit var mMessageView: TextView

        private lateinit var mCancelView: TextView
        private lateinit var mLineView: View
        private lateinit var mConfirmView: TextView

        init {
            setContentView(R.layout.dialog_message)
            mAnimations = BaseDialog.AnimStyle.IOS
            setGravity(Gravity.CENTER)

            mTitleView = findViewById(R.id.tv_dialog_message_title)
            mMessageView = findViewById(R.id.tv_dialog_message_message)

            mCancelView = findViewById(R.id.tv_dialog_message_cancel)
            mLineView = findViewById(R.id.v_dialog_message_line)
            mConfirmView = findViewById(R.id.tv_dialog_message_confirm)

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

        fun setMessage(resId: Int): Builder {
            return setMessage(activity.getText(resId))
        }

        fun setMessage(text: CharSequence): Builder {
            mMessageView.text = text
            return this
        }

        fun setCancel(resId: Int): Builder {
            return setCancel(activity.getText(resId))
        }

        fun setCancel(text: CharSequence): Builder {
            mCancelView.text = text

            mCancelView.visibility = if (isEmpty(text)) View.GONE else View.VISIBLE
            mLineView.visibility = if (isEmpty(text)) View.GONE else View.VISIBLE
            mConfirmView.setBackgroundResource(
                if (isEmpty(text))
                    R.drawable.dialog_message_one_button
                else
                    R.drawable.dialog_message_right_button
            )
            return this
        }

        fun setConfirm(resId: Int): Builder {
            return setConfirm(activity.getText(resId))
        }

        fun setConfirm(text: CharSequence): Builder {
            mConfirmView.text = text
            return this
        }

        override fun create(): BaseDialog {
            // 如果标题为空就隐藏
            if ("" == mTitleView.text.toString()) {
                mTitleView.visibility = View.GONE
            }
            // 如果内容为空就抛出异常
            if ("" == mMessageView.text.toString()) {
                throw IllegalArgumentException("Dialog message not null")
            }
            return super.create()
        }

        override fun onClick(v: View?) {
            if (mAutoDismiss) {
                dismiss()
            }

            if (mListener == null) return

            if (v === mConfirmView) {
                mListener?.confirm(mDialog)
            } else if (v === mCancelView) {
                mListener?.cancel(mDialog)
            }
        }
    }

    interface OnListener {

        /**
         * 点击确定时回调
         */
        fun confirm(dialog: Dialog)

        /**
         * 点击取消时回调
         */
        fun cancel(dialog: Dialog)
    }
}