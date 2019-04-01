package com.vincent.baseproject.ui.city_picker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.haoge.easyandroid.easy.EasyToast
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity
import com.vincent.baseproject.data.SourceCity
import com.vincent.baseproject.util.ChangeToPinYin
import com.zaaach.citypicker.CityPicker
import com.zaaach.citypicker.adapter.OnPickListener
import com.zaaach.citypicker.db.CustomDBManager
import com.zaaach.citypicker.model.City
import com.zaaach.citypicker.model.HotCity
import com.zaaach.citypicker.model.LocateState
import com.zaaach.citypicker.model.LocatedCity
import kotlinx.android.synthetic.main.activity_city_picker.*
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.home_fragment_a.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CityPickerActivity : UIActivity() {
    override fun getLayoutId() = R.layout.activity_city_picker

    private var sourceData = mutableListOf<SourceCity>()
    private var targetData = mutableListOf<City>()
    private val hotCities = mutableListOf<HotCity>()

    private var isInit = false
    override fun initView() {
        tv_title.text = "选择城市"
        initToolBar(app_toolbar)
    }

    override fun initData() {
        super.initData()
        getTargetList()

        hotCities.add(HotCity("北京", "北京", "101010100")) //code为城市代码
        hotCities.add(HotCity("上海", "上海", "101020100"))
        hotCities.add(HotCity("广州", "广东", "101280101"))
        hotCities.add(HotCity("深圳", "广东", "101280601"))
        hotCities.add(HotCity("杭州", "浙江", "101210101"))
    }

    override fun initEvent() {
        super.initEvent()
        cityPicker_btn_default.setOnClickListener {
            CityPicker.from(this) //activity或者fragment
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
                        Handler().postDelayed( {
                            //定位完成之后更新数据
                            CityPicker.from(this@CityPickerActivity)
                                .locateComplete(LocatedCity("成都", "四川", "101270101"), LocateState.SUCCESS)
                        }, 1500)
                    }

                })
                .show()
        }

        cityPicker_btn_list.setOnClickListener {
            if(!isInit){
                EasyToast.DEFAULT.show("数据正在初始化，请稍后")
                return@setOnClickListener
            }
            CityPicker.from(this) //activity或者fragment
                .enableAnimation(true)    //启用动画效果，默认无
//                .setAnimationStyle(anim)	//自定义动画
                .setLocatedCity(null)  //APP自身已定位的城市，传null会自动定位（默认）
                .setHotCities(hotCities)    //指定热门城市
                .setDbManager(CustomDBManager(this,targetData))
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
                        Handler().postDelayed( {
                            //定位完成之后更新数据
                            CityPicker.from(this@CityPickerActivity)
                                .locateComplete(LocatedCity("成都", "四川", "101270101"), LocateState.SUCCESS)
                        }, 1500)
                    }

                })
                .show()
        }
    }

    private fun getTargetList(){
        val jsonData = getJson(this,"city.json")
        sourceData = Gson().fromJson(jsonData,object : TypeToken<List<SourceCity>>(){}.type)
        object : Thread() {
            var cityId = 0
            override fun run() {
                super.run()
                sourceData.forEach {province->
                    province.city.forEach { city ->
                        val targetCity = City(city.name.trim(),province.name.trim(),
                            ChangeToPinYin.getStringPinYin(city.name.trim()),cityId.toString())
                        targetData.add(targetCity)
                        cityId++
                        for (i in city.area){
                            if(i.trim().isEmpty()) break
                            val tCity = City(i,province.name.trim(), ChangeToPinYin.getStringPinYin(i.trim()),cityId.toString())
                            targetData.add(tCity)
                            cityId++
                        }
                    }
                }
                isInit = true
            }
        }.start()
    }

    private fun getJson(context: Context, fileName: String): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val bf = BufferedReader(InputStreamReader(assetManager.open(fileName)))
            var line = bf.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bf.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }
}
