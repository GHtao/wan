package com.gt.wan_gt.api.body

/**
 * time 2020/8/19 0019
 * author GT
 */
class OrcBody {
//        "image":    "图片二进制数据的base64编码/图片url"，
//        "configure":
//        {
//            "min_size" : 16,                      #图片中文字的最小高度，单位像素
//            "output_prob" : true,               #是否输出文字框的概率
//            "output_keypoints": false,          #是否输出文字框角点
//            "skip_detection": false             #是否跳过文字检测步骤直接进行文字识别
//            "without_predicting_direction": false   #是否关闭文字行方向预测
//        }
    var image = ""
    var configure:Configure? = null

    class Configure{
        var min_size = 16
        var output_prob = true
        var output_keypoints = false
        var skip_detection = false
        var without_predicting_direction = false

    }
}