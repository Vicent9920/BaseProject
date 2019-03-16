package com.vincent.baseproject.ui.home.fragment

import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baselibrary.base.BaseLazyFragment
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
class HomeFragmentA : BaseLazyFragment() {
    override fun getLayoutId() = com.vincent.baseproject.R.layout.home_fragment_a

    override fun initView() {
        setTitleBar(top_toolbar)
        ctl_top_bar.mListener = object : XCollapsingToolbarLayout.OnScrimsListener {
            override fun onScrimsStateChange(shown: Boolean) {
                if (shown) {
                    homeA_tv_address.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            com.vincent.baseproject.R.color.black
                        )
                    )
                } else {
                    homeA_tv_address.setTextColor(
                        ContextCompat.getColor(
                            context!!,
                            com.vincent.baseproject.R.color.white
                        )
                    )
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
                .setLocatedCity(LocatedCity("杭州", "浙江", "101210101"))  //APP自身已定位的城市，传null会自动定位（默认）
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
                        Handler().postDelayed(Runnable {
                            //定位完成之后更新数据
                            CityPicker.from(this@HomeFragmentA)
                                .locateComplete(LocatedCity("成都", "四川", "101270101"), LocateState.SUCCESS)
                        }, 3000)
                    }

                })
                .show()
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