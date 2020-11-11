package com.gt.wan_gt.api.bean

/**
 * time 2020/7/16 0016
 * author GT
 * 文章列表的bean
 */
class FeedArticleBean {
    var curPage = 0
    var datas: ArrayList<FeedArticleData>? = null
    var offset = 0
    var over = false
    var pageCount = 0
    var size = 0
    var total = 0

    /**
     * 文章的信息
     */
    class FeedArticleData{
        var apkLink: String? = null
        var author: String? = null
        var chapterId = 0
        var chapterName: String? = null
        var collect = false
        var courseId = 0
        var desc: String? = null
        var envelopePic: String? = null
        var id = 0
        var link: String? = null
        var niceDate: String? = null
        var origin: String? = null
        var projectLink: String? = null
        var superChapterId = 0
        var superChapterName: String? = null
        var publishTime: Long = 0
        var title: String? = null
        var visible = 0
        var zan = 0
    }
}