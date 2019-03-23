package com.vincent.baselibrary.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import com.haoge.easyandroid.easy.EasyLog
import com.vincent.baselibrary.dao.NetworkChangeEvent
import org.greenrobot.eventbus.EventBus


/**
 * 创建日期：2019/3/22 0022on 下午 2:01
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object NetUtils {
    //未找到合适匹配网络类型
    const val TYPE_NO = 0
    //中国移动CMNET网络(中国移动GPRS接入方式之一, 主要为PC、笔记本电脑、PDA设立)
    const val TYPE_MOBILE_CMNET = 1

    //中国移动CMWAP网络(中国移动GPRS接入方式之一,主要为手机WAP上网而设立)
    const val TYPE_MOBILE_CMWAP = 2

    //中国联通UNIWAP网络(中国联通划分GPRS接入方式之一, 主要为手机WAP上网而设立)
    const val TYPE_MOBILE_UNIWAP = 3

    //中国联通3GWAP网络
    const val TYPE_MOBILE_3GWAP = 4

    //中国联通3HNET网络
    const val TYPE_MOBLIE_3GNET = 5

    //中国联通UNINET网络(中国联通划分GPRS接入方式之一, 主要为PC、笔记本电脑、PDA设立)
    const val TYPE_MOBILE_UNINET = 6

    //WIFI网络
    const val TYPE_WIFI = 10


    var isConnected = true
    /**
     * 检查网络是否连接
     */
    @SuppressLint("MissingPermission")
    fun isNetworkConnected(context: Context): Boolean {
        val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        isConnected = mNetworkInfo?.isConnected ?: false
        return isConnected
    }

    /**
     * 获取网络类型
     * 推荐文章 https://yq.aliyun.com/articles/341803#
     */
    @Deprecated("在Android6.0中开发者不应该关注传输网络类型，如需指定网络传输类型，应通过其它api：https://www.jianshu.com/p/8b6b48c61120")
    @SuppressLint("MissingPermission")
    fun getNetworkState(context: Context): Int {
        //获取ConnectivityManager对象
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获得当前网络信息
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isAvailable) {
            if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.extraInfo == "cmnet") {
                    return TYPE_MOBILE_CMNET
                }
                if (networkInfo.extraInfo == "cmwap") {
                    return TYPE_MOBILE_CMWAP
                }
                if (networkInfo.extraInfo == "uniwap") {
                    return TYPE_MOBILE_UNIWAP
                }
                if (networkInfo.extraInfo == "3gwap") {
                    return TYPE_MOBILE_3GWAP
                }
                if (networkInfo.extraInfo == "3gnet") {
                    return TYPE_MOBLIE_3GNET
                }
                if (networkInfo.extraInfo == "uninet") {
                    return TYPE_MOBILE_UNINET
                }
                if (networkInfo.extraInfo == "ctwap") {
                    return TYPE_MOBILE_UNINET
                }
                if (networkInfo.extraInfo == "ctnet") {
                    return TYPE_MOBILE_UNINET
                }
            } else {
                if (networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    return TYPE_WIFI
                }
            }
        }
        return TYPE_NO
    }

    /**
     * 获取网络状态事件监听
     * https://blog.csdn.net/u011315960/article/details/79006162
     * 测试过程中没有添加 WRITE_SETTINGS 权限在报错在 Vivo 和华为P9 测试没有问题
     */
    fun netWorkListener(context: Context) {
        isConnected = isNetworkConnected(context)
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val req = NetworkRequest.Builder()
        // 蜂窝煤网络
        req.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        // WiFi
        req.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        // 蓝牙
        req.addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 字面意思理解应该是热点
            req.addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
        }
        connectivityManager.requestNetwork(req.build(), object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                isConnected = true
                EasyLog.DEFAULT.e(true)
                EventBus.getDefault().post(NetworkChangeEvent(true))
            }

            override fun onUnavailable() {
                super.onUnavailable()
                isConnected = false
                EventBus.getDefault().post(NetworkChangeEvent(false))
                EasyLog.DEFAULT.e("onUnavailable")
            }

            override fun onLost(network: Network?) {
                super.onLost(network)
                isConnected = false
                EasyLog.DEFAULT.e(network)
                EventBus.getDefault().post(NetworkChangeEvent(false))
            }
        })
    }
}