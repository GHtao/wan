package com.gt.wan_gt.wx

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.WxArticleBean
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.wx.list.WxArticleListFragment
import kotlinx.android.synthetic.main.fragment_wx_article.*

/**
 * time 2020/7/30 0030
 * author GT
 */
class WxArticleFragment:BaseFragment<WxArticleFragmentVM>(),IJumpToTop {
    private val fragments = ArrayList<WxArticleListFragment>()
    override fun setContentView() = R.layout.fragment_wx_article

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun createVM(): WxArticleFragmentVM {
        return ViewModelProvider(this).get(WxArticleFragmentVM::class.java)
    }

    /**
     * 初始化view pager
     */
    private fun initViewPager(data:ArrayList<WxArticleBean>){
        wx_article_vp.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        TabLayoutMediator(wx_article_tab,wx_article_vp,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            tab.text = data[position].name
        }).attach()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loaWxdData()
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@WxArticleFragment, Observer {
                ToastUtil.showToast(it.any)
            })
            wxArticleData.observe(this@WxArticleFragment, Observer {
                fragments.clear()
                it.forEach {
                    fragments.add(WxArticleListFragment.getInstance(Bundle().apply {
                        putInt(WxArticleListFragment.ID,it.id)
                        putString(WxArticleListFragment.NAME,it.name)
                    }))
                }
                initViewPager(it)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragments.clear()
    }
    override fun jumpToTop() {
        val currentItem = wx_article_vp.currentItem
        if(fragments.size > 0 && currentItem < fragments.size){
            fragments[currentItem].jumpToTop()
        }
    }
}