package com.gt.wan_gt.common.dialog

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.gt.wan_gt.R

/**
 * time 2020/7/15 0015
 * author GT
 */


/**
 * 等待的dialog
 */
fun loadDialog(context: Context, msg:String, cancel:Boolean = false):AlertDialog{
    val view = LayoutInflater.from(context)
        .inflate(R.layout.dialog_loading,null,false)
    val loadLottie = view.findViewById<LottieAnimationView>(R.id.dialog_loading_lottie)
    val loadMsg = view.findViewById<TextView>(R.id.dialog_loading_msg)
    loadLottie.setAnimation("loading.json")
    loadMsg.text = msg
    val alertDialog = AlertDialog.Builder(context).create()
    alertDialog.setView(view)
    alertDialog.setCancelable(cancel)
    alertDialog.setOnDismissListener {
        loadLottie.cancelAnimation()
    }
    alertDialog.setOnShowListener {
        loadLottie.playAnimation()
    }
    return alertDialog
}