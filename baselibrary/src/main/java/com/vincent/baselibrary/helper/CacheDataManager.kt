package com.vincent.baselibrary.helper

import android.content.Context
import android.os.Environment
import java.nio.file.Files.isDirectory
import android.os.Environment.MEDIA_MOUNTED
import java.io.File
import java.math.BigDecimal


/**
 * 创建日期：2019/3/19 0019on 上午 10:13
 * 描述：应用缓存管理
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object CacheDataManager {
    /**
     * 获取缓存大小
     */
    fun getTotalCacheSize(context: Context): String {
        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 清除缓存
     */
    fun clearAllCache(context: Context) {
        deleteDir(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            deleteDir(context.externalCacheDir)
        }
    }

    /**
     * 删除文件夹
     */
    private fun deleteDir(dir: File?): Boolean {
        dir?:return true
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }

    // 获取文件大小
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    private fun getFolderSize(file: File?): Long {
        file?:return 0
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (i in fileList.indices) {
                // 如果下面还有文件
                size = if (fileList[i].isDirectory) {
                    size + getFolderSize(fileList[i])
                } else {
                    size + fileList[i].length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 格式化单位
     */
    private fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "K"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "M"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}