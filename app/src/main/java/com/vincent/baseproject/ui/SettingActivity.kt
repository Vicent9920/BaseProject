package com.vincent.baseproject.ui

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.haoge.easyandroid.easy.EasyLog
import com.haoge.easyandroid.safe.SafeDialogHandle
import com.vincent.baselibrary.base.BaseActivity
import com.vincent.baselibrary.helper.CacheDataManager
import com.vincent.baseproject.R
import com.vincent.baseproject.widget.NumberProgressBar
import com.vincent.dialoglibrary.MessageDialog
import com.vincent.dialoglibrary.base.BaseDialog
import com.vincent.dialoglibrary.base.BaseDialogFragment
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.xupdate_dialog_app.*
import org.lzh.framework.updatepluginlib.UpdateBuilder
import org.lzh.framework.updatepluginlib.base.CheckNotifier
import org.lzh.framework.updatepluginlib.base.DownloadCallback
import org.lzh.framework.updatepluginlib.base.DownloadNotifier
import org.lzh.framework.updatepluginlib.base.InstallNotifier
import org.lzh.framework.updatepluginlib.model.Update
import java.io.File


class SettingActivity : BaseActivity() {
    override fun getLayoutId() = com.vincent.baseproject.R.layout.activity_setting

    override fun initView() {
        tv_title.text = "设置"
        initToolBar(app_toolbar)
    }

    override fun initData() {
        super.initData()
        setting_setBar_cache.setRightText(CacheDataManager.getTotalCacheSize(this))
        setting_setBar_update.setRightText(getVersionName(this))
    }

    override fun initEvent() {
        super.initEvent()

        setting_setBar_cache.setOnClickListener {
            CacheDataManager.clearAllCache(this)
            setting_setBar_cache.setRightText(CacheDataManager.getTotalCacheSize(this))
        }
        setting_setBar_about.setOnClickListener {
            startActivity( Intent(this, WebActivity::class.java)
                .putExtra("url","https://github.com/Vicent9920/BaseProject/blob/master/README.md"))
        }

        setting_setBar_update.setOnClickListener {
            // 提醒用户升级的Dialog
            UpdateBuilder.create().setCheckNotifier(object : CheckNotifier() {
                override fun create(context: Activity): Dialog {
                    return Custom(this@SettingActivity)
                        .setContentView(com.vincent.baseproject.R.layout.xupdate_dialog_app)
                        .setText(R.id.dialog_tv_title,"是否升级到${update.versionName}版本？")
                        .setText(R.id.dialog_tv_updateInfo,update.updateContent)
                        .setOnClickListener(com.vincent.baseproject.R.id.dialog_iv_close,
                            object : BaseDialog.OnClickListener<View> {
                                override fun onClick(dialog: Dialog, view: View) {
                                    sendUserCancel()
                                    SafeDialogHandle.safeDismissDialog(dialog)
                                }

                            })
                        .setOnClickListener(
                            com.vincent.baseproject.R.id.dialog_btn_update,
                            object : BaseDialog.OnClickListener<View> {
                                override fun onClick(dialog: Dialog, view: View) {
                                    sendDownloadRequest()
                                    SafeDialogHandle.safeDismissDialog(dialog)
                                }

                            })
                        .apply {
                            this.mAnimations = BaseDialog.AnimStyle.SCALE
                            this.mCancelable = false
                        }
                        .show()
                }
            })
                .setDownloadNotifier(object : DownloadNotifier() {
                override fun create(update: Update, activity: Activity): DownloadCallback {
                    val dialog = Custom2(this@SettingActivity)
                        .setContentView(com.vincent.baseproject.R.layout.xupdate_dialog_app)
                        .setVisibility(com.vincent.baseproject.R.id.dialog_layout_close, View.GONE)
                        .setVisibility(com.vincent.baseproject.R.id.dialog_btn_update, View.GONE)
                        .setVisibility(com.vincent.baseproject.R.id.dialog_npb_progress, View.VISIBLE)
                        .setText(R.id.dialog_tv_title,"是否升级到${update.versionName}版本？")
                        .setText(R.id.dialog_tv_updateInfo,update.updateContent)
                        .apply {
                            this.mAnimations = BaseDialog.AnimStyle.SCALE
                            this.mCancelable = false
                        }

                    return object : DownloadCallback {
                        override fun onDownloadStart() {
                            dialog.show()
                        }

                        override fun onDownloadComplete(file: File?) {
                            SafeDialogHandle.safeDismissDialog(dialog.mDialog)
                        }

                        override fun onDownloadError(t: Throwable?) {
                            SafeDialogHandle.safeDismissDialog(dialog.mDialog)
                            MessageDialog.Builder(activity as FragmentActivity)
                                .setTitle("下载apk失败") // 标题可以不用填写
                                .setMessage("是否重新下载？")
                                .setConfirm("确定")
                                .setCancel("取消") // 设置 null 表示不显示取消按钮
                                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                                .apply {
                                    this.mListener = object : com.vincent.dialoglibrary.MessageDialog.OnListener {
                                        override fun confirm(dialog: Dialog) {
                                            restartDownload()
                                        }

                                        override fun cancel(dialog: Dialog) {

                                        }


                                    }
                                }

                                .show()
                        }

                        override fun onDownloadProgress(current: Long, total: Long) {
                            dialog.setProgress((current * 1f / total * 100).toInt())
                        }
                    }
                }
            })
                .setInstallNotifier(object : InstallNotifier() {
                override fun create(activity: Activity?): Dialog {
                    val updateContent = String.format(
                        "版本号：%s\n\n%s",
                        update.versionName, update.updateContent
                    )
                    return MessageDialog.Builder(this@SettingActivity)
                        .setTitle("安装包已就绪，是否安装？") // 标题可以不用填写
                        .setMessage(updateContent)
                        .setConfirm("立即安装")
                        .setCancel("取消") // 设置 null 表示不显示取消按钮
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .apply {
                            this.mListener = object : com.vincent.dialoglibrary.MessageDialog.OnListener {
                                override fun confirm(dialog: Dialog) {
                                    SafeDialogHandle.safeDismissDialog(dialog)
                                    sendToInstall()
                                }

                                override fun cancel(dialog: Dialog) {
                                    sendUserCancel()
                                    SafeDialogHandle.safeDismissDialog(dialog)
                                }


                            }
                        }

                        .show()
                }
            })
                .check()
        }
    }



    /**
     * 获取本地Apk版本名称
     * @param context 上下文
     * @return String
     */
    private fun getVersionName(context: Context): String {
        var verName = ""
        try {
            verName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            EasyLog.DEFAULT.e("AppApplicationMgr-->>getVerName()", e.message + "获取本地Apk版本名称失败！")
            e.printStackTrace()
        }

        return verName
    }

}
class Custom(activity: FragmentActivity, themeResId: Int = -1) :
    BaseDialogFragment.Builder<Custom>(activity, themeResId)

class Custom2(activity: FragmentActivity, themeResId: Int = -1) :
    BaseDialogFragment.Builder<Custom2>(activity, themeResId) {
    private var progressBar: NumberProgressBar? = null
    fun setProgress(progress: Int) {
        if (progressBar == null) {
            progressBar = mContentView.findViewById(com.vincent.baseproject.R.id.dialog_npb_progress)
        }
        progressBar?.progress = progress
    }
}