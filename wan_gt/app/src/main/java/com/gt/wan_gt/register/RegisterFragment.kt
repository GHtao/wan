package com.gt.wan_gt.register

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.item_tool_bar.*

/**
 * time 2020/7/14 0014
 * author GT
 */
class RegisterFragment:BaseFragment<RegisterFragmentVM>() {
    override fun setContentView() = R.layout.fragment_register

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        initToolBar()
        register_btn.setOnClickListener {
            val account = register_et_account.text.toString().trim()
            val password = register_et_password.text.toString().trim()
            val rePassword = register_et_re_password.text.toString().trim()
            if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)){
                ToastUtil.showToast("账号或密码不能为空")
                return@setOnClickListener
            }
            if(password != rePassword){
                ToastUtil.showToast("两次密码不一致")
                return@setOnClickListener
            }
            viewModel.register(account,password,rePassword)
        }
    }

    /**
     * 初始化tool bar
     */
    private fun initToolBar(){
        common_tool_bar.setBackgroundColor(WanApp.instance!!.resources.getColor(R.color.register_bac))
        common_tool_bar_search.visibility = View.GONE
        common_tool_bar_usage.visibility = View.GONE
        common_tool_bar_title.text = "注册"
        common_tool_bar_back.setOnClickListener {
            pop()
        }
    }

    override fun createVM(): RegisterFragmentVM {
        return ViewModelProvider(this).get(RegisterFragmentVM::class.java)
    }

    override fun observe() {
        viewModel.apply {
            result.observe(this@RegisterFragment, Observer {
                if(it.flag){
                    ToastUtil.showToast("注册成功")
                    pop()
                }else{
                    ToastUtil.showToast("注册失败:${it.any}")
                }
            })
        }
    }
}