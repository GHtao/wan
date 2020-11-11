package com.gt.wan_gt.db.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.NameInDb

/**
 * 搜索历史数据
 */
@Entity
@NameInDb("search_history_db")
data class SearchHistoryDb(
    @Id(assignable = true)
    @NameInDb(DATA_ID)
    var dataId: Long = 0,
    //人员工号
    @NameInDb(DATA)
    var data: String? = null,
    //人员姓名
    @NameInDb(TIME)
    var time: Long = 0) {


    companion object {
        const val DATA_ID = "data_id"
        const val DATA = "data"
        const val TIME = "time"
    }
}