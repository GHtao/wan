package com.gt.wan_gt.navigation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.core.base.BaseViewModel
import com.gt.wan_gt.R
import com.gt.wan_gt.api.bean.NavigationBean

/**
 * time 2020/7/28 0028
 * author GT
 */
class NavigationLeftAdapter:
    BaseQuickAdapter<NavigationBean,BaseViewHolder>(R.layout.adapter_navigation_left) {
    override fun convert(holder: BaseViewHolder, item: NavigationBean) {
        if(item.isSelected){
            holder.setBackgroundResource(R.id.adapter_navigation_left_title,R.color.white)
        }else{
            holder.setBackgroundResource(R.id.adapter_navigation_left_title,R.color.shallow_grey)
        }
        holder.setText(R.id.adapter_navigation_left_title,item.name)
    }
}