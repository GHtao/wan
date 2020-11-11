package com.gt.wan_gt.index

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.RotateAnimation
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.about_us.AboutUsFragment
import com.gt.wan_gt.collect.CollectFragment
import com.gt.wan_gt.common.WanSp
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.common.dialog.loadDialog
import com.gt.wan_gt.home.HomeFragment
import com.gt.wan_gt.knowledge_hierarchy.KnowledgeHierarchyFragment
import com.gt.wan_gt.login.LoginFragment
import com.gt.wan_gt.main.MainActivity
import com.gt.wan_gt.navigation.NavigationFragment
import com.gt.wan_gt.project.ProjectFragment
import com.gt.wan_gt.search.SearchFragment
import com.gt.wan_gt.setting.SettingFragment
import com.gt.wan_gt.usage.UsageFragment
import com.gt.wan_gt.utils.ToastUtil
import com.gt.wan_gt.views.AsyncAnimHelper
import com.gt.wan_gt.web_view.WebViewActivity
import com.gt.wan_gt.web_view.WebViewFragment
import com.gt.wan_gt.wx.WxArticleFragment
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.fragment_index.*
import kotlinx.android.synthetic.main.item_tool_bar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * time 2020/7/14 0014
 * author GT
 */
class IndexFragment:BaseFragment<IndexFragmentVM>() {
    private val fragments = ArrayList<Fragment>()
    private lateinit var mainActivity: MainActivity
    private var currFragmentPosition = -1
    override fun setContentView() = R.layout.fragment_index

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        //在activity或者fragment中用lifecycleScope 代替GlobalScope
        lifecycleScope.launch {

        }
        mainActivity = activity as MainActivity
        initFragments()
        initToolBar()
        initViewPager()
        initBottomNav()
        initDrawerMenu()
        initFloatButton()
    }

    /**
     * 测试异步动画
     */
    private var rotation = 0f
    private fun testAsyncAnimator(){
        rotation += 180f
        val animator = index_float_up.animate().rotation(rotation).setDuration(2000)
        AsyncAnimHelper.onStartBefore(animator,index_float_up)
        animator.start()
        Log.e("gt","animator start")
        Handler().postDelayed({//主线程1s之后卡顿1s
            Log.e("gt","main sleep:${Thread.currentThread().name}")
            Thread.sleep(1000)
        },1000)
    }

    /**
     * 初始化float button
     */
    private fun initFloatButton(){
        index_float_up.setOnClickListener {
            val rotateAnimation = RotateAnimation(0f,180f)
            rotateAnimation.duration = 2000
            index_float_up.startAnimation(rotateAnimation)

            fragments[currFragmentPosition].apply {
                if(this is IJumpToTop){
                    jumpToTop()
                }
            }
        }
    }
    override fun onSupportVisible() {
        super.onSupportVisible()
        val item = index_nav_view.menu.findItem(R.id.nav_menu_logout)
        val loginBtn = index_nav_view.getHeaderView(0)
            .findViewById<Button>(R.id.nav_head_login)
        if(WanSp.getLoginState()){
            loginBtn.text = WanSp.getLoginAccount()
            loginBtn.setOnClickListener(null)
            item.isVisible = true
        }else{
            loginBtn.text = "登录"
            loginBtn.setOnClickListener {//登录
                    mainActivity.startFragment(LoginFragment())
                }
            item.isVisible = false
        }
    }
    /**
     * 初始化侧边栏
     */
    private fun initDrawerMenu(){
        index_nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_menu_home->{//首页

                }
                R.id.nav_menu_collect->{//收藏
                    start(CollectFragment())
                }
                R.id.nav_menu_setting->{//设置
                    start(SettingFragment())
                }
                R.id.nav_menu_about->{//关于
                    start(AboutUsFragment())
                }
                R.id.nav_menu_logout->{//退出登录
                    viewModel.logout()
                }
            }
            true
        }
    }
    /**
     * 初始化底部按钮
     */
    private fun initBottomNav(){
        common_tool_bar_title.text = "首页"
        //防止三个以上的项目 默认不显示文字
        index_bottom_nav.labelVisibilityMode =  LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        index_bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottom_nav_home->{
                    common_tool_bar_title.text = "首页"
                    index_view_pager.currentItem = 0
                }
                R.id.bottom_nav_knowledge->{
                    common_tool_bar_title.text = "知识体系"
                    index_view_pager.currentItem = 1
                }
                R.id.bottom_nav_wx->{
                    common_tool_bar_title.text = "公众号"
                    index_view_pager.currentItem = 2
                }
                R.id.bottom_nav_navigation->{
                    common_tool_bar_title.text = "导航"
                    index_view_pager.currentItem = 3
                }
                R.id.bottom_nav_project->{
                    common_tool_bar_title.text = "项目"
                    index_view_pager.currentItem = 4
                }
            }
            true
        }
    }
    /**
     * 初始化fragment
     */
    private fun initFragments(){
        fragments.clear()
        fragments.add(HomeFragment())
        fragments.add(KnowledgeHierarchyFragment())
        fragments.add(WxArticleFragment())
        fragments.add(NavigationFragment())
        fragments.add(ProjectFragment())
    }
    /**
     * 初始化view pager
     */
    private fun initViewPager() {
        index_view_pager.isUserInputEnabled = false
        index_view_pager.adapter = IndexViewPageAdapter(this)
        index_view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                currFragmentPosition = position
                super.onPageSelected(position)
            }
        })
    }
    /**
     * 初始化toolbar
     */
    private fun initToolBar(){
        common_tool_bar_back.setImageResource(R.mipmap.logo)
        common_tool_bar_back.setOnClickListener {
            index_drawer.openDrawer(index_nav_view)
        }
        common_tool_bar_usage.setOnClickListener {
            start(UsageFragment())
        }
        common_tool_bar_search.setOnClickListener {
            start(SearchFragment())
        }
    }

    override fun onBackPressedSupport(): Boolean {
        if(index_drawer.isDrawerOpen(index_nav_view)){
            index_drawer.closeDrawer(index_nav_view)
            return true
        }
        return super.onBackPressedSupport()
    }

    override fun createVM(): IndexFragmentVM {
        return ViewModelProvider(this).get(IndexFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            val loadDialog = loadDialog(requireActivity(),"")
            isLoading.observe(this@IndexFragment, Observer {
                if(it.flag){
                    loadDialog.show()
                }else{
                    loadDialog.dismiss()
                }
            })
            result.observe(this@IndexFragment, Observer {
                if(it.flag){
                    mainActivity.startFragment(LoginFragment())
                }else{
                    ToastUtil.showToast("退出失败:${it.any}")
                }
            })
        }
    }
    /**
     * view pager adapter
     */
    inner class IndexViewPageAdapter(fragment:Fragment):FragmentStateAdapter(fragment){
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}