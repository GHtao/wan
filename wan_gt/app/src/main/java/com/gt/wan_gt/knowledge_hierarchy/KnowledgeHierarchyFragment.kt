package com.gt.wan_gt.knowledge_hierarchy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.KnowledgeBean
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.index.IndexFragment
import com.gt.wan_gt.knowledge_detail.KnowledgeDetailFragment
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_knowledge.*
import kotlinx.android.synthetic.main.item_tool_bar.*
import java.io.Serializable

/**
 * time 2020/7/14 0014
 * author GT
 * 底部tab标签的 知识体系页面
 */
class KnowledgeHierarchyFragment:BaseFragment<KnowledgeHierarchyFragmentVM>(),IJumpToTop {
    private lateinit var mParentFragment:IndexFragment
    private lateinit var knowledgeAdapter: KnowledgeAdapter
    private var isLoadData = false
    private var isInit = false
    override fun setContentView() = R.layout.fragment_knowledge

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        mParentFragment = parentFragment as IndexFragment
        initRecycler()

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
    }
    /**
     * view pager2 直接在on resume中加载数据就行
     */
    override fun onResume() {
        super.onResume()
        if(!isInit){
            isInit = true
            knowledge_refresh.autoRefresh()
        }
    }

    /**
     * 初始化recycler
     */
    private fun initRecycler(){
        knowledgeAdapter = KnowledgeAdapter()
        knowledgeAdapter.setOnItemClickListener { adapter, view, position ->
            mParentFragment.start(KnowledgeDetailFragment.getInstance(Bundle().apply {
                putSerializable(KnowledgeDetailFragment.NORMAL_TAB,
                    adapter.data[position] as KnowledgeBean
                )
            }))
        }
        knowledge_refresh.setOnRefreshListener {
            viewModel.loadData()
        }
        knowledge_refresh.setOnLoadMoreListener {
            isLoadData = true
            viewModel.loadData()
        }
        knowledge_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = knowledgeAdapter
        }

    }


    override fun createVM(): KnowledgeHierarchyFragmentVM {
        return ViewModelProvider(this).get(KnowledgeHierarchyFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@KnowledgeHierarchyFragment, Observer {
                if(knowledge_refresh.isRefreshing) knowledge_refresh.finishRefresh()
                if(knowledge_refresh.isLoading) knowledge_refresh.finishLoadMore()
                ToastUtil.showToast(it.any)
            })
            knowledgeData.observe(this@KnowledgeHierarchyFragment, Observer {
                if(isLoadData && knowledgeAdapter.itemCount == it.size){
                    isLoadData = false
                    ToastUtil.showToast("没有更多的数据了")
                }
                if(it.size>0 && knowledgeAdapter.itemCount < it.size){
                    knowledgeAdapter.setList(it)
                }
                if(knowledge_refresh.isRefreshing) knowledge_refresh.finishRefresh()
                if(knowledge_refresh.isLoading) knowledge_refresh.finishLoadMore()
            })
        }
    }

    override fun jumpToTop() {
        knowledge_recycler.smoothScrollToPosition(0)
    }
}