package com.vincent.baseproject.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import com.google.gson.Gson
import com.google.zxing.Result
import com.google.zxing.client.result.*
import com.haoge.easyandroid.easy.EasyPermissions
import com.haoge.easyandroid.easy.EasyToast
import com.mylhyl.zxing.scanner.OnScannerCompletionListener
import com.mylhyl.zxing.scanner.ScannerOptions
import com.mylhyl.zxing.scanner.decode.QRDecode
import com.vincent.baselibrary.base.BaseActivity
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.activity_scanner.*
private const val REQ_PHOTO = 1001
class ScannerActivity : UIActivity(), OnScannerCompletionListener {


    private var mode = true
    private var mLastResult: Result? = null
    override fun getLayoutId() = R.layout.activity_scanner

    override fun initView() {
        scanner_toolbar.setNavigationIcon(R.mipmap.back)
        initToolBar(scanner_toolbar)
        scanner_view.setOnScannerCompletionListener(this)

        val builder = ScannerOptions.Builder()
        //设置扫描成功的声音
        builder.setMediaResId(R.raw.weixin_beep)
        // 设置提醒文本
        builder.setTipText("将二维码放入框内")
        // 设置提醒文本颜色
        builder.setTipTextColor(Color.parseColor("#999999"))
        // 设置扫描动画样式及资源文件
        builder.setLaserStyle(ScannerOptions.LaserStyle.RES_GRID,R.mipmap.zfb_grid_scan_line)
        // 设置扫模框四角的颜色
//        builder.setFrameCornerColor(Color.parseColor("#CCCCCC"))
        scanner_view.setScannerOptions(builder.build())
        scan_iv_flash.isSelected = !mode
    }

    override fun initEvent() {
        super.initEvent()
        scan_iv_flash.setOnClickListener {
            scanner_view.toggleLight(mode)
            scan_iv_flash.isSelected = mode
            mode = !mode
        }
        scan_tv_pictures.setOnClickListener {
            EasyPermissions.create(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).callback {
                if(it){
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQ_PHOTO)
                }else{
                    EasyToast.DEFAULT.show("你拒绝了存储权限，无法打开相册")
                }
            }.request(this)

        }
    }

    override fun onResume() {
        super.onResume()
        scanner_view.onResume()
    }

    override fun onPause() {
        super.onPause()
        scanner_view.onPause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> if (mLastResult != null) {
                scanner_view.restartPreviewAfterDelay(0)
                mLastResult = null
                return true
            }
        }
        return super.onKeyDown(keyCode, event)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                selectedImage?.let {
                    val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
                    val c = contentResolver.query(selectedImage, filePathColumns, null, null, null)
                    c?.moveToFirst()
                    val columnIndex = c?.getColumnIndex(filePathColumns[0])
                    val imagePath = c?.getString(columnIndex?:0)
                    c?.close()
                    QRDecode.decodeQR(imagePath, this)
                }

            }
        }
    }

    override fun onScannerCompletion(rawResult: Result?, parsedResult: ParsedResult?, barcode: Bitmap?) {
        rawResult?.let {
            parsedResult ?: return@let
            when (parsedResult.type) {
                ParsedResultType.ISBN -> {
                    val info = (parsedResult as ISBNParsedResult).isbn
                    easyStart(Intent(this,WebActivity::class.java).putExtra("url","$INFO$info"))
                }
                ParsedResultType.TEXT -> {
                    val info = (parsedResult as TextParsedResult).text
                    easyStart(Intent(this,WebActivity::class.java).putExtra("url","$INFO$info"))
                }
                ParsedResultType.URI -> {
                    val result = parsedResult as URIParsedResult
                    easyStart(Intent(this,WebActivity::class.java).putExtra("url",result.uri))
                }
                // ParsedResultType.ADDRESSBOOK, ParsedResultType.PRODUCT, ParsedResultType.TEL, ParsedResultType.SMS 等
                else -> {
                    val info = Gson().toJson(parsedResult)
                    easyStart(Intent(this,WebActivity::class.java).putExtra("url","$INFO$info"))
                }

            }

            finish()
        }

    }
}
