package com.vincent.dialoglibrary

import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment
import com.vincent.dialoglibrary.widget.ProgressView


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/3/4 0004 <p>
 * <p>@update 2019/3/4 0004<p>
 * <p>版本号：1<p>
 *
 */
class WaitDialog {
    companion object {
        class Builder(activity: FragmentActivity, themeResId: Int) :
            BaseDialogFragment.Builder<Builder>(activity, themeResId) {
            private lateinit var mMessageView: TextView
            private lateinit var mProgressView: ProgressView

            init {
                setContentView(R.layout.dialog_wait);
                setGravity(Gravity.CENTER);
                mAnimations = BaseDialog.AnimStyle.TOAST
                mCancelable = false
                mMessageView = findViewById(R.id.tv_dialog_wait_message);
                mProgressView = findViewById(R.id.pv_dialog_wait_progress);
            }

            fun setMessage(resId: Int): Builder {
                return setMessage(mContext.getText(resId))
            }

            fun setMessage(text: CharSequence?): Builder {
                mMessageView.text = text
                mMessageView.visibility = if (text == null) View.GONE else View.VISIBLE
                return this
            }

            override fun show(): BaseDialog {
                // 如果内容为空就设置隐藏
                if ("" == mMessageView.text.toString()) {
                    mMessageView.visibility = View.GONE
                }
                return super.show()
            }
        }
    }
}