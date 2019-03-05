package com.vincent.dialoglibrary

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/3/3 0003 <p>
 * <p>@update 2019/3/3 0003<p>
 * <p>版本号：1<p>
 *
 */
object ToastDialog {

    class Builder constructor(activity: FragmentActivity, themeResId: Int = -1) :
        BaseDialogFragment.Builder<Builder>(activity, themeResId), Runnable {
        private lateinit var mMessageView: TextView
        private lateinit var mIconView: ImageView
        private lateinit var mType: Type

        init {
            setContentView(R.layout.dialog_toast);
            setGravity(Gravity.CENTER);
            mAnimations = BaseDialog.AnimStyle.TOAST
            mCancelable = false

            mMessageView = findViewById(R.id.tv_dialog_toast_message);
            mIconView = findViewById(R.id.iv_dialog_toast_icon);
        }

        fun setType(type: Type): Builder {
            mType = type
            when (type) {
                Type.FINISH -> mIconView.setImageResource(R.mipmap.ic_dialog_tip_finish)
                Type.ERROR -> mIconView.setImageResource(R.mipmap.ic_dialog_tip_error)
                Type.WARN -> mIconView.setImageResource(R.mipmap.ic_dialog_tip_warning)
            }
            return this
        }

        fun setMessage(resId: Int): Builder {
            return setMessage(activity.getText(resId))
        }

        fun setMessage(text: CharSequence): Builder {
            mMessageView.text = text
            return this
        }

        override fun show(): BaseDialog {
            // 如果显示的类型为空就抛出异常
            if (mType == null) {
                throw IllegalArgumentException("The display type must be specified")
            }
            // 如果内容为空就抛出异常
            if ("" == mMessageView.text.toString()) {
                throw IllegalArgumentException("Dialog message not null")
            }
            // 延迟自动关闭
            mMessageView.postDelayed(this, 3000)
            return super.show()
        }


        override fun run() {
            if (mDialog != null && mDialog.isShowing()) {
                dismiss();
            }
        }

    }

    /**
     * 显示的类型
     */
    enum class Type {
        // 完成，错误，警告
        FINISH,
        ERROR, WARN
    }
}