package com.vincent.baseproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vincent.baseproject.data.LoginRepository

/**
 * 创建日期：2019/3/9 0009on 下午 3:48
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class LoginModelFactory(private val repository:LoginRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return super.create(modelClass)
    }
}