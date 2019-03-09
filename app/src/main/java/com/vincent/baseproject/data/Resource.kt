package com.vincent.baseproject.data

/**
 * 创建日期：2019/3/9 0009on 下午 3:37
 * 描述：liveData回调对象
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
data class Resource<T>(val status: Int, val data: T?, val message: String?) {
    companion object {
        const val SUCCESS = 0
        const val ERROR = 1
        const val LOADING = 2

        fun <T> success(data: T?) = Resource(SUCCESS, data, null)

        fun <T> error(msg: String, data: T?) = Resource(ERROR, data, msg)

        fun <T> loading(data: T?) = Resource(LOADING, data, null)
    }
}