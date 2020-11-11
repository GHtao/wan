package com.gt.wan_gt.home

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import com.gt.wan_gt.WanApp
import com.gt.wan_gt.api.bean.FeedArticleBean

/**
 * time 2020/7/16 0016
 * author GT
 */
class HomeAdapter
    :BaseQuickAdapter<FeedArticleBean.FeedArticleData,BaseViewHolder>(R.layout.adapter_home) {
    var isCollectPage = false
    init {
        //不能再covert里面注册 因为点击事件在convert之前触发
        addChildClickViewIds(R.id.home_item_like,R.id.home_item_title_end,R.id.home_item_project)
    }
    override fun convert(holder: BaseViewHolder, item: FeedArticleBean.FeedArticleData) {
        holder.setText(R.id.home_item_tv,item.author)
        if(isCollectPage){
            holder.setText(R.id.home_item_title_end,item.chapterName)
        }else{
            holder.setText(R.id.home_item_title_end,item.superChapterName+"/"+item.chapterName)
        }
        holder.setText(R.id.home_item_content,item.title)
        if(item.collect || isCollectPage){
            holder.setImageDrawable(R.id.home_item_like,WanApp.instance!!.resources.getDrawable(R.drawable.icon_like))
        }else{
            holder.setImageDrawable(R.id.home_item_like,WanApp.instance!!.resources.getDrawable(R.drawable.icon_like_article_not_selected))
        }
        holder.setText(R.id.home_item_time,item.niceDate)
        setItemTag(holder,item)


    }

    /**
     * 设置条目的标签
     */
    private fun setItemTag(holder: BaseViewHolder, item: FeedArticleBean.FeedArticleData){
        holder.getView<TextView>(R.id.home_item_new).visibility = View.GONE
        holder.getView<TextView>(R.id.home_item_project).visibility = View.GONE
        if(isCollectPage) return
        if(item.superChapterName != null && item.superChapterName!!.contains("开源项目")){
            setProjectTag(holder,"项目")
        }
        if(item.superChapterName != null && item.superChapterName!!.contains("导航")){
            setProjectTag(holder,"导航")
        }
        item.niceDate?.apply {
            if(contains("分钟") || contains("小时") || contains("天")){
                holder.getView<TextView>(R.id.home_item_new).apply {
                    visibility = View.VISIBLE
                    text = "新"
                    setTextColor(ContextCompat.getColor(context,R.color.light_green))
                    setBackgroundResource(R.drawable.shape_tag_green_background)
                }
            }
        }
    }

    /**
     * 设置项目标签
     */
    private fun setProjectTag(holder: BaseViewHolder,name:String){
        holder.getView<TextView>(R.id.home_item_project).apply {
            visibility = View.VISIBLE
            text = name
            setTextColor(ContextCompat.getColor(context,R.color.light_deep_red))
            setBackgroundResource(R.drawable.selector_tag_red_background)
        }


    }
}