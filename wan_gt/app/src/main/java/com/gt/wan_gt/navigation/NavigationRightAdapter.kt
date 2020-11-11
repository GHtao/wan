package com.gt.wan_gt.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.api.bean.NavigationBean
import com.gt.wan_gt.common.Constants
import com.gt.wan_gt.web_view.WebViewFragment
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlin.random.Random

/**
 * time 2020/7/28 0028
 * author GT
 */
class NavigationRightAdapter:
    BaseQuickAdapter<NavigationBean,BaseViewHolder>(R.layout.adapter_navigation_right) {
    var tagClickListener:TagFlowClickListener? = null
    override fun convert(holder: BaseViewHolder, item: NavigationBean) {
        holder.setText(R.id.adapter_navigation_right_title,item.name)
        val tagFlowLayout = holder.getView<TagFlowLayout>(R.id.adapter_navigation_right_tag)
        tagFlowLayout.adapter = object :TagAdapter<FeedArticleBean.FeedArticleData>(item.articles){
            override fun getView(
                parent: FlowLayout?,
                position: Int,
                t: FeedArticleBean.FeedArticleData?): View {
                val tv = LayoutInflater.from(WanApp.instance)
                    .inflate(R.layout.adapter_usage_tag,parent,false) as TextView
                tv.text = t?.title
                tv.setBackgroundColor(Constants.TAB_COLORS[Random.nextInt(Constants.TAB_COLORS.size-1)])
                tv.setTextColor(ContextCompat.getColor(WanApp.instance!!.applicationContext,R.color.white))
                tagFlowLayout.setOnTagClickListener { view, position, parent ->
                    val data = item.articles!![position]
                    tagClickListener?.tagClick(data)
                    Log.e("gt","nav tag:${item.articles?.get(position)?.title}")
                    true
                }
                return tv
            }
        }
    }

    interface TagFlowClickListener{
        fun tagClick(data:FeedArticleBean.FeedArticleData)
    }
}