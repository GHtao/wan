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

    override fun setContentView() = R.layout.fragment_test_motion

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        //用这样的方式代替之前的startActivityForResult 解耦
//        val activityResult = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()) {
//
//        }
//        activityResult.launch(Intent(requireContext(),TestFragment::class.java))
    }

    override fun createVM(): TestFragmentVM {
        return ViewModelProvider(this).get(TestFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }
}