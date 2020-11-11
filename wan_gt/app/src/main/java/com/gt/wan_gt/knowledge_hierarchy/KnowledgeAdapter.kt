package com.gt.wan_gt.knowledge_hierarchy

import androidx.core.graphics.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.KnowledgeBean
import com.gt.wan_gt.utils.ColorUtil
import java.lang.StringBuilder

/**
 * time 2020/7/24 0024
 * author GT
 */
class KnowledgeAdapter:
    BaseQuickAdapter<KnowledgeBean,BaseViewHolder>(R.layout.adapter_knowledge) {
    private val stringBuilder = StringBuilder()
    override fun convert(holder: BaseViewHolder, item: KnowledgeBean) {
        stringBuilder.clear()
        holder.setText(R.id.adapter_knowledge_title,item.name)
        holder.setTextColor(R.id.adapter_knowledge_title, ColorUtil.randomColor())
        item.children?.forEach {
            stringBuilder.append(it.name).append(" ")
        }
        holder.setText(R.id.adapter_knowledge_content,stringBuilder.toString())
    }
}