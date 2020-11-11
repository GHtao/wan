package com.gt.wan_gt.performance.tencent

import android.content.Context
import android.util.Log
import com.tencent.matrix.plugin.DefaultPluginListener
import com.tencent.matrix.report.Issue

/**
 * time 2020/10/12 0012
 * author GT
 */
class MatrixPluginListener(context:Context) : DefaultPluginListener(context){

    override fun onReportIssue(issue: Issue?) {
        super.onReportIssue(issue)
        //检测到问题 通过这个回调接口上报
        Log.e("gt","issue:${issue?.content.toString()}")
    }
}