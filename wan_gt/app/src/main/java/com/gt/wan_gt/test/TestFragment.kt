package com.gt.wan_gt.test

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.views.StickDecoration
import kotlinx.android.synthetic.main.fragment_test.*

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
//        test_rv.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))

        val arrayList = ArrayList<TestBean>()
        for(i in 1 ..19){
            val testBean = TestBean().apply {
//                if(i % 10 ==0){
//                    isAd = true
//                    image = R.mipmap.icon_wan_android
//                }else{
//                }
                    isAd = false
                    title = "$i position-$i"
            }
            arrayList.add(testBean)
        }
        val adapter = TestAdapter()
        adapter.data = arrayList
        test_rv.adapter = adapter
        test_rv.layoutManager = LinearLayoutManager(requireContext())
        test_rv.addItemDecoration(StickDecoration(requireContext()) {
                arrayList[it].title.substring(0, 1)
        })
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