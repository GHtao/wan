package com.gt.wan_gt.wx.list

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.home.HomeAdapter
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.web_view.WebViewActivity
import kotlinx.android.synthetic.main.fragment_wx_article_list.*
import me.yokeyword.fragmentation.support.SupportFragment

/**
 * time 2020/7/30 0030
 * author GT
 */
class WxArticleListFragment:BaseFragment<WxArticleListFragmentVM>(),IJumpToTop {
    private var currPage = 0
    private val mAdapter = HomeAdapter()
    private var articleId = 0
    private var isLoadMore = false
    private var mSuperParent:SupportFragment? = null
    companion object{
        const val ID = "id"
        const val NAME = "name"
        fun getInstance(bundle: Bundle):WxArticleListFragment{
            val instance = WxArticleListFragment()
            instance.arguments = bundle
            return instance
        }
    }
    override fun setContentView() = R.layout.fragment_wx_article_list

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        mSuperParent = parentFragment?.parentFragment as SupportFragment
        initSearch()
        initViewpager()
    }

    /**
     * 初始化view pager
     */
    private fun initViewpager(){
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val article = mAdapter.getItem(position)
            WebViewActivity.startWebView(mSuperParent!!.requireActivity(),Bundle().apply {
                putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,true)
                putBoolean(WebViewActivity.ACTIVITY_IS_LIKE,article.collect)
                putString(WebViewActivity.ACTIVITY_WEB_NAME,article.title)
                putString(WebViewActivity.ACTIVITY_LINK_URL,article.link)
                putInt(WebViewActivity.ACTIVITY_ARTICLE_ID,article.id)
            })
        }
        wx_article_list_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
            setHasFixedSize(true)
        }

        wx_article_list_refresh.setOnRefreshListener {
            isLoadMore = false
            currPage = 0
            viewModel.loadWxArticleList(articleId,currPage)
        }
        wx_article_list_refresh.setOnLoadMoreListener {
            isLoadMore = true
            currPage++
            viewModel.loadWxArticleList(articleId,currPage)
        }
    }
    /**
     * 初始化搜索数据
     */
    private fun initSearch(){

        wx_article_list_search_bt.setOnClickListener {
            viewModel.getSearchData(articleId,currPage,wx_article_list_search.text.toString().trim())
        }
    }
    override fun onResume() {
        super.onResume()
        articleId = requireArguments().getInt(ID)
        viewModel.loadWxArticleList(articleId,currPage)

    }

    override fun createVM(): WxArticleListFragmentVM {
        return ViewModelProvider(this).get(WxArticleListFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@WxArticleListFragment,Observer{
                ToastUtil.showToast(it.any)
                if(wx_article_list_refresh.isRefreshing) wx_article_list_refresh.finishRefresh()
                if(wx_article_list_refresh.isLoading) wx_article_list_refresh.finishLoadMore()
            })
            wxArticleList.observe(this@WxArticleListFragment, Observer {
                if(isLoadMore){
                    isLoadMore = false
                    mAdapter.addData(it)
                }else{
                    mAdapter.setNewInstance(it)
                }
                if(wx_article_list_refresh.isRefreshing) wx_article_list_refresh.finishRefresh()
                if(wx_article_list_refresh.isLoading) wx_article_list_refresh.finishLoadMore()
            })
        }
    }

    override fun jumpToTop() {
        wx_article_list_recycler.smoothScrollToPosition(0)
    }
}