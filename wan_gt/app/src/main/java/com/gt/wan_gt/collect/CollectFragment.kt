package com.gt.wan_gt.collect

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.home.HomeAdapter
import com.gt.wan_gt.search.SearchFragment
import com.gt.wan_gt.usage.UsageFragment
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_collect.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/8/3 0003
 * author GT
 * 收藏页面
 */
class CollectFragment:BaseFragment<CollectFragmentVM>() {
    private var currentPage = 0
    private var isLoadMore = false
    private var mAdapter = HomeAdapter()
    override fun setContentView() = R.layout.fragment_collect

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initToolbar()
        initRefresh()
    }

    /**
     * 初始化列表
     */
    private fun initRefresh(){
        collect_refresh.setOnRefreshListener {
            isLoadMore = false
            currentPage = 0
            viewModel.getCollectData(currentPage)
        }
        collect_refresh.setOnLoadMoreListener {
            isLoadMore = true
            currentPage++
            viewModel.getCollectData(currentPage)
        }
        mAdapter.isCollectPage = true
        collect_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            setHasFixedSize(true)
        }
        collect_refresh.autoRefresh()
    }
    /**
     * 初始化toolbar
     */
    private fun initToolbar(){
        common_tool_bar_title.text = "收藏"
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
    override fun createVM(): CollectFragmentVM {
        return ViewModelProvider(this).get(CollectFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@CollectFragment, Observer {
                ToastUtil.showToast(it.any)
                if(collect_refresh.isRefreshing) collect_refresh.finishRefresh()
                if(collect_refresh.isLoading) collect_refresh.finishLoadMore()
            })

            collectData.observe(this@CollectFragment, Observer {
                if(isLoadMore){
                    if(it.size > 0){
                        mAdapter.addData(it)
                    }else{
                        ToastUtil.showToast("没有更多数据了")
                    }
                }else{
                    mAdapter.setNewInstance(it)
                }
                if(collect_refresh.isRefreshing) collect_refresh.finishRefresh()
                if(collect_refresh.isLoading) collect_refresh.finishLoadMore()
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("gt","collect onDestroyView $view")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("gt","collect onDestroy  $view")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("gt","collect onDetach  $view")
    }
}
