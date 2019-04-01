package com.vincent.baseproject.data

/**
 * 创建日期：2019/3/26 0026on 上午 9:39
 * 描述：源数据实体
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
data class SourceCity(
    val city: List<City>,
    val name: String
)

data class City(
    val area: List<String>,
    val name: String
)