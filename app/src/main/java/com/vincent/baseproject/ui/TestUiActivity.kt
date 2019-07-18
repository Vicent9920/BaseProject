package com.vincent.baseproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baseproject.R
import kotlinx.android.synthetic.main.activity_test_ui.*

class TestUiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_ui)
//        radiusSwitch.setOnClickListener {
//            EasyToast.DEFAULT.show("点击了")
//        }
    }
}
