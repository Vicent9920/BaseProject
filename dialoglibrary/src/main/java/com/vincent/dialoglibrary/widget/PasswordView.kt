package com.vincent.dialoglibrary.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * <p>文件描述：密码遮挡自定义 View<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/3/3 0003 <p>
 * <p>@update 2019/3/3 0003<p>
 * <p>版本号：1<p>
 *
 */
// 密码总个数
val PASSWORD_COUNT = 6
class PasswordView : View {



    // 已经输入的密码个数，也就是需要显示的小黑点个数
    private var mCurrentIndex = 0

    private lateinit var mPaint: Paint
    private lateinit var mPath: Path
    private lateinit var mPointPaint: Paint

    //密码框边界线的颜色值
    private val mStrokeColor = -0x131314

    //单个密码框的高度
    private var mItemWidth = 44

    private var mItemHeight = 41

    //中心黑点的半径大小
    private val mPointRadius = 15

    //中心黑点的颜色
    private val mPointColor = -0x99999a

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        mItemWidth = dp2px(mItemWidth.toFloat())
        mItemHeight = dp2px(mItemHeight.toFloat())

        mPaint = Paint()
        //设置抗锯齿
        mPaint.isAntiAlias = true
        //设置颜色
        mPaint.color = mStrokeColor
        //设置描边
        mPaint.style = Paint.Style.STROKE

        mPath = Path()
        mPath.moveTo(0f, 0f)
        mPath.lineTo(mItemWidth * PASSWORD_COUNT * 1f, 0f)
        mPath.lineTo(mItemWidth * PASSWORD_COUNT * 1f, mItemHeight * 1f)
        mPath.lineTo(0f, mItemHeight * 1f)
        mPath.close()

        mPointPaint = Paint()
        mPointPaint.isAntiAlias = true
        mPointPaint.style = Paint.Style.FILL
        mPointPaint.color = mPointColor
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)

        return if (mode == View.MeasureSpec.EXACTLY) {
            size
        } else {
            mItemWidth * PASSWORD_COUNT
        }
    }

    private fun measureHeight(measureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)

        return if (mode == View.MeasureSpec.EXACTLY) {
            size
        } else {
            mItemHeight
        }
    }

    protected override fun onDraw(canvas: Canvas) {
        mPaint.setStrokeWidth(5f)
        canvas.drawPath(mPath, mPaint)

        drawDivide(canvas)
        drawBlackPoint(canvas)
    }

    /**
     * 画单个的分割线
     */
    private fun drawDivide(canvas: Canvas) {
        mPaint.setStrokeWidth(3f)
        for (index in 1 until PASSWORD_COUNT) {
            canvas.drawLine(mItemWidth * 1f * index, 0f, mItemWidth * index * 1f, mItemHeight * 1f, mPaint)
        }
    }

    /**
     * 绘制中间的小黑点
     */
    private fun drawBlackPoint(canvas: Canvas) {
        if (mCurrentIndex === 0) {
            return
        }
        for (i in 1..mCurrentIndex) {
            canvas.drawCircle(
                i * mItemWidth * 1f - mItemWidth / 2 * 1f,
                mItemHeight * 1f / 2,
                mPointRadius * 1f,
                mPointPaint
            )
        }
    }

    /**
     * 改变密码提示小黑点的个数
     */
    fun setPassWord(index: Int) {
        mCurrentIndex = index
        invalidate()
    }

    fun dp2px(dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}