package com.vincent.baseproject.data

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 创建日期：2019/3/9 0009on 下午 3:32
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class LoginRepository private constructor() {
    val login = MutableLiveData<Resource<String>>()
    val register = MutableLiveData<Resource<String>>()
    fun login(username: String, password: String) {

        login.value = Resource.loading(null)
        LoginNetWork.login(username, password, object : Callback<BaseResult<Any>> {
            override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                login.postValue(Resource.error("登录失败", null))
            }

            override fun onResponse(call: Call<BaseResult<Any>>, response: Response<BaseResult<Any>>) {
                val body = response.body()
                if (body?.errorCode == 0) {
                    login.postValue(Resource.success("登录成功"))
                } else {
                    login.postValue(Resource.error("登录失败", body?.errorMsg))
                }
            }
        })

    }

    fun register(username: String, password: String, rePassword: String) {

        register.value = Resource.loading(null)
        LoginNetWork.register(username, password, rePassword, object : Callback<BaseResult<Any>> {
            override fun onFailure(call: Call<BaseResult<Any>>, t: Throwable) {
                register.postValue(Resource.error("登录失败", null))
            }

            override fun onResponse(call: Call<BaseResult<Any>>, response: Response<BaseResult<Any>>) {
                val body = response.body()
                if (body?.errorCode == 0) {
                    register.postValue(Resource.success("登录成功"))
                } else {
                    register.postValue(Resource.error("登录失败", body?.errorMsg))
                }
            }
        })
    }

    companion object {
        private var instance: LoginRepository? = null
        fun getInstance(): LoginRepository {
            if (instance == null) {
                synchronized(LoginRepository::class.java) {
                    if (instance == null) {
                        instance = LoginRepository()
                    }
                }
            }
            return instance!!
        }
    }
}