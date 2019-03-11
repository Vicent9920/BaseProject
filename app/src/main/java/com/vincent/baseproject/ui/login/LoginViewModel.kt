package com.vincent.baseproject.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.vincent.baseproject.data.LoginRepository
import com.vincent.baseproject.data.Resource

/**
 * 创建日期：2019/3/9 0009on 下午 3:54
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class LoginViewModel(private val repository:LoginRepository): ViewModel() {
    val loginState = Transformations.map(repository.login) { it }!!
    val registerState = Transformations.map(repository.register) { it }!!
    fun logun(username:String,password:String) = repository.login(username,password)
    fun register(username:String,password:String,rePassword:String) =  repository.register(username, password, rePassword)
}