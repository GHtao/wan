package com.gt.wan_gt.project.list

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.FeedArticleBean
import com.gt.wan_gt.api.bean.ProjectBean

/**
 * time 2020/7/30 0030
 * author GT
 */
class ProjectListAdapter:
    BaseQuickAdapter<FeedArticleBean.FeedArticleData,BaseViewHolder>(R.layout.adapter_project_list) {
    override fun convert(holder: BaseViewHolder, item: FeedArticleBean.FeedArticleData) {
        if(item.envelopePic != null){
            Glide.with(context)
                .load(item.envelopePic)
                .into(holder.getView(R.id.adapter_project_list_image))
        }
        holder.setText(R.id.adapter_project_list_title,item.title)
        holder.setText(R.id.adapter_project_list_time,item.niceDate)
        holder.setText(R.id.adapter_project_list_name,item.author)
        holder.setText(R.id.adapter_project_list_content,item.desc)
    }
}