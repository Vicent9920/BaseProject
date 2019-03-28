package com.vincent.baseproject.ui.home


import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vincent.baselibrary.helper.SpaceLayout
import com.vincent.baseproject.R
import com.vincent.baseproject.common.UIActivity

class MainActivity : UIActivity() {
    override fun getLayoutId() = R.layout.activity_main

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        NavigationUI.setupWithNavController(navigation, navController)
        navigation.itemIconTintList = null

        // 如果首页不加载网络数据 建议在此处初始化，有利于提高app启动速度
        SpaceLayout.init(this)
        SpaceLayout.setEmptyLayout(R.layout.layout_empty)
        SpaceLayout.setLoadingLayout(R.layout.layout_loading)
        SpaceLayout.setNetworkErrorLayout(R.layout.network_error2_layout)
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_fragment).navigateUp()
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }


}
