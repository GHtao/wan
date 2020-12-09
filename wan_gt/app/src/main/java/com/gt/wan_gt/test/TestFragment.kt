package com.gt.wan_gt.test

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import kotlinx.android.synthetic.main.fragment_test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * time 2020/10/20 0020
 * author GT
 * 测试页面
 */
class TestFragment : BaseFragment<TestFragmentVM>() {

    override fun setContentView() = R.layout.fragment_test

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        //用这样的方式代替之前的startActivityForResult 解耦
//        val activityResult = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) {
//
//        }
//        activityResult.launch(Intent(requireContext(),TestFragment::class.java))
        test_rv.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        val arrayList = ArrayList<TestBean>()
        for(i in 1 ..19){
            val testBean = TestBean().apply {
                if(i % 10 ==0){
                    isAd = true
                    image = R.mipmap.icon_wan_android
                }else{
                    isAd = false
                    title = "position-$i"
                }
            }
            arrayList.add(testBean)
        }
        val adapter = TestAdapter()
        adapter.data = arrayList
        test_rv.adapter = adapter
        test_rv.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()
    }

    override fun createVM(): TestFragmentVM {
        return ViewModelProvider(this).get(TestFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}