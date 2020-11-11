package com.gt.wan_gt.knowledge_detail.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.home.HomeAdapter
import com.gt.wan_gt.home.HomeFragment
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import kotlinx.android.synthetic.main.fragment_knowledge.*
import kotlinx.android.synthetic.main.fragment_knowledge_detail_list.*
import me.yokeyword.fragmentation.support.SupportFragment

/**
 * time 2020/7/28 0028
 * author GT
 */
class KnowledgeDetailListFragment:BaseFragment<KnowledgeDetailListFragmentVM>() {
//    private val mListData = ArrayList<FeedArticleBean.FeedArticleData>()
    private var currPage = 0
    private var isLoadMore = false
    private var cid = 0
    private var listAdapter:HomeAdapter? = null
    private var mParentFragment:SupportFragment? = null
    companion object{
        const val CID = "cid"
        fun getInstance(bundle: Bundle):KnowledgeDetailListFragment{
            val instance = KnowledgeDetailListFragment()
            instance.arguments = bundle
            return instance
        }
    }
    override fun setContentView() = R.layout.fragment_knowledge_detail_list

    override fun initView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        mParentFragment = parentFragment as SupportFragment
        initRecycler()
        cid = arguments?.getInt(CID,0) ?: 0
        currPage = 0
        viewModel.loadData(currPage,cid)
    }
    /**
     * 初始化列表
     */
    private fun initRecycler(){
        listAdapter = HomeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val article = adapter.data[position] as FeedArticleBean.FeedArticleData
                WebViewActivity.startWebView(mParentFragment!!.requireActivity(),Bundle().apply {
                    putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,true)
                    putBoolean(WebViewActivity.ACTIVITY_IS_LIKE,article.collect)
                    putString(WebViewActivity.ACTIVITY_WEB_NAME,article.title)
                    putString(WebViewActivity.ACTIVITY_LINK_URL,article.link)
                    putInt(WebViewActivity.ACTIVITY_ARTICLE_ID,article.id)
                })
            }
        }
        knowledge_detail_list_refresh.setPrimaryColorsId(R.color.colorPrimary,R.color.white)
        knowledge_detail_list_refresh.setOnRefreshListener {
            isLoadMore = false
            currPage =0
            viewModel.loadData(currPage,cid)
        }
        knowledge_detail_list_refresh.setOnLoadMoreListener {
            isLoadMore = true
            currPage++
            viewModel.loadData(currPage,cid)
        }
        knowledge_detail_list_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }
    }

    override fun createVM(): KnowledgeDetailListFragmentVM {
        return ViewModelProvider(this).get(KnowledgeDetailListFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            listData.observe(this@KnowledgeDetailListFragment,Observer{
                if(isLoadMore){
                    isLoadMore = false
                    listAdapter?.addData(it)
                    if(knowledge_detail_list_refresh.isLoading) knowledge_detail_list_refresh.finishLoadMore()
                }else{
                    listAdapter?.setNewInstance(it)
                    if(knowledge_detail_list_refresh.isRefreshing) knowledge_detail_list_refresh.finishRefresh()
                }
            })
            result.observe(this@KnowledgeDetailListFragment, Observer {
                if(knowledge_detail_list_refresh.isLoading) knowledge_detail_list_refresh.finishLoadMore()
                if(knowledge_detail_list_refresh.isRefreshing) knowledge_detail_list_refresh.finishRefresh()
                ToastUtil.showToast(it.any)
            })
        }
    }
}