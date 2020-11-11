package com.gt.wan_gt.a_route

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.gt.core.base.BaseActivity
import com.gt.wan_gt.R

/**
 * time 2020/10/14 0014
 * author GT
 */
@Route(path = "/test/route")
class ARouteTestActivity :BaseActivity<ARouteTestActivityVM>() {
    //添加JvmField 是public 不加的话就是private
    @JvmField
    @Autowired
    var aaa = ""
    override fun contentView(savedInstanceState: Bundle?) = R.layout.activity_a_route

    override fun initData(savedInstanceState: Bundle?) {
        //注入之后才能获取到数据
        ARouter.getInstance().inject(this)
        Log.e("gt","aaa :$aaa")
    }

    override fun createVM(): ARouteTestActivityVM {
        return ViewModelProvider(this).get(ARouteTestActivityVM::class.java)
    }

    override fun observe() {
        viewModel.apply {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}