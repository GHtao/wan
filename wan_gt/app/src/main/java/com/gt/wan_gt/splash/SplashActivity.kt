package com.gt.wan_gt.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gt.core.base.BaseActivity
import com.gt.wan_gt.main.MainActivity
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.utils.StatusBarUtil
import com.gt.wan_gt.views.AppFloatView
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * time 2020/7/13 0013
 * author GT
 */
class SplashActivity:BaseActivity<SplashViewModel>() {

    override fun contentView(savedInstanceState: Bundle?) = R.layout.activity_splash

    override fun initData(savedInstanceState: Bundle?) {
        StatusBarUtil.immersive(this)
        if(WanApp.isFirstRun){
            WanApp.isFirstRun = false
            animation_wan_w.apply {
                setAnimation("W.json")
                playAnimation()
            }
            animation_wan_a.apply {
                setAnimation("A.json")
                playAnimation()
            }
            animation_wan_n.apply {
                setAnimation("N.json")
                playAnimation()
            }
            animation_android_a.apply {
                setAnimation("A.json")
                playAnimation()
            }
            animation_android_n.apply {
                setAnimation("N.json")
                playAnimation()
            }
            animation_android_d.apply {
                setAnimation("D.json")
                playAnimation()
            }
            animation_android_r.apply {
                setAnimation("R.json")
                playAnimation()
            }
            animation_android_o.apply {
                setAnimation("O.json")
                playAnimation()
            }
            animation_android_i.apply {
                setAnimation("I.json")
                playAnimation()
            }
            animation_android_d2.apply {
                setAnimation("D.json")
                playAnimation()
            }
            viewModel.jumpToMain()
        }else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        animation_wan_w.cancelAnimation()
        animation_wan_a.cancelAnimation()
        animation_wan_n.cancelAnimation()
        animation_android_a.cancelAnimation()
        animation_android_n.cancelAnimation()
        animation_android_d.cancelAnimation()
        animation_android_r.cancelAnimation()
        animation_android_o.cancelAnimation()
        animation_android_i.cancelAnimation()
        animation_android_d2.cancelAnimation()
    }

    override fun createVM(): SplashViewModel {
        return ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override fun observe() {
        viewModel.apply {
            jump.observe(this@SplashActivity, Observer {
                startActivity(Intent(this@SplashActivity,
                    MainActivity::class.java))
                finish()
            })
        }
    }
}