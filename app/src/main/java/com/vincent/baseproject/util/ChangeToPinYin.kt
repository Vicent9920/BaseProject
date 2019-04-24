package com.vincent.baseproject.util

import android.util.Log
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination
import net.sourceforge.pinyin4j.PinyinHelper




/**
 * 创建日期：2019/3/26 0026on 上午 10:27
 * 描述：pinyin4j 汉字转拼音工具类
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
object ChangeToPinYin {

    //pinyin4j格式类
    private var format: HanyuPinyinOutputFormat = HanyuPinyinOutputFormat()
    //拼音字符串数组
    private var pinyin: Array<String>? = null


    /**
     * 对单个字进行转换
     * @param pinYinStr 需转换的汉字字符串
     * @return 拼音字符串数组
     * 多音字会返回一个多音字拼音的数组，pinyin4j并不能有效判断该字的读音
     */
    fun getCharPinYin(pinYinStr: Char): String? {
        Log.e("ChangeToPinYin","getCharPinYin:"+pinYinStr)
        try {
            //执行转换
            pinyin = PinyinHelper.toHanyuPinyinStringArray(pinYinStr, format)

        } catch (e: BadHanyuPinyinOutputFormatCombination) {
            e.printStackTrace()
        }
        //pinyin4j规则，当转换的符串不是汉字，就返回null
        pinyin?.let {
            return it[0]
        }?:return null
    }

    /**
     * 对词语进行转换
     * @param pinYinStr
     * @return
     */
    fun getStringPinYin(pinYinStr: String): String {
        format.toneType = HanyuPinyinToneType.WITHOUT_TONE
        val sb = StringBuffer()
        var tempStr: String? = null
        //循环字符串
        for (i in 0 until pinYinStr.length) {

            tempStr = this.getCharPinYin(pinYinStr[i])
            if (tempStr == null) {
                //非汉字直接拼接
                sb.append(pinYinStr[i])
            } else {
                sb.append(tempStr)
            }
        }

        return sb.toString()

    }


}