package com.gt.wan_gt.about_us

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/7/31 0031
 * author GT
 */
class AboutUsFragment:BaseFragment<AboutUsFragmentVM>() {
    override fun setContentView() = R.layout.fragment_about_us

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initToolbar()
        initContentView()
    }

    /**
     * 初始化toolbar
     */
    private fun initToolbar(){
        common_tool_bar_title.text = "关于我们"
        common_tool_bar_usage.visibility = View.GONE
        common_tool_bar_search.visibility = View.GONE
        common_tool_bar_back.setOnClickListener { pop() }
    }

    /**
     * 初始化内容
     */
    private fun initContentView(){
        about_us_content.text = Html.fromHtml(resources.getString(R.string.about_content))
        val versionCode = requireContext().packageManager
            .getPackageInfo(requireContext().packageName,0).versionCode
        about_us_title.text = "GT WanAndroid V${versionCode}"
    }
    override fun createVM(): AboutUsFragmentVM {
        return ViewModelProvider(this).get(AboutUsFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}