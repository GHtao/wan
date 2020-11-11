package com.gt.wan_gt.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.gt.wan_gt.R
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.simple.SimpleComponent

/**
 * time 2020/7/28 0028
 * author GT
 */
class NavRefreshView @JvmOverloads
constructor(context:Context,attributeSet: AttributeSet? = null,style:Int=0)
    :SimpleComponent(context,attributeSet,style) {
    var pullDownListener:PullDownListener? = null
    var mTextView:TextView = LayoutInflater.from(context)
        .inflate(R.layout.refresh_nva_view,this,false) as TextView
    init {
        addView(mTextView)
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState) {
        when(newState){
            RefreshState.PullDownToRefresh->{
                pullDownListener?.pullDown()
            }
            RefreshState.PullUpToLoad->{
                pullDownListener?.pullDown()
            }
        }
        super.onStateChanged(refreshLayout, oldState, newState)
    }


    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        return 500

    }

    interface PullDownListener{
        fun pullDown()
    }
}