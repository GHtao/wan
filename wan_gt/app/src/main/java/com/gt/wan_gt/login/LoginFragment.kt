package com.gt.wan_gt.login

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseFragment
import com.gt.wan_gt.R
import com.gt.wan_gt.common.dialog.loadDialog
import com.gt.wan_gt.register.RegisterFragment
import com.gt.wan_gt.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * time 2020/7/14 0014
 * author GT
 */
class LoginFragment:BaseFragment<LoginFragmentVM>() {
    override fun setContentView() = R.layout.fragment_login

    override fun initView(view: View?, savedInstanceState: Bundle?) {

        login_bt_login.setOnClickListener {
            val account = login_et_account.text.toString().trim()
            val password = login_et_password.text.toString().trim()
            if(TextUtils.isEmpty(account) || TextUtils.isEmpty(password)){
                ToastUtil.showToast("账号或者密码不能为空")
            }
            viewModel.login(account,password)
        }

        login_bt_register.setOnClickListener {
            start(RegisterFragment())
        }

        login_back.setOnClickListener {
            pop()
        }
    }

    override fun createVM(): LoginFragmentVM {
        return ViewModelProvider(this).get(LoginFragmentVM::class.java)
    }

    override fun observe() {
        val loadDialog = loadDialog(requireActivity(), "登录中")
        viewModel.apply {
            result.observe(this@LoginFragment, Observer {
                if(it.flag){
                    ToastUtil.showToast("登录成功")
                    pop()
                }else{
                    ToastUtil.showToast("登录失败:${it.any}")
                }
            })
            isLoading.observe(this@LoginFragment, Observer {
                if(it.flag){
                    loadDialog.show()
                }else{
                    if(loadDialog.isShowing){
                        loadDialog.dismiss()
                    }
                }
            })
        }
    }
}