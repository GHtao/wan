package com.gt.wan_gt.search_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R

/**
 * time 2020/7/24 0024
 * author GT
 */
class SearchListFragment:BaseFragment<SearchListFragmentVM>() {

    companion object{
        const val SEARCH_KEY = "key"
        fun instance(key:String):SearchListFragment{
            val searchListFragment = SearchListFragment()
            searchListFragment.arguments = Bundle().apply {
                putString(SEARCH_KEY,key)
            }
            return searchListFragment
        }
    }
    override fun setContentView() = R.layout.fragment_search_list

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        val key = arguments?.getString(SEARCH_KEY)
        Log.e("gt","key:$key")
    }

    override fun createVM(): SearchListFragmentVM {
        return ViewModelProvider(this).get(SearchListFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}