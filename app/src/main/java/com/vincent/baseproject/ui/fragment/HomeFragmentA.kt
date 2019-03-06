package com.vincent.baseproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vincent.baseproject.R

/**
 * 创建日期：2019/3/6 0006on 下午 4:32
 * 描述：
 * @author：Vincent
 * QQ：3332168769
 * 备注：
 */
class HomeFragmentA:Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_a,container,false)
    }


}