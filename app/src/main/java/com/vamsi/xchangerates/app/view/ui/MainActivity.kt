package com.vamsi.xchangerates.app.view.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.vamsi.xchangerates.app.R
import com.vamsi.xchangerates.app.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navController = findNavController(this, R.id.navFragment)

        // Set up navigation menu
        NavigationUI.setupWithNavController(binding.navigationView, navController)
    }
}
