@file:Suppress("unused")
package com.vincent.baselibrary.util

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat


object DrawableUtil {
    /**
     * 设置drawable宽高
     *
     * @param drawable
     * @param width
     * @param height
     */
    fun setDrawableWidthHeight(drawable: Drawable?, width: Int, height: Int) {
        drawable?.setBounds(
            0, 0,
            if (width >= 0) width else drawable.intrinsicWidth,
            if (height >= 0) height else drawable.intrinsicHeight
        )
    }

    /**
     * 复制当前drawable
     *
     * @param drawable
     * @return
     */
    fun getNewDrawable(drawable: Drawable?): Drawable? {
        return drawable?.constantState?.newDrawable()
    }


    /**
     * 给一个Drawable变换线框颜色
     *
     * @param drawable 需要变换颜色的drawable
     * @param color    需要变换的颜色
     * @return
     */
    fun setTintDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        drawable?.let {
            DrawableCompat.setTint(it, color)
        }
        return drawable
    }

    fun setTintDrawable(drawable: Drawable?, tint: ColorStateList?): Drawable? {

        if (drawable != null && tint != null) {
            DrawableCompat.setTintList(drawable, tint)
        }
        return drawable
    }

    fun setTintMode(drawable: Drawable?, tintMode: PorterDuff.Mode?): Drawable? {
        if (drawable != null && tintMode != null) {
            DrawableCompat.setTintMode(drawable, tintMode)
        }
        return drawable
    }
}