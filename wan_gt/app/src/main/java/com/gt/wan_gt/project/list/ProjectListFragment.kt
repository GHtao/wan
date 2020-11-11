package com.gt.wan_gt.project.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import kotlinx.android.synthetic.main.fragment_project_list.*
import me.yokeyword.fragmentation.support.SupportFragment

/**
 * time 2020/7/30 0030
 * author GT
 */
class ProjectListFragment:BaseFragment<ProjectListFragmentVM>(),IJumpToTop {
    private val projectListAdapter = ProjectListAdapter()

    companion object{
        const val CID = "cid"
        fun getInstance(bundle: Bundle):ProjectListFragment{
            val instance = ProjectListFragment()
            instance.arguments = bundle
            return instance
        }
    }
    override fun setContentView() = R.layout.fragment_project_list

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initRecycler()
    }

    /**
     * 初始化recycler
     */
    private fun initRecycler(){
        projectListAdapter.setOnItemClickListener { adapter, view, position ->
            val article = projectListAdapter.getItem(position)
            WebViewActivity.startWebView(requireParentFragment().requireParentFragment()
                .requireActivity(),Bundle().apply {
                putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,false)
                putString(WebViewActivity.ACTIVITY_WEB_NAME,article.title)
                putString(WebViewActivity.ACTIVITY_LINK_URL,article.link)
                putInt(WebViewActivity.ACTIVITY_ARTICLE_ID,article.id)
            })
        }
        project_list_recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = projectListAdapter
            setHasFixedSize(true)
            addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if(newState == SCROLL_STATE_SETTLING){
                        Glide.with(this@ProjectListFragment).pauseRequests()
                    }else{
                        Glide.with(this@ProjectListFragment).resumeRequests()
                    }
                }
            })
        }
    }
    override fun onResume() {
        super.onResume()
        val cid = arguments?.getInt(CID)
        if(cid != null){
            viewModel.loadProjectListData(0,cid)
        }
    }
    override fun createVM(): ProjectListFragmentVM {
        return ViewModelProvider(this).get(ProjectListFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@ProjectListFragment, Observer {
                ToastUtil.showToast(it.any)
            })
            projectListData.observe(this@ProjectListFragment,Observer{
                projectListAdapter.setNewInstance(it)
            })
        }
    }

    override fun jumpToTop() {
        project_list_recycler.smoothScrollToPosition(0)
    }
}