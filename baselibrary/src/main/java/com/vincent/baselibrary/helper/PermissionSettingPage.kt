package com.vincent.baselibrary.helper

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build

/**
 * 创建日期：2019/3/8 0008on 下午 2:27
 * 描述：打开权限设置页面
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object PermissionSettingPage {

     val MARK = Build.MANUFACTURER.toLowerCase()

    fun start(context: Context, newTask: Boolean) {
        var intent: Intent? = null
        if (MARK.contains("huawei")) {
            intent = huawei(context)
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomi(context)
        } else if (MARK.contains("oppo")) {
            intent = oppo(context)
        } else if (MARK.contains("vivo")) {
            intent = vivo(context)
        } else if (MARK.contains("meizu")) {
            intent = meizu(context)
        }

        if (intent == null || !hasIntent(context, intent)) {
            intent = google(context)
        }

        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        } catch (var4: Exception) {
            intent = google(context)
            context.startActivity(intent)
        }

    }
    private fun google(context: Context): Intent {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.fromParts("package", context.packageName, null as String?)
        return intent
    }

    private fun huawei(context: Context): Intent {
        val intent = Intent()
        intent.component = ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
        if (hasIntent(context, intent)) {
            return intent
        } else {
            intent.component =
                ComponentName(
                    "com.huawei.systemmanager",
                    "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity"
                )
            if (hasIntent(context, intent)) {
                return intent
            } else {
                intent.component =
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei.notificationmanager.ui.NotificationManagmentActivity"
                    )
                return intent
            }
        }
    }

    private fun xiaomi(context: Context): Intent {
        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
        intent.putExtra("extra_pkgname", context.packageName)
        if (hasIntent(context, intent)) {
            return intent
        } else {
            intent.setPackage("com.miui.securitycenter")
            if (hasIntent(context, intent)) {
                return intent
            } else {
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                if (hasIntent(context, intent)) {
                    return intent
                } else {
                    intent.setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    )
                    return intent
                }
            }
        }
    }

    private fun oppo(context: Context): Intent {
        val intent = Intent()
        intent.putExtra("packageName", context.packageName)
        intent.setClassName(
            "com.color.safecenter",
            "com.color.safecenter.permission.floatwindow.FloatWindowListActivity"
        )
        if (hasIntent(context, intent)) {
            return intent
        } else {
            intent.setClassName(
                "com.coloros.safecenter",
                "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity"
            )
            if (hasIntent(context, intent)) {
                return intent
            } else {
                intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity")
                return intent
            }
        }
    }

    private fun vivo(context: Context): Intent {
        val intent = Intent()
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager")
        intent.putExtra("packagename", context.packageName)
        if (hasIntent(context, intent)) {
            return intent
        } else {
            intent.component =
                ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity")
            return intent
        }
    }

    private fun meizu(context: Context): Intent {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.putExtra("packageName", context.packageName)
        intent.component = ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity")
        return intent
    }

    @SuppressLint("WrongConstant")
    private fun hasIntent(context: Context, intent: Intent): Boolean {
        return context.packageManager.queryIntentActivities(intent, Intent.FLAG_ACTIVITY_NO_ANIMATION ).size > 0
    }
}