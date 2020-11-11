package com.gt.wan_gt.knowledge_detail

import android.os.Bundle
import android.view.FrameMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.KnowledgeBean
import com.gt.wan_gt.knowledge_detail.list.KnowledgeDetailListFragment
import com.gt.wan_gt.knowledge_detail.list.KnowledgeDetailListFragment.Companion.CID
import kotlinx.android.synthetic.main.fragment_knowledge_detail.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/7/27 0027
 * author GT
 * 知识体系tab页 有可能是一个tab 有可能是多个tab
 */
class KnowledgeDetailFragment:BaseFragment<KnowledgeDetailVM>() {

    companion object{
        const val SINGLE_CHEAPER = "is_single"
        const val SUPER_CHEAPER_NAME = "super_name"
        const val SUPER_CHEAPER_ID = "super_name_id"
        const val SINGLE_TAB_NAME = "single_tab_name"
        const val SINGLE_TAB_ID = "single_tab_id"
        const val NORMAL_TAB = "normal_tab"
        fun getInstance(bundle:Bundle):KnowledgeDetailFragment{
            val instance = KnowledgeDetailFragment()
            instance.arguments = bundle
            return instance
        }
    }
    private var isSingle = false
    private var knowledgeBean:KnowledgeBean? = null

    private var title:String? = ""
    private val fragments = ArrayList<Fragment>()
    override fun setContentView() = R.layout.fragment_knowledge_detail

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        isSingle = arguments?.getBoolean(SINGLE_CHEAPER,false) ?: false
        if(!isSingle){
            knowledgeBean = arguments?.getSerializable(NORMAL_TAB) as KnowledgeBean
        }
        initToolbar()
        initViewPager()
    }

    /**
     * 初始化tool bar
     */
    private fun initToolbar(){
        title = if(isSingle){
            arguments?.getString(SUPER_CHEAPER_NAME)
        }else{
            knowledgeBean?.name
        }
        common_tool_bar_back.setOnClickListener {
            pop()
        }
        common_tool_bar_usage.visibility = View.GONE
        common_tool_bar_search.visibility = View.GONE
        common_tool_bar_title.text = title
    }

    /**
     * 初始化view pager
     */
    private fun initViewPager(){
        fragments.clear()
        if(isSingle){
            fragments.add(KnowledgeDetailListFragment.getInstance(Bundle().apply {
                putInt(CID,arguments?.getInt(SINGLE_TAB_ID,0) ?: 0)
            }))
        }else{
            knowledgeBean?.children?.forEach {
                fragments.add(KnowledgeDetailListFragment.getInstance(Bundle().apply {
                    putInt(CID,it.id)
                }))
            }
        }
        knowledge_detail_vp.offscreenPageLimit = 1
        knowledge_detail_vp.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        TabLayoutMediator(knowledge_detail_tab,knowledge_detail_vp,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                if(isSingle){
                    tab.text = arguments?.getString(SINGLE_TAB_NAME,"aa")!!
                }else{
                    tab.text = knowledgeBean?.children?.get(position)?.name
                }
            }).attach()
    }
    override fun createVM(): KnowledgeDetailVM {
        return ViewModelProvider(this).get(KnowledgeDetailVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}