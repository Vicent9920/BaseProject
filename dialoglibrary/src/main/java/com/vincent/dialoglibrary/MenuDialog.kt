package com.vincent.dialoglibrary

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment
import java.util.*

/**
 * 创建日期：2019/3/5 0005on 下午 1:18
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object MenuDialog {

    class Builder(activity: FragmentActivity, themeResId: Int = -1) :
        BaseDialogFragment.Builder<Builder>(activity, themeResId), View.OnClickListener, OnItemClickListener {


         var mListener: OnListener? = null
         var mAutoDismiss = true

        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mAdapter: MenuAdapter
        private lateinit var mCancelView: TextView

        init {
            setContentView(R.layout.dialog_menu)
            setGravity(Gravity.BOTTOM)
            mAnimations = BaseDialog.AnimStyle.BOTTOM
            fullWidth()

            mRecyclerView = findViewById(R.id.rv_dialog_menu_list)
            mCancelView = findViewById(R.id.tv_dialog_menu_cancel)

            mRecyclerView.layoutManager = LinearLayoutManager(activity)
            mAdapter = MenuAdapter(activity)
            mAdapter.listener = this
            mRecyclerView.adapter = mAdapter

            mCancelView.setOnClickListener(this)
        }

        fun setList(vararg data: String): Builder {
            return setList(Arrays.asList(*data))
        }

        fun setList(data: List<String>): Builder {
            mAdapter.setData(data)
            return this
        }

        fun setCancel(resId: Int): Builder {
            return setCancel(activity.getText(resId))
        }

        fun setCancel(text: CharSequence?): Builder {
            mCancelView.text = text
            mCancelView.visibility = if (isEmpty(text)) View.GONE else View.VISIBLE
            return this
        }

        override fun onItemClick(itemView: View, position: Int) {
            if (mAutoDismiss) {
                dismiss()
            }
            mListener?.select(mDialog, position, mAdapter.getItem(position))
        }

        override fun onClick(v: View?) {
            if (mAutoDismiss) {
                dismiss()
            }

            if (v === mCancelView) {
                mListener?.cancel(mDialog)

            }
        }


    }

    private class MenuAdapter internal constructor(val context: Context) : RecyclerView.Adapter<ViewHolder>() {
        private var mDataSet: List<String>? = null
        var listener: OnItemClickListener? = null
        override fun getItemCount(): Int {
            mDataSet?.let {
                return it.size
            } ?: return 0
        }

        @NonNull
        override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(parent, R.layout.item_dialog_menu)
        }

        override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
            holder.mTextView.text = getItem(position)

            if (position == 0) {
                // 当前是否只有一个条目
                if (itemCount == 1) {
                    holder.itemView.setBackgroundResource(R.drawable.dialog_menu_item)
                    holder.mView.visibility = View.GONE
                } else {
                    holder.itemView.setBackgroundResource(R.drawable.dialog_menu_item_top)
                    holder.mView.visibility = View.VISIBLE
                }
            } else if (position == itemCount - 1) {
                holder.itemView.setBackgroundResource(R.drawable.dialog_menu_item_bottom)
                holder.mView.visibility = View.GONE
            } else {
                holder.itemView.setBackgroundResource(R.drawable.dialog_menu_item_middle)
                holder.mView.visibility = View.VISIBLE
            }
            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.itemView, position)
            }
        }

        fun setData(data: List<String>) {
            mDataSet = data
            notifyDataSetChanged()
        }

        fun getItem(position: Int): String {
            return mDataSet!![position]
        }


    }

    class ViewHolder(parent: ViewGroup, layoutId: Int) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

        val mTextView: TextView = itemView.findViewById(R.id.tv_dialog_menu_name) as TextView
        val mView: View = itemView.findViewById(R.id.v_dialog_menu_line)

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
         * 选择条目时回调
         */
        fun select(dialog: Dialog, position: Int, text: String)

        /**
         * 点击取消时回调
         */
        fun cancel(dialog: Dialog)
    }
}