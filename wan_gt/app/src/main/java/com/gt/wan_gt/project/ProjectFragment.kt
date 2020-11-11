package com.gt.wan_gt.project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.ProjectBean
import com.gt.wan_gt.common.interfaces.IJumpToTop
import com.gt.wan_gt.project.list.ProjectListFragment
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_project.*
import me.yokeyword.fragmentation.support.SupportFragment

/**
 * time 2020/7/14 0014
 * author GT
 */
class ProjectFragment:BaseFragment<ProjectFragmentVM>(),IJumpToTop {
    private val fragments = ArrayList<ProjectListFragment>()
    private val mData = ArrayList<ProjectBean>()
    override fun setContentView() = R.layout.fragment_project

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        Log.e("gt","project initView")
    }

    private fun initViewPager(){
        project_vp.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }
        }
        TabLayoutMediator(project_tab,project_vp,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = mData[position].name
            }).attach()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    override fun createVM(): ProjectFragmentVM {
       return ViewModelProvider(this).get(ProjectFragmentVM::class.java)
    }

    override fun onDestroyView() {
        Log.e("gt","project onDestroyView")
        super.onDestroyView()
        fragments.clear()
    }
    override fun observe() {
        viewModel.apply {
            projectData.observe(this@ProjectFragment,Observer{
                fragments.clear()
                mData.clear()
                it.forEach {
                    fragments.add(ProjectListFragment.getInstance(Bundle().apply {
                        putInt(ProjectListFragment.CID,it.id)
                    }))
                }
                mData.addAll(it)
                initViewPager()
            })
            result.observe(this@ProjectFragment, Observer {
                ToastUtil.showToast(it.any)
            })
        }
    }

    override fun jumpToTop() {
        val currentItem = project_vp.currentItem
        if(fragments.size >0 && currentItem<fragments.size)
            fragments[project_vp.currentItem].jumpToTop()
    }

}