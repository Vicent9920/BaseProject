package com.vincent.baseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haoge.easyandroid.EasyAndroid
import com.vincent.baseproject.ui.DialogActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EasyAndroid.init(this)
        tv_info.setOnClickListener {
            startActivity(Intent(this,DialogActivity::class.java))
        }
    }
}
