package com.vincent.baseproject.ui.home.fragment


import android.Manifest
import android.app.Activity
import android.app.Dialog
import androidx.fragment.app.Fragment
import com.haoge.easyandroid.easy.EasyPermissions
import com.haoge.easyandroid.easy.PermissionAlwaysDenyNotifier
import com.jaeger.library.StatusBarUtil
import com.orhanobut.hawk.Hawk
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baselibrary.helper.SettingUtil
import com.vincent.baseproject.R
import com.vincent.baseproject.common.Contsant
import com.vincent.dialoglibrary.MessageDialog
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.home_fragment_c.*

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragmentC : BaseLazyFragment() {
    override fun getLayoutId() = R.layout.home_fragment_c

    override fun initView() {
        tv_title.text = "消息"
       initToolBar(app_toolbar)
    }

    override fun initEvent() {
        super.initEvent()
        homeC_btn_showToast.setOnClickListener {
           toast("我是普通的吐司")
        }
        homeC_btn_permission.setOnClickListener {
            permissionDemo()

        }
        homeC_btn_stateBlack.setOnClickListener {
            StatusBarUtil.setLightMode(mActivity)
        }
        homeC_btn_stateWhite.setOnClickListener {
            StatusBarUtil.setDarkMode(mActivity)
        }
        homeC_btn_swipeEnabled.setOnClickListener {
            Hawk.put(Contsant.SWIPE_ENABLED,true)
        }
        homeC_btn_swipeDisable.setOnClickListener {
            Hawk.put(Contsant.SWIPE_ENABLED,false)
        }
    }

    /**
     * 申请权限
     */
    private fun permissionDemo() {
        EasyPermissions.create(// 指定待申请权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.WRITE_CONTACTS
        )
            // 定制权限申请说明弹窗
            .rational { permission, chain ->
                MessageDialog.Builder(mActivity)
                    .setTitle("权限申请说明")
                    .setMessage("应用需要此权限：\n$permission")
                    .setConfirm("同意")
                    .setCancel("拒绝")
                    //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                    .apply {
                        this.mListener = object : MessageDialog.OnListener {
                            override fun confirm(dialog: Dialog) {
                                chain.process()
                            }

                            override fun cancel(dialog: Dialog) {
                                chain.cancel()
                            }
                        }
                    }
                    .show()
                return@rational true
            }
            // 设置授权结果回调
            .callback { grant ->
                toast("权限申请${if (grant) "成功" else "失败"}")
            }
            // 当权限被默认拒绝时。调起弹窗提醒需要用户去主动开启权限
            .alwaysDenyNotifier(object : PermissionAlwaysDenyNotifier() {
                override fun onAlwaysDeny(permissions: Array<String>, activity: Activity) {
                    val message = StringBuilder("以下部分权限已被默认拒绝，请前往设置页将其打开:\n\n")
                    EasyPermissions.getPermissionGroupInfos(permissions, activity).forEach {
                        message.append("${it.label} : ${it.desc} \n")
                    }
                    MessageDialog.Builder(mActivity)
                        .setTitle("权限申请提醒") // 标题可以不用填写
                        .setMessage(message)
                        .setConfirm("确定")
                        .setCancel("取消") // 设置 null 表示不显示取消按钮
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .apply {
                            this.mListener = object : MessageDialog.OnListener {
                                override fun confirm(dialog: Dialog) {
                                    //打开设置页面
                                    SettingUtil.goSetting(mActivity)
                                }

                                override fun cancel(dialog: Dialog) {
                                    toast("坚决不肯同意应用权限，建议关闭这个APP")
                                }
                            }
                        }
                        .show()
                }

            })
            // 发起请求
            .request(mActivity)
    }
}
