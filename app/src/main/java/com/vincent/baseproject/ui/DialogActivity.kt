package com.vincent.baseproject.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.base.BaseDialog
import com.vincent.baselibrary.base.BaseDialogFragment
import com.vincent.baseproject.R
import com.vincent.dialoglibrary.*
import kotlinx.android.synthetic.main.activity_dialog.*
import java.util.*

class DialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        setSupportActionBar(toolbar)
        title = "对话框案例"
        initEvent()
    }

    private fun initEvent() {
        btn_dialog_message.setOnClickListener {
            MessageDialog.Builder(this)
                .setTitle("我是标题") // 标题可以不用填写
                .setMessage("我是内容")
                .setConfirm("确定")
                .setCancel("取消") // 设置 null 表示不显示取消按钮
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .apply {
                    this.mListener = object : com.vincent.dialoglibrary.MessageDialog.OnListener {
                        override fun confirm(dialog: Dialog) {
                            EasyToast.DEFAULT.show("确定了")
                        }

                        override fun cancel(dialog: Dialog) {
                            EasyToast.DEFAULT.show("取消了")
                        }


                    }
                }

                .show()
        }
        btn_dialog_bottom_menu.setOnClickListener {
            val data = ArrayList<String>()
            for (i in 0..9) {
                data.add("我是数据$i")
            }
            MenuDialog.Builder(this)
                .setCancel("取消") // 设置 null 表示不显示取消按钮
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setList(data)
                .setGravity(Gravity.BOTTOM)
                .apply {
                    mListener = object : MenuDialog.OnListener {
                        override fun select(dialog: Dialog, position: Int, text: String) {
                            EasyToast.DEFAULT.show("位置：$position，文本：$text")
                        }

                        override fun cancel(dialog: Dialog) {
                            EasyToast.DEFAULT.show("取消了")
                        }


                    }
                    mAnimations = BaseDialog.AnimStyle.BOTTOM
                }
                .show()
        }
        btn_dialog_center_menu.setOnClickListener {
            val data = ArrayList<String>()
            for (i in 0..9) {
                data.add("我是数据$i")
            }
            MenuDialog.Builder(this)
                .setCancel(null) // 设置 null 表示不显示取消按钮
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setList(data)
                .setGravity(Gravity.CENTER)
                .apply {
                    mListener = object : MenuDialog.OnListener {
                        override fun select(dialog: Dialog, position: Int, text: String) {
                            EasyToast.DEFAULT.show("位置：$position，文本：$text")
                        }

                        override fun cancel(dialog: Dialog) {
                            EasyToast.DEFAULT.show("取消了")
                        }


                    }
                    mAnimations = BaseDialog.AnimStyle.SCALE
                }
                .show()
        }
        btn_dialog_succeed_toast.setOnClickListener {
            ToastDialog.Builder(this)
                .setType(ToastDialog.Type.FINISH)
                .setMessage("完成")
                .show()
        }
        btn_dialog_fail_toast.setOnClickListener {
            ToastDialog.Builder(this)
                .setType(ToastDialog.Type.ERROR)
                .setMessage("错误")
                .show()
        }
        btn_dialog_warn_toast.setOnClickListener {
            ToastDialog.Builder(this)
                .setType(ToastDialog.Type.WARN)
                .setMessage("警告")
                .show()
        }
        btn_dialog_wait.setOnClickListener {
            val dialog = WaitDialog.Builder(this)
                .setMessage("加载中...") // 消息文本可以不用填写
                .show()
            Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 3000)
        }
        btn_dialog_pay.setOnClickListener {
            PayPasswordDialog.Builder(this)
                .setTitle("请输入支付密码")
                .setSubTitle("用于购买一个女盆友")
                .setMoney("￥ 100.00")
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .apply {
                    this.mListener = object : PayPasswordDialog.OnListener {
                        override fun complete(dialog: Dialog, password: String) {
                            EasyToast.DEFAULT.show(password)
                        }

                        override fun cancel(dialog: Dialog) {
                            EasyToast.DEFAULT.show("取消了")
                        }
                    }
                }
                .show()
        }
        btn_dialog_custom.setOnClickListener {
//                        val b = BaseDialogFragment.Builder<BaseDialogFragment.Builder<Int>>(this)
//
//            }
//            BaseDialogFragment.Builder(activity = this)
//                .setContentView(R.layout.dialog_custom)
//                .setAnimStyle(BaseDialog.AnimStyle.SCALE)
//                //.setText(id, "我是预设置的文本")
//                .setOnClickListener(R.id.btn_dialog_custom_ok, object : BaseDialog.OnClickListener<ImageView> {
//                    override fun onClick(dialog: Dialog, view: ImageView) {
//                       dialog.dismiss()
//                    }
//                })
//                .show()
        }
    }
}
