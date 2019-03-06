package com.vincent.baselibrary.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import com.vincent.baselibrary.R

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 带清除按钮的EditText
 */
class ClearEditText : EditText, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private lateinit var mClearIcon: Drawable

    private var mOnTouchListener: View.OnTouchListener? = null
    private var mOnFocusChangeListener: View.OnFocusChangeListener? = null


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val drawable = ContextCompat.getDrawable(context, R.mipmap.icon_input_del)
        mClearIcon = DrawableCompat.wrap(drawable!!)
        mClearIcon.setBounds(0, 0, mClearIcon.intrinsicWidth, mClearIcon.intrinsicHeight)
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
        ViewCompat.setBackgroundTintList(this, ContextCompat.getColorStateList(context, R.color.black60))
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFocused) {
            setClearIconVisible(!text.isNullOrEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            setClearIconVisible(text.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener?.onFocusChange(v, hasFocus)
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event ?: return false
        val x = event.x.toInt()
        if (mClearIcon.isVisible && x > width - paddingRight - mClearIcon.intrinsicWidth) {
            if (event.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener?.onTouch(v, event) ?: false
    }

    override fun setOnTouchListener(l: OnTouchListener?) {
        mOnTouchListener = l
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        mOnFocusChangeListener = l
    }

    private fun setClearIconVisible(visible: Boolean) {
        if (mClearIcon.isVisible == visible) return
        mClearIcon.setVisible(visible, false)
        val compoundDrawables = compoundDrawables
        setCompoundDrawables(
            compoundDrawables[0],
            compoundDrawables[1],
            if (visible) mClearIcon else null,
            compoundDrawables[3]
        )
    }


}