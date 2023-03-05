package com.mandi.view

import android.os.Bundle
import com.mandi.common.ToolbarConfig
import com.mandi.databinding.ActivityMainBinding
import com.mandi.extention.hide
import com.mandi.extention.show
import com.mandi.view.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    // region - life cycle methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
    // endregion

    // region - base class methods
    override fun initViews() {
    }

    override fun collectFlow() {
    }
    // endregion

    fun setToolbar(toolbarConfig: ToolbarConfig) {
        when (toolbarConfig) {
            ToolbarConfig.Hide -> {
                binding.toolbar.hide()
            }
            is ToolbarConfig.Show -> {
                binding.toolbar.show()
                toolbarConfig.customTitle?.let {
                    supportActionBar?.title = it
                }
            }
        }
    }
}