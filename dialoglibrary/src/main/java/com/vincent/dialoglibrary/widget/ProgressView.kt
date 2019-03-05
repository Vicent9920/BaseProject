package com.vincent.dialoglibrary.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.util.TypedValue
import android.view.animation.AnimationUtils


/**
 * <p>文件描述：<p>
 * <p>@author 烤鱼<p>
 * <p>@date 2019/3/4 0004 <p>
 * <p>@update 2019/3/4 0004<p>
 * <p>版本号：1<p>
 *
 */
const val MODE_DETERMINATE = 0
const val MODE_INDETERMINATE = 1

class ProgressView : View {

    var isStart = false
    var isAutoStart = true
//    lateinit var circularProgressDrawable: CircularProgressDrawable

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        applyStyle(context)
    }

    fun applyStyle(context: Context) {
//        circularProgressDrawable = CircularProgressDrawable.Builder(context, R.style.CircularProgress).build()
    }

    companion object {
        const val PROGRESS_STATE_HIDE = -1
        const val PROGRESS_STATE_STRETCH = 0
        const val PROGRESS_STATE_KEEP_STRETCH = 1
        const val PROGRESS_STATE_SHRINK = 2
        const val PROGRESS_STATE_KEEP_SHRINK = 3

        const val RUN_STATE_STOPPED = 0
        const val RUN_STATE_STARTING = 1
        const val RUN_STATE_STARTED = 2
        const val RUN_STATE_RUNNING = 3
        const val RUN_STATE_STOPPING = 4

        class CircularProgressDrawable(
            var mPadding: Int,
            var mInitialAngle: Float,
            var mMaxSweepAngle: Float,
            var mMinSweepAngle: Float,
            var mStrokeSize: Int,
            var mStrokeColors: IntArray,
            var mReverse: Boolean,
            var mRotateDuration: Int,
            var mTransformDuration: Int,
            var mKeepDuration: Int,
            var mTransformInterpolator: Interpolator,
            var mProgressMode: Int,
            var mInAnimationDuration: Int,
            var mInStepPercent: Float,
            var mInColors: IntArray,
            var mOutAnimationDuration: Int
        ) : Drawable(), Animatable {

            lateinit var mPaint: Paint
            lateinit var mRect: RectF
            var mRunState = RUN_STATE_STOPPED
            private var mLastUpdateTime: Long = 0
            private var mLastProgressStateTime: Long = 0
            private var mLastRunStateTime: Long = 0
            var mProgressState = 0
            var mStrokeColorIndex: Int = 0
            var mStartAngle = 0f
            var mSweepAngle = 0f

            init {
                mPaint = Paint()
                mPaint.isAntiAlias = true
                mPaint.strokeCap = Paint.Cap.ROUND
                mPaint.strokeJoin = Paint.Join.ROUND

                mRect = RectF()
            }

            private fun drawIndeterminate(canvas: Canvas) {
                if (mRunState == RUN_STATE_STARTING) {
                    val bounds = bounds
                    val x = (bounds.left + bounds.right) / 2f
                    val y = (bounds.top + bounds.bottom) / 2f
                    val maxRadius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2f) / 2f

                    val stepTime = 1f / (mInStepPercent * (mInColors.size + 2) + 1)
                    val time = (SystemClock.uptimeMillis() - mLastRunStateTime) as Float / mInAnimationDuration
                    val steps = time / stepTime

                    var outerRadius = 0f
                    var innerRadius = 0f

                    for (i in Math.floor(steps.toDouble()).toInt() downTo 0) {
                        innerRadius = outerRadius
                        outerRadius = Math.min(1f, (steps - i) * mInStepPercent) * maxRadius

                        if (i >= mInColors.size)
                            continue

                        if (innerRadius == 0f) {
                            mPaint.color = mInColors[i]
                            mPaint.style = Paint.Style.FILL
                            canvas.drawCircle(x, y, outerRadius, mPaint)
                        } else if (outerRadius > innerRadius) {
                            val radius = (innerRadius + outerRadius) / 2
                            mRect.set(x - radius, y - radius, x + radius, y + radius)

                            mPaint.strokeWidth = outerRadius - innerRadius
                            mPaint.style = Paint.Style.STROKE
                            mPaint.color = mInColors[i]

                            canvas.drawCircle(x, y, radius, mPaint)
                        } else
                            break
                    }

                    if (mProgressState === PROGRESS_STATE_HIDE) {
                        if (steps >= 1 / mInStepPercent || time >= 1) {
                            resetAnimation()
                            mProgressState = PROGRESS_STATE_STRETCH
                        }
                    } else {
                        val radius = maxRadius - mStrokeSize / 2f

                        mRect.set(x - radius, y - radius, x + radius, y + radius)
                        mPaint.strokeWidth = mStrokeSize * 1f
                        mPaint.style = Paint.Style.STROKE
                        mPaint.color = getIndeterminateStrokeColor()

                        canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
                    }
                } else if (mRunState === RUN_STATE_STOPPING) {
                    val size = mStrokeSize as Float * Math.max(
                        0,
                        mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime
                    ) / mOutAnimationDuration

                    if (size > 0) {
                        val bounds = bounds
                        val radius =
                            (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize * 2 + size) / 2f
                        val x = (bounds.left + bounds.right) / 2f
                        val y = (bounds.top + bounds.bottom) / 2f

                        mRect.set(x - radius, y - radius, x + radius, y + radius)
                        mPaint.strokeWidth = size
                        mPaint.style = Paint.Style.STROKE
                        mPaint.color = getIndeterminateStrokeColor()


                        canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
                    }
                } else if (mRunState !== RUN_STATE_STOPPED) {
                    val bounds = bounds
                    val radius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2 - mStrokeSize) / 2f
                    val x = (bounds.left + bounds.right) / 2f
                    val y = (bounds.top + bounds.bottom) / 2f

                    mRect.set(x - radius, y - radius, x + radius, y + radius)
                    mPaint.strokeWidth = mStrokeSize.toFloat()
                    mPaint.style = Paint.Style.STROKE
                    mPaint.color = getIndeterminateStrokeColor()

                    canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint)
                }
            }

            private fun resetAnimation() {
                mLastUpdateTime = SystemClock.uptimeMillis()
                mLastProgressStateTime = mLastUpdateTime
                mStartAngle = mInitialAngle
                mStrokeColorIndex = 0
                mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
            }

            private fun getIndeterminateStrokeColor(): Int {
                if (mProgressState !== PROGRESS_STATE_KEEP_SHRINK || mStrokeColors.size == 1)
                    return mStrokeColors[mStrokeColorIndex]

                val value = Math.max(
                    0f,
                    Math.min(1f, (SystemClock.uptimeMillis() - mLastProgressStateTime) as Float / mKeepDuration)
                )
                val prev_index = if (mStrokeColorIndex === 0) mStrokeColors.size - 1 else mStrokeColorIndex - 1

                return ColorUtil.getMiddleColor(mStrokeColors[prev_index], mStrokeColors[mStrokeColorIndex], value)
            }

            override fun isRunning(): Boolean {
                return mRunState != RUN_STATE_STOPPED
            }

            override fun start() {
                start(mInAnimationDuration > 0);
            }

            override fun stop() {
                stop(mOutAnimationDuration > 0)
            }

            override fun draw(canvas: Canvas) {
                drawIndeterminate(canvas)
            }

            override fun setAlpha(alpha: Int) {
                mPaint.alpha = alpha
            }

            override fun getOpacity(): Int {
                return PixelFormat.TRANSLUCENT
            }

            override fun setColorFilter(colorFilter: ColorFilter?) {
                mPaint.colorFilter = colorFilter
            }

            override fun scheduleSelf(what: Runnable, w: Long) {
                if (mRunState === RUN_STATE_STOPPED)
                    mRunState = if (mInAnimationDuration > 0) RUN_STATE_STARTING else RUN_STATE_RUNNING
                super.scheduleSelf(what, w)
            }

            private fun start(withAnimation: Boolean) {
                if (isRunning)
                    return

                resetAnimation()

                if (withAnimation) {
                    mRunState = RUN_STATE_STARTING
                    mLastRunStateTime = SystemClock.uptimeMillis()
                    mProgressState = PROGRESS_STATE_HIDE
                }

                scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION)
                invalidateSelf()
            }

            private fun stop(withAnimation: Boolean) {
                if (!isRunning)
                    return

                if (withAnimation) {
                    mLastRunStateTime = SystemClock.uptimeMillis()
                    if (mRunState === RUN_STATE_STARTED) {
                        scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION)
                        invalidateSelf()
                    }
                    mRunState = RUN_STATE_STOPPING
                } else {
                    mRunState = RUN_STATE_STOPPED
                    unscheduleSelf(mUpdater)
                    invalidateSelf()
                }
            }

            private val mUpdater = Runnable { update() }
            private fun update() {
                when (mProgressMode) {
                    MODE_DETERMINATE -> updateDeterminate()
                    MODE_INDETERMINATE -> updateIndeterminate()
                }
            }

            private fun updateDeterminate() {
                val curTime = SystemClock.uptimeMillis()
                var rotateOffset = (curTime - mLastUpdateTime) * 360f / mRotateDuration
                if (mReverse)
                    rotateOffset = -rotateOffset
                mLastUpdateTime = curTime

                mStartAngle += rotateOffset

                if (mRunState === RUN_STATE_STARTING) {
                    if (curTime - mLastRunStateTime > mInAnimationDuration) {
                        mRunState = RUN_STATE_RUNNING
                    }
                } else if (mRunState === RUN_STATE_STOPPING) {
                    if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                        stop(false)
                        return
                    }
                }

                if (isRunning)
                    scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION)

                invalidateSelf()
            }

            private fun updateIndeterminate() {
                //update animation
                val curTime = SystemClock.uptimeMillis()
                var rotateOffset = (curTime - mLastUpdateTime) * 360f / mRotateDuration
                if (mReverse)
                    rotateOffset = -rotateOffset
                mLastUpdateTime = curTime

                when (mProgressState) {
                    PROGRESS_STATE_STRETCH -> if (mTransformDuration <= 0) {
                        mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
                        mProgressState = PROGRESS_STATE_KEEP_STRETCH
                        mStartAngle += rotateOffset
                        mLastProgressStateTime = curTime
                    } else {
                        val value = (curTime - mLastProgressStateTime) / mTransformDuration as Float
                        val maxAngle = if (mReverse) -mMaxSweepAngle else mMaxSweepAngle
                        val minAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle

                        mStartAngle += rotateOffset
                        mSweepAngle = mTransformInterpolator.getInterpolation(value) * (maxAngle - minAngle) + minAngle

                        if (value > 1f) {
                            mSweepAngle = maxAngle
                            mProgressState = PROGRESS_STATE_KEEP_STRETCH
                            mLastProgressStateTime = curTime
                        }
                    }
                    PROGRESS_STATE_KEEP_STRETCH -> {
                        mStartAngle += rotateOffset

                        if (curTime - mLastProgressStateTime > mKeepDuration) {
                            mProgressState = PROGRESS_STATE_SHRINK
                            mLastProgressStateTime = curTime
                        }
                    }
                    PROGRESS_STATE_SHRINK -> if (mTransformDuration <= 0) {
                        mSweepAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle
                        mProgressState = PROGRESS_STATE_KEEP_SHRINK
                        mStartAngle += rotateOffset
                        mLastProgressStateTime = curTime
                        mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors.size
                    } else {
                        val value = (curTime - mLastProgressStateTime) / mTransformDuration as Float
                        val maxAngle = if (mReverse) -mMaxSweepAngle else mMaxSweepAngle
                        val minAngle = if (mReverse) -mMinSweepAngle else mMinSweepAngle

                        val newSweepAngle =
                            (1f - mTransformInterpolator.getInterpolation(value)) * (maxAngle - minAngle) + minAngle
                        mStartAngle += rotateOffset + mSweepAngle - newSweepAngle
                        mSweepAngle = newSweepAngle

                        if (value > 1f) {
                            mSweepAngle = minAngle
                            mProgressState = PROGRESS_STATE_KEEP_SHRINK
                            mLastProgressStateTime = curTime
                            mStrokeColorIndex = (mStrokeColorIndex + 1) % mStrokeColors.size
                        }
                    }
                    PROGRESS_STATE_KEEP_SHRINK -> {
                        mStartAngle += rotateOffset

                        if (curTime - mLastProgressStateTime > mKeepDuration) {
                            mProgressState = PROGRESS_STATE_STRETCH
                            mLastProgressStateTime = curTime
                        }
                    }
                }

                if (mRunState === RUN_STATE_STARTING) {
                    if (curTime - mLastRunStateTime > mInAnimationDuration) {
                        mRunState = RUN_STATE_RUNNING
                        if (mProgressState === PROGRESS_STATE_HIDE) {
                            resetAnimation()
                            mProgressState = PROGRESS_STATE_STRETCH
                        }
                    }
                } else if (mRunState === RUN_STATE_STOPPING) {
                    if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                        stop(false)
                        return
                    }
                }

                if (isRunning)
                    scheduleSelf(mUpdater, SystemClock.uptimeMillis() + ViewUtil.FRAME_DURATION)

                invalidateSelf()
            }

        }

        class Builder{
            private var mPadding: Int = 0
            private var mInitialAngle: Float = 0.toFloat()
            private var mMaxSweepAngle: Float = 0.toFloat()
            private var mMinSweepAngle: Float = 0.toFloat()
            private var mStrokeSize: Int = 0
            private lateinit var mStrokeColors: IntArray
            private var mReverse: Boolean = false
            private var mRotateDuration: Int = 0
            private var mTransformDuration: Int = 0
            private var mKeepDuration: Int = 0
            private lateinit var mTransformInterpolator: Interpolator
            private var mProgressMode: Int = 0
            private var mInStepPercent: Float = 0.toFloat()
            private var mInColors: IntArray? = null
            private var mInAnimationDuration: Int = 0
            private var mOutAnimationDuration: Int = 0
            constructor(context: Context):this(context,null,0,0)
            constructor(context: Context, attrs: AttributeSet?,defStyleAttr:Int, defStyleRes: Int) {
                val a = context.obtainStyledAttributes(
                    attrs,
                    com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable,
                    defStyleAttr,
                    defStyleRes
                )
                var resId: Int

                padding(a.getDimensionPixelSize(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_padding, 0))
                initialAngle(a.getInteger(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_initialAngle, 0).toFloat());
                maxSweepAngle(a.getInteger(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_maxSweepAngle, 270).toFloat());
                minSweepAngle(a.getInteger(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_minSweepAngle, 1).toFloat());
                strokeSize(
                    a.getDimensionPixelSize(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_strokeSize,
                        ThemeUtil.dpToPx(context, 4)
                    )
                )
                strokeColors(
                    a.getColor(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_strokeColor,
                        ThemeUtil.colorPrimary(context, -0x1000000)
                    )
                )
                resId = a.getResourceId(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_strokeColors, 0)
                if (resId != 0) {
                    val ta = context.resources.obtainTypedArray(resId)
                    val colors = IntArray(ta.length())
                    for (j in 0 until ta.length())
                        colors[j] = ta.getColor(j, 0)
                    ta.recycle()
                    strokeColors2(colors)
                }
                reverse(a.getBoolean(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_reverse, false))
                rotateDuration(
                    a.getInteger(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_rotateDuration,
                        context.resources.getInteger(android.R.integer.config_longAnimTime)
                    )
                )
                transformDuration(
                    a.getInteger(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_transformDuration,
                        context.resources.getInteger(android.R.integer.config_mediumAnimTime)
                    )
                )
                keepDuration(
                    a.getInteger(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_keepDuration,
                        context.resources.getInteger(android.R.integer.config_shortAnimTime)
                    )
                )
                resId = a.getResourceId(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_transformInterpolator, 0)
                if (resId != 0)
                    transformInterpolator(AnimationUtils.loadInterpolator(context, resId))
                progressMode(
                    a.getResourceId(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_pv_progressMode,
                        MODE_INDETERMINATE
                    )
                )
                //progressMode(a.getInteger(R.styleable.CircularProgressDrawable_pv_progressMode, ProgressView.MODE_INDETERMINATE));
                inAnimDuration(
                    a.getInteger(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_inAnimDuration,
                        context.resources.getInteger(android.R.integer.config_mediumAnimTime)
                    )
                )
                resId = a.getResourceId(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_inStepColors, 0)
                if (resId != 0) {
                    val ta = context.resources.obtainTypedArray(resId)
                    val colors = IntArray(ta.length())
                    for (j in 0 until ta.length())
                        colors[j] = ta.getColor(j, 0)
                    ta.recycle()
                    inStepColors(colors)
                }
                inStepPercent(a.getFloat(com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_inStepPercent, 0.5f))
                outAnimDuration(
                    a.getInteger(
                        com.vincent.dialoglibrary.R.styleable.CircularProgressDrawable_cpd_outAnimDuration,
                        context.resources.getInteger(android.R.integer.config_mediumAnimTime)
                    )
                )
                a.recycle()
            }


            fun padding(padding: Int): Builder {
                mPadding = padding
                return this
            }

            fun initialAngle(angle: Float): Builder {
                mInitialAngle = angle
                return this
            }

            fun maxSweepAngle(angle: Float): Builder {
                mMaxSweepAngle = angle
                return this
            }

            fun minSweepAngle(angle: Float): Builder {
                mMinSweepAngle = angle
                return this
            }

            fun strokeSize(strokeSize: Int): Builder {
                mStrokeSize = strokeSize
                return this
            }

            fun strokeColors(vararg strokeColors: Int): Builder {
                mStrokeColors = strokeColors
                return this
            }

            fun strokeColors2( strokeColors: IntArray): Builder {
                mStrokeColors = strokeColors
                return this
            }

            fun reverse(reverse: Boolean): Builder {
                mReverse = reverse
                return this
            }

            fun rotateDuration(duration: Int): Builder {
                mRotateDuration = duration
                return this
            }

            fun transformDuration(duration: Int): Builder {
                mTransformDuration = duration
                return this
            }

            fun keepDuration(duration: Int): Builder {
                mKeepDuration = duration
                return this
            }

            fun transformInterpolator(interpolator: Interpolator): Builder {
                mTransformInterpolator = interpolator
                return this
            }

            fun progressMode(mode: Int): Builder {
                mProgressMode = mode
                return this
            }

            fun inAnimDuration(duration: Int): Builder {
                mInAnimationDuration = duration
                return this
            }

            fun inStepPercent(percent: Float): Builder {
                mInStepPercent = percent
                return this
            }

            fun inStepColors( colors: IntArray): Builder {
                mInColors = colors
                return this
            }

            fun outAnimDuration(duration: Int): Builder {
                mOutAnimationDuration = duration
                return this
            }
        }
    }



    object ColorUtil {
        private fun getMiddleValue(prev: Int, next: Int, factor: Float): Int {
            return Math.round(prev + (next - prev) * factor)
        }

        fun getMiddleColor(prevColor: Int, curColor: Int, factor: Float): Int {
            if (prevColor == curColor)
                return curColor

            if (factor == 0f)
                return prevColor
            else if (factor == 1f)
                return curColor

            val a = getMiddleValue(Color.alpha(prevColor), Color.alpha(curColor), factor)
            val r = getMiddleValue(Color.red(prevColor), Color.red(curColor), factor)
            val g = getMiddleValue(Color.green(prevColor), Color.green(curColor), factor)
            val b = getMiddleValue(Color.blue(prevColor), Color.blue(curColor), factor)

            return Color.argb(a, r, g, b)
        }

    }

     object ViewUtil {

        val FRAME_DURATION = (1000 / 60).toLong()

        fun setBackground(v: View, drawable: Drawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                v.background = drawable
            else
                v.setBackgroundDrawable(drawable)
        }

    }

     object ThemeUtil {

        private var value: TypedValue? = null

        fun dpToPx(context: Context, dp: Int): Int {
            return (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }

        private fun getColor(context: Context, id: Int, defaultValue: Int): Int {
            if (value == null)
                value = TypedValue()

            try {
                val theme = context.theme
                if (theme != null && theme.resolveAttribute(id, value, true)) {
                    if (value!!.type >= TypedValue.TYPE_FIRST_INT && value!!.type <= TypedValue.TYPE_LAST_INT)
                        return value!!.data
                    else if (value!!.type == TypedValue.TYPE_STRING)
                        return context.resources.getColor(value!!.resourceId)
                }
            } catch (ex: Exception) {

            }

            return defaultValue
        }


        fun colorPrimary(context: Context, defaultValue: Int): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) getColor(
                context,
                android.R.attr.colorPrimary,
                defaultValue
            ) else getColor(context, com.vincent.dialoglibrary.R.attr.colorPrimary, defaultValue)

        }
    }
}