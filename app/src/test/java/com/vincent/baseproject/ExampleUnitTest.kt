package com.vincent.baseproject

import android.graphics.Color
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val color = calculateColor(0xffffff,100)
        print(color)
    }

    fun calculateColor(color: Int, alpha: Int): Int {
        if (alpha == 0) {
            return color
        }
        val a = 1 - alpha / 255f
        var red = color shr 16 and 0xff
        var green = color shr 8 and 0xff
        var blue = color and 0xff
        println(red)
        println(green)
        println(blue)
        return Color.argb(alpha,red,green,blue)
    }
}
