package com.gt.wan_gt.navigation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.api.bean.NavigationBean
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.views.NavRefreshView
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import kotlinx.android.synthetic.main.fragment_navigation.*
import me.yokeyword.fragmentation.support.SupportFragment

/**
 * time 2020/7/14 0014
 * author GT
 */
class NavigationFragment:BaseFragment<NavigationFragmentVM>() {
    private val leftAdapter = NavigationLeftAdapter()
    private val rightAdapter = NavigationRightAdapter()
    private val rightData = ArrayList<NavigationBean>()
    private var currPosition = 0//当前选择的标签
    override fun setContentView() = R.layout.fragment_navigation

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initLeft()
        initRight()
    }

    override fun onResume() {
        super.onResume()
        if(leftAdapter.itemCount <= 0) viewModel.loadData()
    }
    /**
     * 设置右面的view
     */
    private fun initRight(){
        rightAdapter.tagClickListener = object :NavigationRightAdapter.TagFlowClickListener{
            override fun tagClick(data: FeedArticleBean.FeedArticleData) {
                WebViewActivity.startWebView(parentFragment!!.requireActivity(),Bundle().apply {
                    putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,false)
                    putString(WebViewActivity.ACTIVITY_WEB_NAME,data.title!!)
                    putString(WebViewActivity.ACTIVITY_LINK_URL,data.link!!)
                })
            }

        }
        navigation_rv_right.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rightAdapter
        }
    }
    /**
     * 初始化左边的recycler
     */
    private fun initLeft(){
        var nextItem:NavigationBean? = null
        navigation_refresh_header.pullDownListener = object :NavRefreshView.PullDownListener{
            override fun pullDown() {
                Log.e("gt","header $this")
                val nextPosition = currPosition - 1
                if(nextPosition >= 0 && nextPosition<leftAdapter.itemCount){
                    nextItem = leftAdapter.getItem(nextPosition)
                    navigation_refresh_header.mTextView.text = "继续下拉浏览 ${nextItem?.name}"
                }else{
                    navigation_refresh_header.mTextView.text = "没有更多内容了"
                }
            }
        }
        navigation_refresh_footer.pullDownListener = object :NavRefreshView.PullDownListener{
            override fun pullDown() {
                Log.e("gt","footer $this")
                val nextPosition = currPosition + 1
                if(nextPosition >= 0 && nextPosition<leftAdapter.itemCount){
                    nextItem = leftAdapter.getItem(nextPosition)
                    navigation_refresh_footer.mTextView.text = "继续上拉浏览 ${nextItem?.name}"
                }else{
                    navigation_refresh_footer.mTextView.text = "没有更多内容了"
                }
            }
        }
        navigation_refresh.setOnRefreshListener {
            val nextPosition = currPosition - 1
            if(nextPosition >= 0 && nextPosition<leftAdapter.itemCount){
                leftAdapter.getItem(currPosition).isSelected = false
                leftAdapter.getItem(nextPosition).isSelected = true
                leftAdapter.notifyDataSetChanged()
                refreshRight(nextItem)
                currPosition = nextPosition
            }
            navigation_refresh.finishRefresh()
        }
        navigation_refresh.setOnLoadMoreListener {
            navigation_refresh.finishLoadMore()
            val nextPosition = currPosition + 1
            if(nextPosition >= 0 && nextPosition<leftAdapter.itemCount){
                leftAdapter.getItem(currPosition).isSelected = false
                leftAdapter.getItem(nextPosition).isSelected = true
                leftAdapter.notifyDataSetChanged()
                refreshRight(nextItem)
                currPosition = nextPosition
            }
        }
        leftAdapter.setOnItemClickListener { adapter, view, position ->
            currPosition = position
            for (i in 0 until leftAdapter.itemCount){
                leftAdapter.getItem(i).isSelected = position == i
            }
            leftAdapter.notifyDataSetChanged()

            refreshRight(leftAdapter.getItem(position))
        }
        navigation_rv_left.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = leftAdapter
        }
    }

    private fun refreshRight(bean:NavigationBean?){
        if(bean != null){
            rightData.clear()
            rightData.add(bean)
            rightAdapter.notifyItemChanged(0)
            navigation_rv_right.smoothScrollToPosition(0)
        }
    }
    override fun createVM(): NavigationFragmentVM {
        return ViewModelProvider(this).get(NavigationFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            navigationData.observe(this@NavigationFragment,Observer{
                if(it.size>0){
                    it[0].isSelected = true
                    rightData.clear()
                    rightData.add(it[0])
                    rightAdapter.setNewInstance(rightData)
                    leftAdapter.setNewInstance(it)
                }
            })
            result.observe(this@NavigationFragment, Observer {
                ToastUtil.showToast("获取导航数据失败:${it.any}")
            })
        }
    }
}