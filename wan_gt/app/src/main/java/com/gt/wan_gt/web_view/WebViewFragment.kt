package com.gt.wan_gt.web_view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.LEFT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.common.WanMMKV
import com.gt.wan_gt.common.WanSp
import com.gt.wan_gt.login.LoginFragment
import com.gt.wan_gt.utils.NetWorkUtil
import com.gt.wan_gt.utils.ToastUtil
import com.just.agentweb.AbsAgentWebSettings
import com.just.agentweb.AgentWeb
import com.just.agentweb.IAgentWebSettings
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.item_tool_bar.*


/**
 * time 2020/7/20 0020
 * author GT
 */
class WebViewFragment:BaseFragment<WebViewFragmentVM>() {
    companion object{
        const val IS_COMMON = "is_common"//是不是正常的文章
        const val LINK_URL = "link_url"//文章链接
        const val WEB_NAME = "web_name"//文章链接
        const val IS_LIKE = "is_like"//是否已经喜欢
        const val ARTICLE_ID = "article_id"//文章id
        const val COLLECT_RETURN = "collect_return"//收藏的状态
        fun getInstance(bundle:Bundle):WebViewFragment{
            val instance = WebViewFragment()
            instance.arguments = bundle
            return instance
        }
    }
    private lateinit var agentWeb: AgentWeb
    override fun setContentView() = R.layout.fragment_web_view

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        setSwipeBackEnable(true)
        initToolBar()
        initWebView()
    }

    /**
     * 初始化  web view
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){

        val url = arguments?.getString(LINK_URL)

        agentWeb = AgentWeb.with(this)
            .setAgentWebParent((web_view_content as FrameLayout), FrameLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
//            .setAgentWebWebSettings(getSettings())//4.1.2版本必须得设置 不然ready会报错
            .createAgentWeb()
            .ready()
            .go(url)
        val webView = agentWeb.webCreator.webView
        webView.settings.apply{
            blockNetworkImage = WanSp.getNoImage()

            if(WanSp.getAutoCache()){
                setAppCacheEnabled(true)//允许缓存
                domStorageEnabled = true//
                databaseEnabled = true

                cacheMode = if(NetWorkUtil.isNetConnect()){
                    WebSettings.LOAD_DEFAULT
                }else{
                    WebSettings.LOAD_CACHE_ELSE_NETWORK
                }
            }else{
                setAppCacheEnabled(false)//不允许缓存
                domStorageEnabled = false//
                databaseEnabled = false
                cacheMode = WebSettings.LOAD_NO_CACHE
            }
            javaScriptEnabled = true
            setSupportZoom(true)
            builtInZoomControls = true
            //不显示缩放按钮
            //不显示缩放按钮
            displayZoomControls = false
            //设置自适应屏幕，两者合用
            //将图片调整到适合WebView的大小
            //设置自适应屏幕，两者合用
            //将图片调整到适合WebView的大小
            useWideViewPort = true
            //缩放至屏幕的大小
            //缩放至屏幕的大小
            loadWithOverviewMode = true
            //自适应屏幕
            //自适应屏幕
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        }
    }

    private fun getSettings():IAgentWebSettings<WebSettings>{
        return object:AbsAgentWebSettings(){
            override fun bindAgentWebSupport(agentWeb: AgentWeb?) {

            }

        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        agentWeb.webLifeCycle.onDestroy()
    }
    /**
     * 初始化toolbar
     */
    private fun initToolBar(){
        val isCommon = arguments?.getBoolean(IS_COMMON)
        var isLike = arguments?.getBoolean(IS_LIKE)
        val webName = arguments?.getString(WEB_NAME)
        val articleId = arguments?.getInt(ARTICLE_ID)
        val popupWindow = PopupWindow(requireContext())
        val popView = LayoutInflater.from(requireContext()).inflate(R.layout.pop_share,null)
        popView.findViewById<TextView>(R.id.web_pop_share)
            .setOnClickListener {
            //分享
        }
       popView.findViewById<TextView>(R.id.web_pop_sys)
            .setOnClickListener {
            //使用系统浏览器打开
                val url = arguments?.getString(LINK_URL)
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                val contentUrl: Uri = Uri.parse(url)
                intent.data = contentUrl
                startActivity(Intent.createChooser(intent, "请选择浏览器"))
        }
        popupWindow.contentView = popView
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //设置宽高才能显示出来
        popupWindow.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        common_tool_bar_search.setOnClickListener {
            //显示分享的按钮
            popupWindow.showAsDropDown(common_tool_bar_search)
        }
        if(isCommon != null && isCommon){
            common_tool_bar_usage.visibility = View.VISIBLE
            if(isLike!!){
                common_tool_bar_usage.setImageResource(R.drawable.icon_like)
            }else{
                common_tool_bar_usage.setImageResource(R.drawable.icon_like_article_not_selected)
            }
            common_tool_bar_usage.setOnClickListener {
                if(!WanMMKV.getLoginState()){
                    start(LoginFragment())
                }else{
                    if(isLike!!){
                        viewModel.cancelCollect(articleId!!)

                    }else{
                        viewModel.addCollect(articleId!!)
                    }
                }
                isLike = !isLike!!
            }
        }else{
            common_tool_bar_usage.visibility = View.GONE
        }
        common_tool_bar_title.text = Html.fromHtml(webName)
        //将title 靠左显示
        val layoutParams = common_tool_bar_title.layoutParams as RelativeLayout.LayoutParams
        layoutParams.addRule(RelativeLayout.RIGHT_OF,R.id.common_tool_bar_back)
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutParams.marginStart = 10
        common_tool_bar_title.layoutParams = layoutParams
        common_tool_bar_back.setOnClickListener {
            pop()
        }
    }


    override fun createVM(): WebViewFragmentVM {
        return ViewModelProvider(this).get(WebViewFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@WebViewFragment, Observer {
                if(!it.flag) ToastUtil.showToast(it.any)
            })
            cancelResult.observe(this@WebViewFragment, Observer {
                if(it.flag){
                    common_tool_bar_usage.setImageResource(R.drawable.icon_like_article_not_selected)
                    setFragmentResult(100,Bundle().apply {
                        putBoolean(COLLECT_RETURN,false)
                    })
                }else{
                    ToastUtil.showToast(it.any)
                }
            })
            collectResult.observe(this@WebViewFragment, Observer {
                if(it.flag){
                    common_tool_bar_usage.setImageResource(R.drawable.icon_like)
                    setFragmentResult(100,Bundle().apply {
                        putBoolean(COLLECT_RETURN,true)
                    })
                }else{
                    ToastUtil.showToast(it.any)
                }
            })
        }
    }


}