package com.gt.wan_gt.usage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.UsageBean
import com.gt.wan_gt.common.Constants
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_usage.*
import kotlinx.android.synthetic.main.item_tool_bar.*
import kotlin.random.Random

/**
 * time 2020/7/17 0017
 * author GT
 * 常用网站页面
 */
class UsageFragment:BaseFragment<UsageFragmentVM>() {
    private var usageList = ArrayList<UsageBean>()
    override fun setContentView() = R.layout.fragment_usage

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initToolbar()
        usage_flow.setOnTagClickListener { view, position, parent ->
            WebViewActivity.startWebView(requireActivity(),Bundle().apply {
                putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,false)
                putString(WebViewActivity.ACTIVITY_WEB_NAME,usageList[position].name!!)
                putString(WebViewActivity.ACTIVITY_LINK_URL,usageList[position].link!!)
            })
            true
        }
        viewModel.loadData()
    }

    /**
     * 初始化toolbar
     */
    private fun initToolbar(){
        common_tool_bar_back.setOnClickListener {
            pop()
        }
        common_tool_bar_title.text = "常用网站"
        common_tool_bar_usage.visibility = View.GONE
        common_tool_bar_search.visibility = View.GONE
    }

    override fun createVM(): UsageFragmentVM {
        return ViewModelProvider(this).get(UsageFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            usageData.observe(this@UsageFragment, Observer {
                usageList = it.any as ArrayList<UsageBean>
                usage_flow.adapter = object : TagAdapter<UsageBean>(usageList){
                    override fun getView(parent: FlowLayout?, position: Int, t: UsageBean?): View {
                        val tagView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.adapter_usage_tag,parent,false) as TextView
                        tagView.text = t?.name
                        tagView.setBackgroundColor(Constants.TAB_COLORS[Random.nextInt(Constants.TAB_COLORS.size-1)])
                        tagView.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                        return tagView
                    }
                }
            })
        }
    }
}