package com.vincent.dialoglibrary

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment
import com.vincent.dialoglibrary.widget.PASSWORD_COUNT
import com.vincent.dialoglibrary.widget.PasswordView
import java.util.*


/**
 * 创建日期：2019/3/5 0005on 下午 2:43
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object PayPasswordDialog {

    class Builder(activity: FragmentActivity, themeResId: Int = -1) :
        BaseDialogFragment.Builder<Builder>(activity, themeResId),
        View.OnClickListener, OnItemClickListener {

        // 输入键盘文本
        private val KEYBOARD_TEXT = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "")

         var mListener: OnListener? = null
         var mAutoDismiss = true

        private lateinit var mPasswordView: PasswordView
        private lateinit var mRecyclerView: RecyclerView
        private val mRecordList = LinkedList<String>()
        private lateinit var mTitleView: TextView
        private lateinit var mSubTitleView: TextView
        private lateinit var mMoneyView: TextView
        private lateinit var mCloseView: ImageView

        init {
            setContentView(R.layout.dialog_pay_password)
            setGravity(Gravity.BOTTOM)
            mAnimations = BaseDialog.AnimStyle.BOTTOM
            fullWidth()
            mCancelable = false

            mRecyclerView = findViewById(R.id.rv_dialog_pay_list)
            mPasswordView = findViewById(R.id.pw_dialog_pay_view)
            mTitleView = findViewById(R.id.tv_dialog_pay_title)
            mSubTitleView = findViewById(R.id.tv_dialog_pay_sub_title)
            mMoneyView = findViewById(R.id.tv_dialog_pay_money)
            mCloseView = findViewById(R.id.iv_dialog_pay_close)

            mCloseView.setOnClickListener(this)

            mRecyclerView.layoutManager = GridLayoutManager(activity, 3)
            val keyBoardAdapter = keyboardAdapter(activity)
            keyBoardAdapter.setData(Arrays.asList(*KEYBOARD_TEXT))
            keyBoardAdapter.listener = this
            mRecyclerView.adapter = keyBoardAdapter
        }

        fun setTitle(resId: Int): Builder {
            return setTitle(activity.getText(resId))
        }

        fun setTitle(title: CharSequence?): Builder {
            if (title != null && "" != title.toString()) {
                mTitleView.text = title
                mTitleView.visibility = View.VISIBLE
            } else {
                mTitleView.visibility = View.GONE
            }
            return this
        }

        fun setSubTitle(resId: Int): Builder {
            return setSubTitle(activity.getText(resId))
        }

        fun setSubTitle(subTitle: CharSequence?): Builder {
            if (subTitle != null && "" != subTitle.toString()) {
                mSubTitleView.text = subTitle
                mSubTitleView.visibility = View.VISIBLE
            } else {
                mSubTitleView.visibility = View.GONE
            }
            return this
        }

        fun setMoney(resId: Int): Builder {
            return setSubTitle(activity.getText(resId))
        }

        fun setMoney(money: CharSequence?): Builder {
            if (money != null && "" != money.toString()) {
                mMoneyView.text = money
                mMoneyView.visibility = View.VISIBLE
            } else {
                mMoneyView.visibility = View.GONE
            }
            return this
        }

        override fun onClick(v: View?) {
            if (v === mCloseView) {

                if (mAutoDismiss) {
                    dismiss()
                }
                mListener?.cancel(mDialog)
            }
        }

        override fun onItemClick(itemView: View, position: Int) {
            when (position) {
                9 -> { // 点击空白的地方不做任何操作
                }
                11 -> {
                    if (mRecordList.isNotEmpty()) {
                        mRecordList.removeLast()
                    }
                }
                else -> {
                    // 判断密码是否已经输入完毕
                    if (mRecordList.size < PASSWORD_COUNT) {
                        // 点击数字，显示在密码行
                        mRecordList.add(KEYBOARD_TEXT[position])
                    }

                    // 判断密码是否已经输入完毕
                    if (mRecordList.size == PASSWORD_COUNT) {
                        mListener?.let {
                            mPasswordView.postDelayed({
                                if (mAutoDismiss) {
                                    dismiss()
                                }
                                // 获取输入的支付密码
                                val password = StringBuilder()
                                for (s in mRecordList) {
                                    password.append(s)
                                }
                                it.complete(mDialog, password.toString())
                            }, 300)
                        }
                    }
                }
            }
        }

    }

    private class keyboardAdapter internal constructor(context: Context) :
        RecyclerView.Adapter<keyboardAdapter.ViewHolder>() {

        private var mDataSet: List<String>? = null
        var listener: OnItemClickListener? = null

        override fun getItemCount(): Int {
            mDataSet?.let {
                return it.size
            } ?: return 0
        }

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): keyboardAdapter.ViewHolder {
            return ViewHolder(parent, R.layout.item_dialog_pay_password)
        }

        override fun onBindViewHolder(@NonNull holder: keyboardAdapter.ViewHolder, position: Int) {
            holder.mTextView.text = getItem(position)

            when (position) {
                9 -> {
                    holder.mTextView.setBackgroundColor(-0x2a2725)
                    holder.mTextView.visibility = View.VISIBLE
                    holder.mImageView.visibility = View.GONE
                    holder.itemView.setBackgroundColor(-0x131314)
                }
                11 -> {
                    holder.mTextView.setBackgroundColor(-0x2a2725)
                    holder.mTextView.visibility = View.GONE
                    holder.mImageView.visibility = View.VISIBLE
                    holder.itemView.setBackgroundResource(R.drawable.dialog_pay_password_item_del_selector)
                }

            }

            holder.itemView.setOnClickListener {
                listener?.onItemClick(it, position)
            }
        }

        fun setData(data: List<String>) {
            mDataSet = data
            notifyDataSetChanged()
        }

        fun getItem(position: Int): String {
            return mDataSet!![position]
        }

        internal inner class ViewHolder(parent: ViewGroup, layoutId: Int) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        ) {

            val mTextView: TextView = itemView.findViewById(R.id.tv_dialog_pay_key) as TextView
            val mImageView: ImageView = itemView.findViewById(R.id.iv_dialog_pay_delete) as ImageView


        }
    }

    /**
     * RecyclerView 条目点击监听类
     */
    interface OnItemClickListener {

        /**
         * 当 RecyclerView 某个条目被点击时回调
         *
         * @param itemView      被点击的条目对象
         * @param position      被点击的条目位置
         */
        fun onItemClick(itemView: View, position: Int)
    }

    interface OnListener {

        /**
         * 输入完成时回调
         *
         * @param password 六位支付密码
         */
        fun complete(dialog: Dialog, password: String)

        /**
         * 点击取消时回调
         */
        fun cancel(dialog: Dialog)
    }
}