package com.gt.wan_gt.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseActivity
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
class LoginActivity:BaseActivity<LoginActivityVM>() {

    companion object{
        fun start(context:Activity){
            context.startActivity(Intent(context,LoginActivity::class.java))
        }
    }
    override fun createVM(): LoginActivityVM {
        return ViewModelProvider(this).get(LoginActivityVM::class.java)
    }

    override fun observe() {

    }

    override fun contentView(savedInstanceState: Bundle?) = R.layout.activity_login

    override fun initData(savedInstanceState: Bundle?) {
        loadRootFragment(R.id.fl_content,LoginFragment())
    }
}