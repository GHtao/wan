package com.gt.wan_gt.test

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gt.wan_gt.R
import per.wsj.lib.RvParallaxImageView
import per.wsj.lib.controller.ResImageController

/**
 * time 2020/11/18 0018
 * author GT
 */
class TestAdapter:BaseMultiItemQuickAdapter<TestBean,BaseViewHolder>() {

    init {
        addItemType(TestBean.AD, R.layout.adapter_test_image)
        addItemType(TestBean.NORMAl, R.layout.adapter_test)
    }

    override fun convert(holder: BaseViewHolder, item: TestBean) {
        when(holder.itemViewType){
            TestBean.AD->{
               holder.getView<RvParallaxImageView>(R.id.adapter_test_image).apply {
                       bindRecyclerView(recyclerView)
                       setController(ResImageController(context,item.image))
                   }
            }
            TestBean.NORMAl->{
                holder.setText(R.id.adapter_test_tv,item.title)
            }
        }
    }
}