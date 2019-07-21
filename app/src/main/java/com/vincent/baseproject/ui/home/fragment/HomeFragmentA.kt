package com.vincent.baseproject.ui.home.fragment

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.haoge.easyandroid.easy.EasyActivityResult
import com.haoge.easyandroid.easy.EasyPermissions
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baseproject.R
import com.vincent.baseproject.common.BaseUiFragment
import com.vincent.baseproject.ui.ScannerActivity
import com.vincent.baseproject.widget.XCollapsingToolbarLayout
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import kotlinx.android.synthetic.main.home_fragment_a.*


/**
 * 创建日期：2019/3/6 0006on 下午 4:32
 * 描述：首页
 * @author：Vincent
 * QQ：3332168769
 * 备注：滑动到指定距离头部区域改变
 */
class HomeFragmentA : BaseUiFragment() {

    override fun getLayoutId() = R.layout.home_fragment_a

    override fun initView() {
        setTitleBar(top_toolbar)
        ctl_top_bar.mListener = object : XCollapsingToolbarLayout.OnScrimsListener {
            override fun onScrimsStateChange(shown: Boolean) {
                if (shown) {
                    homeA_tv_address.setTextColor(ContextCompat.getColor(mActivity, R.color.black))
                    homeA_iv_scan.setImageResource(R.mipmap.scan_change)
                } else {
                    homeA_tv_address.setTextColor(ContextCompat.getColor(mActivity, R.color.white))
                    homeA_iv_scan.setImageResource(R.mipmap.scan_normal)
                }
                homeA_tv_search.isSelected = shown
            }

        }
    }

    override fun initEvent() {
        super.initEvent()
        homeA_tv_address.setOnClickListener {
            val hotCities = mutableListOf<HotCity>()
            hotCities.add(HotCity("北京", "北京", "101010100")) //code为城市代码
            hotCities.add(HotCity("上海", "上海", "101020100"))
            hotCities.add(HotCity("广州", "广东", "101280101"))
            hotCities.add(HotCity("深圳", "广东", "101280601"))
            hotCities.add(HotCity("杭州", "浙江", "101210101"))
            CityPicker.from(mActivity) //activity或者fragment
                .enableAnimation(true)    //启用动画效果，默认无
//                .setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(null)  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(object : OnPickListener {
                    override fun onPick(position: Int, data: City?) {
                        homeA_tv_address.text = data?.name
                        EasyToast.DEFAULT.show(data?.name)
                    }

                    override fun onCancel() {
                        EasyToast.DEFAULT.show("取消选择")
                    }

                    override fun onLocate() {
                        //定位接口，需要APP自身实现，这里模拟一下定位
                        Handler().postDelayed({
                            //定位完成之后更新数据
                            CityPicker.from(mActivity)
                                .locateComplete(LocatedCity("成都", "四川", "101270101"), LocateState.SUCCESS)
                        }, 1500)
                    }

                })
                .show()
        }
        homeA_iv_scan.setOnClickListener {
            EasyPermissions.create(Manifest.permission.CAMERA).callback {
                if (it) {
                    EasyActivityResult.startActivity(mActivity, Intent(mActivity, ScannerActivity::class.java), null)
                } else {
                    EasyToast.DEFAULT.show("你拒绝了打开摄像头权限，无法打开扫一扫")
                }
            }.request(mActivity)

        }
    }


    private fun setTitleBar(view: View) {
        val layoutParams = view.layoutParams
        if (layoutParams.height != -2 && layoutParams.height != -1) {
            layoutParams.height += getSystemBarHeight()
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + getSystemBarHeight(),
                view.paddingRight,
                view.paddingBottom
            )
        } else {
            view.post {
                layoutParams.height = view.height + getSystemBarHeight()
                view.setPadding(
                    view.paddingLeft,
                    view.paddingTop + getSystemBarHeight(),
                    view.paddingRight,
                    view.paddingBottom
                )
            }
        }
    }


}