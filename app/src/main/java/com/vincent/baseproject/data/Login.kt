package com.vincent.baseproject.data

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 创建日期：2019/3/9 0009on 下午 2:55
 * 描述：登录模块api
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
interface Login {
    @POST("user/login")
    fun login(@Body body: RequestBody): Call<BaseResult<Any>>

    @POST("user/register")
    fun register(@Body body: RequestBody): Call<BaseResult<Any>>
}