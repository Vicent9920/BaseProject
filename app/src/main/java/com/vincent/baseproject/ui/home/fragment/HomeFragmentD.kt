package com.vincent.baseproject.ui.home.fragment


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import com.bumptech.glide.Glide
import com.vincent.baselibrary.base.BaseLazyFragment
import com.vincent.baseproject.R
import com.vincent.baseproject.ui.DialogActivity
import com.vincent.baseproject.ui.SettingActivity
import com.vincent.baseproject.ui.WebActivity
import com.vincent.baseproject.ui.login.LoginActivity
import com.vincent.baseproject.ui.login.RegisterActivity
import com.zhouwei.mzbanner.MZBannerView
import com.zhouwei.mzbanner.holder.MZHolderCreator
import com.zhouwei.mzbanner.holder.MZViewHolder
import kotlinx.android.synthetic.main.home_fragment_d.*


class HomeFragmentD : BaseLazyFragment() {

    private var statusAlpha = 0
    override fun getLayoutId() = R.layout.home_fragment_d

    override fun initView() {
        tv_title.text = "我的"
        top_layout.post {
            top_layout.background.mutate().alpha = statusAlpha
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val lp: RelativeLayout.LayoutParams = top_layout.layoutParams as RelativeLayout.LayoutParams
            lp.topMargin = getSystemBarHeight()
            top_layout.layoutParams = lp
        }
        val pages = mutableListOf<String>()
        pages.add("https://tse3.mm.bing.net/th?id=OIP.87OnFjdaecqw4R_Y1XH_KQHaEK&pid=Api&dpr=1")
        pages.add("https://tse2.mm.bing.net/th?id=OIP.Fs3binc-wFNJWYRDS7-09AHaE8&pid=Api&dpr=1")
        pages.add("https://www.technobezz.com/files/uploads/2016/12/Android-7.0-Nougat.jpg")
        pages.add("https://www.extremetech.com/wp-content/uploads/2017/03/smiling-android.jpg")
        val MzBanner = banner as MZBannerView<String>
        MzBanner.setPages(pages, object : MZHolderCreator<BannerViewHolder> {
            override fun createViewHolder(): BannerViewHolder {
                return BannerViewHolder()
            }

        })
    }

    override fun initEvent() {
        super.initEvent()
        nsv_layout.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val headerHeight = top_layout.height
            val scrollDistance = Math.min(scrollY, headerHeight)
            statusAlpha = (255F * scrollDistance / headerHeight).toInt()
            setTopBackground()
        }
        homeD_btn_dialog.setOnClickListener {
            startActivity(Intent(mActivity, DialogActivity::class.java))
        }
        homeD_btn_login.setOnClickListener {
            startActivity(Intent(mActivity, LoginActivity::class.java))
        }
        homeD_btn_register.setOnClickListener {
            startActivity(Intent(mActivity, RegisterActivity::class.java))
        }
        homeD_btn_browser.setOnClickListener {
            startActivity(Intent(mActivity, WebActivity::class.java).putExtra("url", "https://www.pgyer.com/he3F"))
        }

        homeD_btn_about.setOnClickListener {
            startActivity(
                Intent(mActivity, WebActivity::class.java).putExtra(
                    "url",
                    "https://github.com/Vicent9920/BaseProject/blob/master/README.md"
                )
            )
        }

        homeD_btn_setting.setOnClickListener {
            startActivity(Intent(mActivity,SettingActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        banner.start()
    }

    override fun onPause() {
        super.onPause()
        banner.pause()
    }

    private fun setTopBackground() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            top_layout.setBackgroundColor(Color.argb(statusAlpha, 255, 255, 255))
            val window = activity!!.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.argb(statusAlpha, 255, 255, 255)
        }
    }

    class BannerViewHolder : MZViewHolder<String> {
        lateinit var bannerImg: ImageView
        override fun onBind(p0: Context?, p1: Int, p2: String?) {
            p0 ?: return
            p2 ?: return
            Glide.with(p0).load(p2).error(R.mipmap.bg_launcher).into(bannerImg)
        }

        override fun createView(p0: Context?): View {
            val view = LayoutInflater.from(p0).inflate(R.layout.banner_item, null)
            bannerImg = view.findViewById(R.id.banner_image)
            return view
        }

    }
}


