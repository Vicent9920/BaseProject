package com.vincent.baseproject.ui.home


import android.Manifest
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.haoge.easyandroid.easy.EasyPermissions
import com.vincent.baselibrary.util.NetUtils
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
    }


    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_fragment).navigateUp()
    }

    override fun isSupportSwipeBack(): Boolean {
        return false
    }

}
