package com.gt.wan_gt.web_view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseActivity
import com.gt.wan_gt.R
import com.gt.wan_gt.common.WanSp
import com.gt.wan_gt.login.LoginFragment
import com.gt.wan_gt.utils.NetWorkUtil
import com.gt.wan_gt.utils.StatusBarUtil
import com.gt.wan_gt.utils.ToastUtil
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/8/4 0004
 * author GT
 * 新开一个进程来访问view 内容 减少一个进程内存占用太高 导致频繁gc
 */
class WebViewActivity:BaseActivity<WebViewActivityVM>() {
    companion object{
        const val ACTIVITY_BUNDLE = "activity_bundle"
        const val WEB_REQUEST_CODE = 100
        const val ACTIVITY_IS_COMMON = "is_common"//是不是正常的文章
        const val ACTIVITY_LINK_URL = "link_url"//文章链接
        const val ACTIVITY_WEB_NAME = "web_name"//文章链接
        const val ACTIVITY_IS_LIKE = "is_like"//是否已经喜欢
        const val ACTIVITY_ARTICLE_ID = "article_id"//文章id
        const val ACTIVITY_COLLECT_RETURN = "collect_return"//收藏的状态
        fun startWebView(context: Activity, bundle:Bundle,result:Boolean= false){
            val intent = Intent()
            intent.putExtra(ACTIVITY_BUNDLE,bundle)
            intent.component = ComponentName("com.gt.wan_gt","com.gt.wan_gt.web_view.WebViewActivity")
            if(result){
                context.startActivityForResult(intent,WEB_REQUEST_CODE)
            }else{
                context.startActivity(intent)
            }
        }

    }
    private lateinit var agentWeb: AgentWeb
    override fun contentView(savedInstanceState: Bundle?) = R.layout.fragment_web_view

    override fun initData(savedInstanceState: Bundle?) {
        Log.e("gt","task web:${taskId}")
        setSwipeBackEnable(true)
        StatusBarUtil.setStatusColor(window,
            ContextCompat.getColor(this, R.color.main_status_bar_blue),
            1f)
        val bundle = intent.getBundleExtra(ACTIVITY_BUNDLE)
        initToolBar(bundle)
        initWebView(bundle)
    }

    override fun createVM(): WebViewActivityVM {
        return ViewModelProvider(this)[WebViewActivityVM::class.java]
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@WebViewActivity, Observer {
                if(!it.flag) ToastUtil.showToast(it.any)
            })
            cancelResult.observe(this@WebViewActivity, Observer {
                if(it.flag){
                    common_tool_bar_usage.setImageResource(R.drawable.icon_like_article_not_selected)
                    setResult(WEB_REQUEST_CODE,Intent().apply {
                        putExtra(ACTIVITY_COLLECT_RETURN,false)
                    })
                }else{
                    ToastUtil.showToast(it.any)
                }
            })
            collectResult.observe(this@WebViewActivity, Observer {
                if(it.flag){
                    common_tool_bar_usage.setImageResource(R.drawable.icon_like)
                    setResult(WEB_REQUEST_CODE,Intent().apply {
                        putExtra(ACTIVITY_COLLECT_RETURN,true)
                    })
                }else{
                    ToastUtil.showToast(it.any)
                }
            })
        }
    }

    /**
     * 初始化toolbae
     */
    private fun initToolBar(bundle: Bundle){
        val isCommon = bundle.getBoolean(ACTIVITY_IS_COMMON)
        var isLike = bundle.getBoolean(ACTIVITY_IS_LIKE)
        val webName = bundle.getString(ACTIVITY_WEB_NAME)
        val articleId = bundle.getInt(ACTIVITY_ARTICLE_ID)
        val popupWindow = PopupWindow(this)
        val popView = LayoutInflater.from(this).inflate(R.layout.pop_share,null)
        popView.findViewById<TextView>(R.id.web_pop_share)
            .setOnClickListener {
                //分享
            }
        popView.findViewById<TextView>(R.id.web_pop_sys)
            .setOnClickListener {
                //使用系统浏览器打开
                val url = bundle.getString(ACTIVITY_LINK_URL)
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
        if(isCommon){
            common_tool_bar_usage.visibility = View.VISIBLE
            if(isLike){
                common_tool_bar_usage.setImageResource(R.drawable.icon_like)
            }else{
                common_tool_bar_usage.setImageResource(R.drawable.icon_like_article_not_selected)
            }
            common_tool_bar_usage.setOnClickListener {
                if(!WanSp.getLoginState()){
                    start(LoginFragment())
                }else{
                    if(isLike){
                        viewModel.cancelCollect(articleId)
                    }else{
                        viewModel.addCollect(articleId)
                    }
                }
                isLike = !isLike
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
            finish()
        }
    }

    /**
     * 初始化web view
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(bundle: Bundle){

        val url = bundle.getString(ACTIVITY_LINK_URL)

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
            //允许js交互
            javaScriptEnabled = true
            //允许js弹窗
            javaScriptCanOpenWindowsAutomatically = true
            //调用js中的方法
//            webView.loadUrl("javascript:call()")
            //该方法比第一种方法效率更高、使用更简洁。
            //因为该方法的执行不会使页面刷新，而第一种方法（loadUrl ）的执行则会。
//            webView.evaluateJavascript(""){
//            }

            //js 通过toJs来调用test方法
            webView.addJavascriptInterface(object:Any(){
                @JavascriptInterface
                fun test(){
                    Log.e("gt","js test")
                }
            },"toJs")
        }
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

}