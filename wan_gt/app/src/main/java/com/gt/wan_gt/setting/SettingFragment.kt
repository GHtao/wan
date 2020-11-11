package com.gt.wan_gt.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.search.SearchFragment
import com.gt.wan_gt.usage.UsageFragment
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/8/3 0003
 * author GT
 */
class SettingFragment:BaseFragment<SettingFragmentVM>() {
    override fun setContentView() = R.layout.fragment_setting

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initToolbar()
        initNormalSetting()
        initOtherSetting()
    }

    /**
     * 其他设置
     */
    private fun initOtherSetting(){
        setting_cache_size.text = viewModel.getCacheSize()
        setting_feedback.setOnClickListener {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO,
                Uri.parse("mailto:690040262@qq.com")),
                "选择使用的邮件客户端"))
        }
        setting_clear.setOnClickListener {
            viewModel.clearCache()
            setting_cache_size.text = "0K"
        }
    }
    /**
     * 初始化通用设置
     */
    private fun initNormalSetting(){
        setting_cb_cache.isChecked = viewModel.getCacheState()
        setting_cb_cache.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setCacheState(isChecked)
        }
        setting_cb_image.isChecked = viewModel.getImageState()
        setting_cb_image.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setImageState(isChecked)
        }

        setting_cb_night.isChecked = viewModel.getNightState()
        setting_cb_night.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setNightState(isChecked)
        }
    }
    private fun initToolbar(){
        common_tool_bar_title.text = "设置"
        common_tool_bar_back.setOnClickListener {
            pop()
        }
        common_tool_bar_usage.setOnClickListener {
            start(UsageFragment())
        }
        common_tool_bar_search.setOnClickListener {
            start(SearchFragment())
        }
    }

    override fun createVM(): SettingFragmentVM {
        return ViewModelProvider(this).get(SettingFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}