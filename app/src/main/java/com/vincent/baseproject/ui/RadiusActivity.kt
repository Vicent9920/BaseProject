package com.vincent.baseproject.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.haoge.easyandroid.easy.EasyFormatter
import com.haoge.easyandroid.easy.EasyLog
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.widget.radius.RadiusTextView
import com.vincent.baselibrary.widget.radius.delegate.RadiusViewDelegate
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import kotlinx.android.synthetic.main.activity_radius.*
import kotlinx.android.synthetic.main.app_toolbar.*
import com.vincent.baselibrary.widget.radius.RadiusRadioButton



class RadiusActivity : UIActivity() {

    private val mBgDrawable = GradientDrawable()

    override fun getLayoutId() = R.layout.activity_radius

    override fun initView() {
        tv_title.text = "对话框示例"
        initToolBar(app_toolbar)
        mBgDrawable.cornerRadius = resources.getDimension(R.dimen.dp_radius)
        mBgDrawable.setStroke(
            resources.getDimensionPixelSize(R.dimen.dp_stroke_width),
            Color.MAGENTA,
            resources.getDimension(R.dimen.dp_dash_width),
            resources.getDimension(R.dimen.dp_dash_gap))
        mBgDrawable.setShape(GradientDrawable.RECTANGLE)

        rtv_javaBg.background = mBgDrawable
        rtv_javaBg.delegate?.run {
            this.setTextCheckedColor(Color.BLUE)
            .setBackgroundCheckedColor(Color.WHITE)
            .setRadius(resources.getDimension(R.dimen.dp_radius))
            .setStrokeWidth(resources.getDimensionPixelSize(R.dimen.dp_stroke_width))
            .setStrokeColor(ContextCompat.getColor(this@RadiusActivity,android.R.color.holo_purple))
            .setStrokeDashWidth(resources.getDimension(R.dimen.dp_dash_width))
            .setStrokeDashGap(resources.getDimension(R.dimen.dp_dash_gap))
            .initShape()
        }

        rtv_selectRadius.setOnClickListener {
            rtv_selectRadius.isSelected = it.isSelected.not()
        }

        rtv_drawableRadius.delegate?.run {
            this.setLeftDrawable(null)
            .setOnSelectedChangeListener(object : RadiusViewDelegate.OnSelectedChangeListener{
                override fun onSelectedChanged(view: View, isSelected: Boolean) {
                    EasyToast.DEFAULT.show("isSelected:$isSelected")
                }

            }).initShape()
        }

        radioTest_radius.delegate?.run {
            this.setButtonPressedDrawable(R.drawable.ic_cb_normal)
                .initShape()
        }
        ret_circle.setText("我就试一试光标位置")

        rtv_testRadius.setOnClickListener {
            rtv_testRadius.isEnabled = it.isEnabled.not()
        }
        rtv_drawableRadius.setOnClickListener {
            rtv_drawableRadius.isSelected = it.isSelected.not()
        }

//        val radiusSwitch = RadiusRadioButton(this)
//        radiusSwitch.delegate?.run {
//           this .setBackgroundSelectedColor(Color.BLUE)
//            .setBackgroundCheckedColor(Color.YELLOW)
//            .setRadius(4f)
//            .initShape()
//        }

//        layout_content.addView(radiusSwitch)
    }


}
