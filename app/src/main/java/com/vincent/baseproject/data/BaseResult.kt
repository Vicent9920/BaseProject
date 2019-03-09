package com.vincent.baseproject.data

/**
 * 创建日期：2019/3/9 0009on 下午 3:02
 * 描述：返回实体
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
data class BaseResult<T>(val t:T,val errorCode:Int,val errorMsg:String?)