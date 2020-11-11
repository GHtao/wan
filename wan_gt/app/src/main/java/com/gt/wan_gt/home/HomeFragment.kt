package com.gt.wan_gt.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.common.WanSp
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.common.dialog.loadDialog
import com.gt.wan_gt.common.event.EventBean
import com.gt.wan_gt.index.IndexFragment
import com.gt.wan_gt.knowledge_detail.KnowledgeDetailFragment
import com.gt.wan_gt.knowledge_detail.list.KnowledgeDetailListFragment
import com.gt.wan_gt.login.LoginFragment
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_home.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.support.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * time 2020/7/13 0013
 * author GT
 */
class HomeFragment:BaseFragment<HomeFragmentVM>(),IJumpToTop {

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var banner:Banner
    private var articleList = ArrayList<FeedArticleBean.FeedArticleData>()
    private lateinit var mParentFragment:SupportFragment
    private var clickItem = -1
    private var isLoadMore = false
    override fun setContentView() = R.layout.fragment_home

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        mParentFragment = parentFragment as SupportFragment
        initRefresh()
        initRecycler()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventMessage(bean:EventBean){
        when(bean.type){
            EventBean.Type.COLLECT->{
                if(clickItem < articleList.size){
                    articleList[clickItem].collect = bean.data as Boolean
                    homeAdapter.setData(clickItem,articleList[clickItem])
                }
            }
            else -> {

            }
        }
    }
    /**
     * 下拉刷新和加载更多
     */
    private fun initRefresh(){
        home_refresh.setOnRefreshListener {
            isLoadMore = false
            viewModel.loadData()
        }
        home_refresh.setOnLoadMoreListener {
            isLoadMore = true
            viewModel.loadMode()
        }
    }
    /**
     * 初始化列表
     */
    private fun initRecycler(){
        //1 自动登录一下
        //2 获取banner数据
        //3 回去文章列表
        homeAdapter = HomeAdapter()
        homeAdapter.setOnItemClickListener { adapter, view, position ->
            val article = articleList[position]
            clickItem = position
            WebViewActivity.startWebView(mParentFragment.requireActivity(),Bundle().apply {
                putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,true)
                putBoolean(WebViewActivity.ACTIVITY_IS_LIKE,article.collect)
                putString(WebViewActivity.ACTIVITY_WEB_NAME,article.title)
                putString(WebViewActivity.ACTIVITY_LINK_URL,article.link)
                putInt(WebViewActivity.ACTIVITY_ARTICLE_ID,article.id)
            },true)
        }
        homeAdapter.setOnItemChildClickListener { adapter, view, position ->
            when(view.id){
                R.id.home_item_like->{//喜欢
                    handleCollectEvent(position)
                }
                R.id.home_item_project->{//导航

                }
                R.id.home_item_title_end->{//项目分类
                    val article = articleList[position]
                    mParentFragment.start(KnowledgeDetailFragment.getInstance(Bundle().apply {
                        putBoolean(KnowledgeDetailFragment.SINGLE_CHEAPER,true)
                        putString(KnowledgeDetailFragment.SUPER_CHEAPER_NAME,article.superChapterName)
                        putInt(KnowledgeDetailFragment.SUPER_CHEAPER_ID,article.superChapterId)
                        putInt(KnowledgeDetailFragment.SINGLE_TAB_ID,article.chapterId)
                        putString(KnowledgeDetailFragment.SINGLE_TAB_NAME,article.chapterName)

                    }))
                }
            }
        }
        val bannerView:LinearLayout =
            LayoutInflater.from(requireContext()).inflate(R.layout.adapter_home_banner,null) as LinearLayout
        banner = bannerView.findViewById(R.id.home_banner)
        bannerView.removeView(banner)
        homeAdapter.addHeaderView(banner)
        home_recycler.apply {
            layoutManager = LinearLayoutManager(WanApp.instance)
            adapter = homeAdapter
        }
        isLoadMore = false
        home_refresh.autoRefresh()
    }

    /**
     * 处理收藏事件
     */
    private fun handleCollectEvent(position:Int){
        if(!WanSp.getLoginState()){
            (parentFragment as SupportFragment).start(LoginFragment())
            ToastUtil.showToast("请先登录")
            return
        }
        val article = articleList[position]
        if(!article.collect){
            viewModel.collectArticle(position,article)
        }else{
            viewModel.cancelCollectArticle(position,article)
        }
    }
    override fun createVM(): HomeFragmentVM {
        return ViewModelProvider(this).get(HomeFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            val loading = loadDialog(requireParentFragment().requireActivity(),"数据加载")
            isLoading.observe(this@HomeFragment, Observer {
                if(it.flag){
                    loading.show()
                }else{
                    if(loading.isShowing) loading.dismiss()
                    if(home_refresh.isRefreshing) home_refresh.finishRefresh()
                }
            })
            result.observe(this@HomeFragment,Observer{
                if(!it.flag){
                    ToastUtil.showToast(it.any.toString())
                    if(home_refresh.isRefreshing) home_refresh.finishRefresh()
                    if(home_refresh.isLoading) home_refresh.finishLoadMore()
                }

            })
            collectResult.observe(this@HomeFragment, Observer {
                if(it.flag){
                    val position = it.any
                    articleList[position!!].collect = true
                    homeAdapter.setData(position,articleList[position])
                }
            })
            unCollectResult.observe(this@HomeFragment, Observer {
                if(it.flag){
                    val position = it.any
                    articleList[position!!].collect = false
                    homeAdapter.setData(position,articleList[position])
                }
            })
            bannerData.observe(this@HomeFragment, Observer {
                if(it.flag){
                    val titles = ArrayList<String>()
                    val images = ArrayList<String>()
                    val bannerUrl = ArrayList<String>()
                    it.any?.map { bean->
                        titles.add(bean.title!!)
                        images.add(bean.imagePath!!)
                        bannerUrl.add(bean.url!!)
                    }
                    banner.apply {
                        setBannerTitles(titles)//设置标题
                        setImages(images)//设置图片
                        setImageLoader(object :ImageLoader(){//设置图片的加载器
                            override fun displayImage(
                                context: Context?,
                                path: Any?,
                                imageView: ImageView?) {
                                Glide.with(context!!).load(path!!).into(imageView!!)
                            }
                        })
                        setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)//设置样式 有title和数字指示器
                        setBannerAnimation(Transformer.ScaleInOut)//设置切换动画
                        isAutoPlay(true)//设置自动播放
                        setDelayTime(titles.size*400)//设置时长
                        setIndicatorGravity(BannerConfig.CENTER)//设置指示器位置（当banner模式中有指示器时）
                        setOnBannerListener {
                            //图片的点击操作
                            WebViewActivity.startWebView(mParentFragment.requireActivity(),Bundle().apply {
                                putBoolean(WebViewActivity.ACTIVITY_IS_COMMON,false)
                                putString(WebViewActivity.ACTIVITY_WEB_NAME,titles[it])
                                putString(WebViewActivity.ACTIVITY_LINK_URL,bannerUrl[it])
                            })
                        }
                        //配置完成的时候 启动
                        start()
                    }
                }
            })
            articleData.observe(this@HomeFragment, Observer {
                if(it.flag){
                    if(!isLoadMore){
                        articleList = it.any!!.datas!!
                        home_refresh.finishRefresh()
                    }else{
                        articleList.addAll(it.any!!.datas!!)
                        home_refresh.finishLoadMore()
                    }
                    homeAdapter.setList(articleList)
                }
            })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        Log.e("gt","home onDestroyView")
    }
    override fun jumpToTop() {
       home_recycler.smoothScrollToPosition(0)
    }
}