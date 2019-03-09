package com.vincent.baseproject.data

import okhttp3.MultipartBody
import retrofit2.Callback

/**
 * 创建日期：2019/3/9 0009on 下午 3:11
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object LoginNetWork {
    private val services = ServiceCreator.create(Login::class.java)

    /**
     * 登录
     */
    fun login(username:String, password:String,callback:  Callback<BaseResult<Any>>){
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("username", username)
        builder.addFormDataPart("password",password)
        services.login(builder.build()).enqueue(callback)
    }

    /**
     * 注册
     */
    fun register(username:String, password:String,repassword:String,callback:  Callback<BaseResult<Any>>){
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("username", username)
        builder.addFormDataPart("password",password)
        builder.addFormDataPart("password",password)
        services.register(builder.build()).enqueue(callback)
    }
}