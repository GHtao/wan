package com.gt.wan_gt.search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import com.gt.wan_gt.db.bean.SearchHistoryDb

/**
 * time 2020/7/24 0024
 * author GT
 */
class SearchAdapter:
    BaseQuickAdapter<SearchHistoryDb,BaseViewHolder>(R.layout.adapter_search_history) {

    override fun convert(holder: BaseViewHolder, item: SearchHistoryDb) {
        holder.setText(R.id.search_adapter_key,item.data)
    }
}