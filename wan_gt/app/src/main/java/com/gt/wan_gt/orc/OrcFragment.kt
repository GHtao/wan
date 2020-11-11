package com.gt.wan_gt.orc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.common.event.EventBean
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_orc.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * time 2020/8/19 0019
 * author GT
 */
const val REQUEST_CODE_CONTENT = 10
class OrcFragment:BaseFragment<OrcFragmentVM>() {

    override fun setContentView() = R.layout.fragment_orc

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        orc_bt_orc.setOnClickListener {
            val split = orc_tv_path.text.split(":")
            if(split.size>1){
                viewModel.loadOrc(split[1])
            }
        }
        orc_bt_path.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            requireActivity().startActivityForResult(intent, REQUEST_CODE_CONTENT)
        }

    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    fun eventBusData(bean:EventBean){
        Log.e("gt","收到path")
        when(bean.type){
            EventBean.Type.ORC_PATH->{
                orc_tv_path.text = "照片路径:"+bean.data?.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("gt","注册 bus ")
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        Log.e("gt","取消注册 bus")
        EventBus.getDefault().unregister(this)
    }
    override fun createVM(): OrcFragmentVM {
        return ViewModelProvider(this).get(OrcFragmentVM::class.java)
    }

    @SuppressLint("SetTextI18n")
    override fun observe() {
       viewModel.apply {
            orcResult.observe(this@OrcFragment, Observer {
                orc_tv_result.text = "识别结果:$it"
            })

           result.observe(this@OrcFragment, Observer {
               ToastUtil.showToast(it.any)
           })
       }
    }
}