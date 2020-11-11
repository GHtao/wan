package com.gt.wan_gt.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.HotSearchBean
import com.gt.wan_gt.common.Constants
import com.gt.wan_gt.search_list.SearchListFragment
import com.gt.wan_gt.utils.ToastUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import kotlin.random.Random

/**
 * time 2020/7/23 0023
 * author GT
 */
class SearchFragment:BaseFragment<SearchFragmentVM>() {
    private var hotSearchList = ArrayList<HotSearchBean>()
    private lateinit var searchAdapter:SearchAdapter
    override fun setContentView() = R.layout.fragment_search

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initSearchTitle()
        initHistoryView()
        viewModel.loadData()
    }

    /**
     * 历史数据的view
     */
    private fun initHistoryView(){
        searchAdapter = SearchAdapter()
        searchAdapter.setOnItemClickListener { adapter, view, position ->
            val item = searchAdapter.getItem(position)
            search_et_content.setText(item.data)
            viewModel.addHistory(item.data!!)
            start(SearchListFragment.instance(item.data!!))
        }
        search_lv_history.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
        search_clear.setOnClickListener {
            if(searchAdapter.itemCount>0){
                viewModel.clearHistory()
                searchAdapter.setList(null)
            }
        }
    }
    /**
     * 初始化搜索栏
     */
    private  fun initSearchTitle(){
        search_back.setOnClickListener {
            pop()
        }
        search_bt_go.setOnClickListener {
            //搜索
            val data = search_et_content.text.toString().trim()
            if(TextUtils.isEmpty(data)){
                ToastUtil.showToast("搜索内容为空")
                return@setOnClickListener
            }
            viewModel.addHistory(data)
            start(SearchListFragment.instance(data))
        }
    }
    override fun createVM(): SearchFragmentVM {
        return ViewModelProvider(this).get(SearchFragmentVM::class.java)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        viewModel.allHistoryData()
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@SearchFragment, Observer {
                if(!it.flag){
                    ToastUtil.showToast(it.any)
                }
            })
            //热搜
            hotSearchData.observe(this@SearchFragment, Observer {
                hotSearchList = it as ArrayList<HotSearchBean>
                search_tag_flow.adapter = object : TagAdapter<HotSearchBean>(hotSearchList){
                    override fun getView(
                        parent: FlowLayout?,
                        position: Int,
                        t: HotSearchBean?): View {
                        val tagView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.adapter_usage_tag,parent,false) as TextView
                        tagView.apply {
                            text = t?.name
                            setBackgroundColor(Constants.TAB_COLORS[Random.nextInt(Constants.TAB_COLORS.size-1)])
                            setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                        }
                        search_tag_flow.setOnTagClickListener { view, position, parent ->
                            val searchBean = hotSearchList[position]
                            search_et_content.setText(searchBean.name)
                            viewModel.addHistory(searchBean.name!!)
                            start(SearchListFragment.instance(searchBean.name!!))
                            true
                        }
                        return tagView
                    }
                }
            })
            //历史数据
            historyData.observe(this@SearchFragment, Observer {
                searchAdapter.setList(it)
            })
        }
    }
}