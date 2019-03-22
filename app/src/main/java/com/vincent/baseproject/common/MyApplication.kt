package com.vincent.baseproject.common

import android.app.Application
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.haoge.easyandroid.EasyAndroid
import com.orhanobut.hawk.Hawk
import com.vincent.baselibrary.util.NetUtils
import me.jessyan.autosize.AutoSizeConfig
import org.json.JSONObject
import org.lzh.framework.updatepluginlib.UpdateConfig
import org.lzh.framework.updatepluginlib.base.FileChecker
import org.lzh.framework.updatepluginlib.base.UpdateChecker
import org.lzh.framework.updatepluginlib.base.UpdateParser
import org.lzh.framework.updatepluginlib.base.UpdateStrategy
import org.lzh.framework.updatepluginlib.model.Update


/**
 * 创建日期：2019/3/6 0006on 下午 3:11
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        EasyAndroid.init(this)

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(this, null)
        // 字体大小忽略设置 只使用设置的默认字体
        AutoSizeConfig.getInstance().isExcludeFontScale = true

        Hawk.init(this).build()
        // 初始化版本升级框架
        initUpdateConfig()
//        // 网络监听
        NetUtils.netWorkListener(this)
    }


    private fun initUpdateConfig() {
        UpdateConfig.getConfig().setUrl("https://raw.githubusercontent.com/yjfnypeu/UpdatePlugin/master/update.json")
            .setUpdateParser(object : UpdateParser() {
                override fun parse(response: String?): Update {
                    val obj = JSONObject(response)
                    val update = Update()
                    update.updateUrl = obj.getString("update_url")
                    update.versionCode = obj.getInt("update_ver_code")
                    update.versionName = obj.getString("update_ver_name")
                    update.updateContent = obj.getString("update_content")
                    update.isForced = false
                    update.isIgnore = obj.getBoolean("ignore_able")
                    return update
                }
            })
            // 更新接口api网络访问任务
            //            .setCheckWorker(object : CheckWorker() {
            //                // 异步查询服务端 apk 版本号
            //                override fun useAsync(): Boolean {
            //                    return false
            //                }
            //
            //                // 异步操作 此处运行于子线程
            //                override fun asyncCheck(entity: CheckEntity?) {
            //                    super.asyncCheck(entity)
            //                    // 此处可以使用自定义Retrofit等方式请求更新接口
            //                    // 当请求失败：需要手动调用onError(Throwable)并传入失败异常
            //                    // 当请求成功：需要手动调用onResponse(String)并传入接口返回原始数据。便于后续解析
            //                }
            //            }::class.java)
            // 更新数据检测器 判断诸如版本号之类决定是否下载apk文件升级
            .setUpdateChecker(object : UpdateChecker() {
                override fun check(update: Update?): Boolean {
                    // 默认判断线上apk版本号大于本地apk以及版本号是否忽略两个条件
                    return true
                }
            })
            // apk下载路径 建议使用原策略
            //            .setFileCreator(object :FileCreator (){
            //                override fun create(update: Update?): File {
            //                    // 下载文件路径
            //                }
            //
            //                override fun createForDaemon(update: Update?): File {
            //                    // 后台下载时文件路径
            //                }
            //
            //            })
            // 检查文件是否可以直接安装
            .setFileChecker(object : FileChecker() {
                override fun onCheckBeforeDownload(): Boolean {
                    // 默认执行下面的操作
                    //若Update.getMd5()为空时。使用PackageManager读取apk的版本信息并与Update.versionCode比对
                    //若Update.getMd5()不为空。则计算apk的md5值并与Update.md5值进行比对。
                    // 当我们在同一个版本号下进行测试，会重复下载同一个版本号的文件，此时需要删除已经下载好的这个文件，否则会直接安装
                    file.delete()
                    return true
                }

                override fun onCheckBeforeInstall() {
                    // 默认执行onCheckBeforeDownload的检查项 有异常则抛出异常，或者调用安装api操作
                    // 我们上面删除了原文件，因此下载的文件是最新的，可以直接安装
                }
            }).updateStrategy = object : UpdateStrategy() {
            override fun isShowDownloadDialog(): Boolean {
                return true
            }

            override fun isAutoInstall(): Boolean {
                return false
            }

            override fun isShowUpdateDialog(update: Update?): Boolean {
                return true
            }
        }
        // 提醒用户升级的Dialog
//            .setCheckNotifier(object : CheckNotifier() {
//                override fun create(context: Activity): Dialog {
//                    return Custom(this as FragmentActivity)
//                        .setContentView(com.vincent.baseproject.R.layout.xupdate_dialog_app)
//                        .setOnClickListener(com.vincent.baseproject.R.id.iv_close,
//                            object : BaseDialog.OnClickListener<View> {
//                                override fun onClick(dialog: Dialog, view: View) {
//                                    sendUserCancel()
//                                    SafeDialogHandle.safeDismissDialog(dialog)
//                                }
//
//                            })
//                        .setOnClickListener(
//                            com.vincent.baseproject.R.id.btn_update,
//                            object : BaseDialog.OnClickListener<View> {
//                                override fun onClick(dialog: Dialog, view: View) {
//                                    sendDownloadRequest()
//                                    SafeDialogHandle.safeDismissDialog(dialog)
//                                }
//
//                            })
//                        .apply {
//                            this.mAnimations = BaseDialog.AnimStyle.SCALE
//                            this.mCancelable = false
//                        }
//                        .show()
//                }
//            })
        // 展示下载进度
//            .setDownloadNotifier(object : DownloadNotifier() {
//                override fun create(update: Update, activity: Activity): DownloadCallback {
//                    val dialog = Custom2(this as FragmentActivity)
//                        .setContentView(com.vincent.baseproject.R.layout.xupdate_dialog_app)
//                        .setVisibility(com.vincent.baseproject.R.id.ll_close, View.GONE)
//                        .setVisibility(com.vincent.baseproject.R.id.btn_update, View.GONE)
//                        .setVisibility(com.vincent.baseproject.R.id.npb_progress, View.VISIBLE)
//                        .apply {
//                            this.mAnimations = BaseDialog.AnimStyle.SCALE
//                            this.mCancelable = false
//                        }
//
//                    return object : DownloadCallback {
//                        override fun onDownloadStart() {
//                            dialog.show()
//                        }
//
//                        override fun onDownloadComplete(file: File?) {
//                            SafeDialogHandle.safeDismissDialog(dialog.mDialog)
//                        }
//
//                        override fun onDownloadError(t: Throwable?) {
//                            SafeDialogHandle.safeDismissDialog(dialog.mDialog)
//                            MessageDialog.Builder(activity as FragmentActivity)
//                                .setTitle("下载apk失败") // 标题可以不用填写
//                                .setMessage("是否重新下载？")
//                                .setConfirm("确定")
//                                .setCancel("取消") // 设置 null 表示不显示取消按钮
//                                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
//                                .apply {
//                                    this.mListener = object : com.vincent.dialoglibrary.MessageDialog.OnListener {
//                                        override fun confirm(dialog: Dialog) {
//                                            restartDownload()
//                                        }
//
//                                        override fun cancel(dialog: Dialog) {
//
//                                        }
//
//
//                                    }
//                                }
//
//                                .show()
//                        }
//
//                        override fun onDownloadProgress(current: Long, total: Long) {
//                            dialog.setProgress((current * 1f / total * 100).toInt())
//                        }
//                    }
//                }
//            })
        // Dialog 使用的FragmentActivity需要到需要的页面才能使用，否则不能生成对话框
//            .setInstallNotifier(object : InstallNotifier() {
//                override fun create(activity: Activity?): Dialog {
//                    val updateContent = String.format(
//                        "版本号：%s\n\n%s",
//                        update.versionName, update.updateContent
//                    )
//                    return MessageDialog.Builder(activity as FragmentActivity)
//                        .setTitle("安装包已就绪，是否安装？") // 标题可以不用填写
//                        .setMessage(updateContent)
//                        .setConfirm("立即安装")
//                        .setCancel("取消") // 设置 null 表示不显示取消按钮
//                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
//                        .apply {
//                            this.mListener = object : com.vincent.dialoglibrary.MessageDialog.OnListener {
//                                override fun confirm(dialog: Dialog) {
//                                    SafeDialogHandle.safeDismissDialog(dialog)
//                                    sendToInstall()
//                                }
//
//                                override fun cancel(dialog: Dialog) {
//                                    sendUserCancel()
//                                    SafeDialogHandle.safeDismissDialog(dialog)
//                                }
//
//
//                            }
//                        }
//
//                        .show()
//                }
//            })


    }


}

