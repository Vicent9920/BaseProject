package com.vincent.baselibrary.helper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 文本输入辅助类，通过管理多个 EditText 输入是否为空来启用或者禁用按钮的点击事件
 *    blog   : https://www.jianshu.com/p/fd3795e8a6b3
 */
class EditTextInputHelper(val view: View,var alpha:Boolean = false) :TextWatcher{

    private var mViewSet: MutableList<EditText> = mutableListOf()
    fun addView(vararg edits: EditText){
        for (e in edits){
            e.addTextChangedListener(this)
            mViewSet.add(e)
        }
        afterTextChanged(null)
    }

    fun removeViews(){
        if(mViewSet.isEmpty())return
        for (e in mViewSet){
            e.removeTextChangedListener(this)
        }
        mViewSet.clear()

    }

    /**
     * 设置 View 的事件
     * @param enabled   启用或者禁用 View 的事件
     */
    fun setEnabled(enabled: Boolean) {
        if(enabled == view.isEnabled)return
        if(enabled){
            view.isEnabled = true
            if(alpha)view.alpha = 1f
        }else{
            view.isEnabled = false
            if(alpha)view.alpha = 0.5f
        }
    }
    override fun afterTextChanged(s: Editable?) {
        for (e in mViewSet){
            if(e.text.toString().isBlank()){
                setEnabled(false)
                return
            }
        }
        setEnabled(true)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }
}