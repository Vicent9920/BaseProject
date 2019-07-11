@file:SuppressLint("CustomViewStyleable")

package com.vincent.baselibrary.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import com.vincent.baselibrary.R
import kotlin.math.max
import kotlin.math.min


/**
 * 创建日期：2019/3/7 0007on 下午 3:21
 * 描述：设置圆角/圆环效果
 * author：Vincent
 * QQ：3332168769
 * 备注：
 */
class RoundImageView(context: Context, val attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    //圆角大小，默认为10
    var mBorderRadius = 6f
    // 圆环半径 默认为1
    var mBoederWidth = 0f
    var mBorderColor: Int = Color.GRAY

    private var mPaint: Paint = Paint()

    // 3x3 矩阵，主要用于缩小放大
    private var mMatrix: Matrix

    //渲染图像，使用图像为绘制图形着色
    private var mBitmapShader: BitmapShader? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)


    init {
        mPaint.isAntiAlias = true
        mMatrix = Matrix()
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleDrawable, defStyleAttr, 0)
        mBorderRadius = typedArray.getFloat(R.styleable.CircleDrawable_circle_radius, 6f)
        mBoederWidth = typedArray.getFloat(R.styleable.CircleDrawable_circle_border, 0f)
        mBorderColor = typedArray.getColor(R.styleable.CircleDrawable_circle_color, Color.GRAY)
        typedArray.recycle()
        mBorderRadius = dipToPixels(context, mBorderRadius)
        if (mBoederWidth != 0f) {
            mBoederWidth = dipToPixels(context, mBoederWidth)
        }
        super.setScaleType(ScaleType.CENTER_CROP)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        drawable ?: return
        val bitmap = drawableToBitmap(drawable)
        mBitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        var scale = 1.0f
        if (!(bitmap.width == width && bitmap.height == height)) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = max(width * 1.0f / bitmap.width, height * 1.0f / bitmap.height)
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale)
        // 设置变换矩阵
        mBitmapShader?.setLocalMatrix(mMatrix)
        // 设置shader
        mPaint.shader = mBitmapShader
        if (mBoederWidth == 0f) {
            canvas?.drawRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()), mBorderRadius, mBorderRadius,
                mPaint
            )
        } else {
            canvas?.drawRoundRect(
                RectF(mBoederWidth, mBoederWidth, width.toFloat() - mBoederWidth, height.toFloat() - mBoederWidth),
                mBorderRadius - mBoederWidth,
                mBorderRadius - mBoederWidth,
                mPaint
            )
            val mBorderRect = calculateBounds()
            val p = Paint()
            p.isAntiAlias = true
            p.style = Paint.Style.STROKE
            p.strokeWidth = mBoederWidth
            p.color = mBorderColor
            val bgRadius =
                min((mBorderRect.height() - mBoederWidth) / 2.0f, (mBorderRect.width() - mBoederWidth) / 2.0f)
            canvas?.drawCircle(mBorderRect.centerX(), mBorderRect.centerY(), bgRadius, p)
        }


    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        val w = if (drawable.intrinsicWidth <= 0) width else drawable.intrinsicWidth
        val h = if (drawable.intrinsicHeight <= 0) height else drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }

    private fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics) + 0.5f
    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = min(availableWidth, availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }

}