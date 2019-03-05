package com.vincent.baselibrary.base

import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction


/**
 * <p>文件描述：DialogFragment基类
 * <p>封装到 baseLibrary 是因为需要资源文件
 * <p>@author 烤鱼<p>
 * <p>@date 2019/2/26 0026 <p>
 * <p>@update 2019/2/26 0026<p>
 * <p>版本号：1<p>
 *
 */
class BaseDialogFragment : DialogFragment() {
    private lateinit var mDialog: BaseDialog

    private lateinit var sShowTag: String
    private var sLastTime: Long = 0

    /**
     * 父类同名方法简化
     */
    fun show(fragment: Fragment) {
        show(fragment.getFragmentManager(), fragment::class.java.getName())
    }

    /**
     * 父类同名方法简化
     */
    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, activity.javaClass.name)
    }



    override fun show(transaction: FragmentTransaction, tag: String): Int {
        return if (!isRepeatedShow(tag)) {
            super.show(transaction, tag)
        } else -1
    }

    /**
     * 根据 tag 判断这个 Dialog 是否重复显示了
     *
     * @param tag           Tag标记
     */
    protected fun isRepeatedShow(tag: String): Boolean {
        val result = tag == sShowTag && SystemClock.uptimeMillis() - sLastTime < 500
        sShowTag = tag
        sLastTime = SystemClock.uptimeMillis()
        return result
    }

    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        mDialog?.let {
            return mDialog
        } ?: return BaseDialog(context, -1)

    }

    open class Builder<B : BaseDialog.Builder<B>> (val activity: FragmentActivity, themeResId: Int = -1) :
        BaseDialog.Builder<B>(activity, themeResId) {

        private lateinit var mDialogFragment: BaseDialogFragment

        /**
         * 获取 Fragment 的标记
         */
        protected fun getFragmentTag(): String {
            return javaClass.name
        }

        // 重写父类的方法（仅供子类调用）
        override fun dismiss() {
            mDialogFragment.dismiss()
        }

        override fun show(): BaseDialog {
            val dialog = create()
            mDialogFragment = BaseDialogFragment()
            mDialogFragment.mDialog = dialog
            mDialogFragment.show(activity.getSupportFragmentManager(), getFragmentTag())
            // 解决 Dialog 设置了而 DialogFragment 没有生效的问题
            mDialogFragment.isCancelable = mCancelable
            return dialog
        }
    }
}