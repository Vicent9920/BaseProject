package com.vincent.baseproject.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Environment
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.haoge.easyandroid.easy.EasyLog
import com.haoge.easyandroid.easy.EasyPermissions
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.app_toolbar.*


class WebActivity : UIActivity() {
    override fun getLayoutId() = com.vincent.baseproject.R.layout.activity_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        initToolBar(app_toolbar)
        web_wv_view.isVerticalScrollBarEnabled = false
        web_wv_view.isHorizontalScrollBarEnabled = false
        web_wv_view.settings.allowFileAccess = true
        web_wv_view.settings.javaScriptEnabled = true
        web_wv_view.settings.setGeolocationEnabled(true)
        web_wv_view.settings.savePassword = true
        // 支持播放gif动画  解决Android 5.0上Webview默认不允许加载Http与Https混合内容  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        web_wv_view.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        // 加快HTML网页加载完成的速度，等页面finish再加载图片
        web_wv_view.settings.loadsImagesAutomatically = true
        web_wv_view.webViewClient = object : WebViewClient() {
            // 网页加载错误时回调
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                // TODO 待自定义错误Layout
                super.onReceivedError(view, errorCode, description, failingUrl)
            }

            // 网页加载错误时回调
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                // TODO 待自定义错误Layout
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.let {
                    // handler.cancel();// Android默认的处理方式
                    handler.proceed()// 接受所有网站的证书
                    // handleMessage(Message msg);// 进行其他处理
                } ?: super.onReceivedSslError(view, handler, error)

            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                web_pb_progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                web_pb_progress.visibility = View.GONE
            }

            // TODO 处理自定义链接，如 scheme 协议
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }

            // TODO 处理自定义链接，如 scheme 协议
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        web_wv_view.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                title?.let {
                    tv_title.text = it
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                EasyLog.DEFAULT.e(newProgress)
                web_pb_progress.progress = newProgress
                if(newProgress == 100){
                    web_pb_progress.visibility = View.GONE
                }
            }
        }
    }

    override fun initData() {
        super.initData()
        web_wv_view.loadUrl(intent.getStringExtra("url"))
//        //注册下载文件广播
        registerReceiver(
            DownloadCompleteReceiver(),
            IntentFilter().apply { this.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE) })
    }

    override fun initEvent() {
        super.initEvent()
        web_wv_view.setDownloadListener { url, _, contentDisposition, mimetype, _ ->
            EasyPermissions.create( Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).callback {
                if(it){
                    downloadBySystem(
                        url,
                        contentDisposition,
                        mimetype
                    )
                }else{
                    EasyToast.DEFAULT.show("你拒绝了下载文件需要的权限")
                }
            }.request(this)

        }
    }

    override fun onResume() {
        super.onResume()
        web_wv_view.onResume()
        web_wv_view.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        web_wv_view.onPause()
        web_wv_view.pauseTimers()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK && web_wv_view.canGoBack()){
            web_wv_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            // 如果没有注册的话注销会有异常  IllegalArgumentException
            unregisterReceiver(DownloadCompleteReceiver())
        } catch (e: Exception) {
        }
        (web_wv_view.parent as ViewGroup).removeView(web_wv_view)
        web_wv_view.clearHistory()
        web_wv_view.stopLoading()
        //加载一个空白页
        web_wv_view.loadUrl("about:blank")
        web_wv_view.webChromeClient = null
        web_wv_view.webViewClient = null
        //移除WebView所有的View对象
        web_wv_view.removeAllViews()
        //销毁此的WebView的内部状态
        web_wv_view.destroy()
    }

    /**
     * 使用系统的下载服务
     */
    private fun downloadBySystem(url: String, contentDisposition: String, mimeType: String) {
        // 指定下载地址
        val request = DownloadManager.Request(Uri.parse(url))
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner()
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // 设置通知栏的标题，如果不设置，默认使用文件名
        //        request.setTitle("This is title");
        // 设置通知栏的描述
        //        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(true)
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(true)
        // 允许漫游时下载
        request.setAllowedOverRoaming(true)
        // 允许下载的网路类型 默认情况下，所有网络类型都是允许的
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        // 设置下载文件保存的路径和文件名
        val fileName = URLUtil.guessFileName(url, contentDisposition, mimeType)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        //        另外可选一下方法，自定义下载路径
        //        request.setDestinationUri()
        //        request.setDestinationInExternalFilesDir()
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 添加一个下载任务
        val downloadId = downloadManager.enqueue(request)

    }



}

class DownloadCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == it.action) {
                val downloadId = it.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val downloadManager = context?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                var type = downloadManager.getMimeTypeForDownloadedFile(downloadId)
                if (type.isEmpty()) type = "*/*"
                val uri = downloadManager.getUriForDownloadedFile(downloadId)
                context.startActivity(Intent(Intent.ACTION_VIEW).setDataAndType(uri, type))
            }
        }
    }
}