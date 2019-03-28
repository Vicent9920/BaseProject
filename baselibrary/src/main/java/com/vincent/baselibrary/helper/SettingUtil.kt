package com.vincent.baselibrary.helper

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build


object SettingUtil {


    /**
     * 跳转到系统设置
     * 小米10使用小米专用方法会崩溃，因此一并使用默认方法
     */
    fun goSetting(context: Context) {

        val manufacturer = Build.MANUFACTURER.toLowerCase()
        when {
            manufacturer.contains("huawei") -> goHuaweiPermission(context)
            manufacturer.contains("meizu") -> goMeizuPermission(context)
            else -> goDefaultSetting(context)
        }



    }

    private fun goDefaultSetting(context: Context) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", context.packageName, null)
        context.startActivity(intent)
    }


    /**
     * 跳转到魅族的权限管理系统
     */
    private fun goMeizuPermission(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            goDefaultSetting(context)
            e.printStackTrace()

        }

    }

    /**
     * 华为的权限管理页面
     */
    private fun goHuaweiPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")//华为权限管理
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            goDefaultSetting(context)
            e.printStackTrace()
        }

    }


}