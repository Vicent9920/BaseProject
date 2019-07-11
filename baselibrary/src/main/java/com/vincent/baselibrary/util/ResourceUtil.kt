@file:Suppress("unused")
package com.vincent.baselibrary.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat


object ResourceUtil {

    fun getText(context: Context, res: Int): CharSequence? {
        var txt: CharSequence? = null
        try {
            txt = context.getText(res)
        } catch (e: Exception) {

        }

        return txt
    }

    fun getTextArray(context: Context, res: Int): Array<CharSequence?> {
        var result = arrayOfNulls<CharSequence>(0)
        try {
            result = context.resources.getTextArray(res)
        } catch (e: Exception) {
        }

        return result
    }

    fun getDrawable(context: Context, res: Int): Drawable? {
        var drawable: Drawable? = null
        try {
            drawable = ContextCompat.getDrawable(context, res)
        } catch (e: Exception) {

        }

        return drawable
    }

    fun getColor(context: Context, res: Int): Int {
        var result = 0
        try {
            result = ContextCompat.getColor(context, res)
        } catch (e: Exception) {
        }

        return result
    }

    fun getColorStateList(context: Context, res: Int): ColorStateList? {
        var color: ColorStateList? = null
        try {
            color = ContextCompat.getColorStateList(context, res)
        } catch (e: Exception) {

        }

        return color
    }

    fun getDimension(context: Context, res: Int): Float {
        var result = 0f
        try {
            result = context.resources.getDimension(res)
        } catch (e: Exception) {
        }

        return result
    }

    fun getDimensionPixelSize(context: Context, res: Int): Int {
        var result = 0
        try {
            result = context.resources.getDimensionPixelSize(res)
        } catch (e: Exception) {
        }

        return result
    }

    fun getStringArray(context: Context, res: Int): Array<String?> {
        var result = arrayOfNulls<String>(0)
        try {
            result = context.resources.getStringArray(res)
        } catch (e: Exception) {
        }

        return result
    }

    fun getAttrColor(context: Context, attrRes: Int): Int {
        var result = 0
        try {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attrRes, typedValue, true)
            result = typedValue.data
        } catch (e: Exception) {

        }

        return result
    }

    fun getAttrFloat(context: Context, attrRes: Int): Float {
        return getAttrFloat(context, attrRes, 1.0f)
    }

    fun getAttrFloat(context: Context, attrRes: Int, def: Float): Float {
        var result = def
        try {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(attrRes, typedValue, true)
            result = typedValue.float
        } catch (e: Exception) {

        }

        return if (result == 0f) def else result
    }
}