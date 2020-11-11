package com.gt.wan_gt.api.bean

/**
 * time 2020/8/19 0019
 * author GT
 */
class OrcBean {
    var request_id = ""
    var ret:ArrayList<Ret>? = null
    var success = false
    class Ret{
        var prob = ""
        var rect:Rect? = null
        var keypoints:ArrayList<Keypoints>? = null
        var word = ""
    }
    class Rect{
        var angle = 0f
        var left = 0f
        var top = 0f
        var width = 0f
        var height = 0f
    }
    class Keypoints{
        var x = 0f
        var y = 0f
    }
//    {
//        "request_id" : "20170301160849_918cfcae128fc919ad6d6e3b865d2973",     #请求唯一标识，用于错误追踪
//        "ret":[
//        {
//            \"prob\" : 0.95983,  #文字区域概率
//            "rect":{     #文字区域
//                "angle" : 0,   #文字区域角度
//                "left" : 50,   #文字区域左上角x坐标
//                "top" : 50,   #文字区域左上角y坐标
//                "width" : 100,  #文字区域宽度
//                "height" : 40     #文字区域高度
//              },
//            "keypoints": [   # 文字区域角点位置，以文字的真实左上角点为起始点，顺时针顺序
//            {"x": 50, "y": 50},
//            {"x": 150, "y": 50},
//            {"x": 150, "y": 90},
//            {"x": 50, "y": 90}
//            ],
//            "word":"文字内容"    #文字内容
//        },
//        {
//            \"prob\" : 0.95983,  #文字区域概率
//            "rect":{     #文字区域
//            "angle" : 10,   #文字区域角度
//            "left" : 50,   #文字区域左上角x坐标
//            "top" : 50,   #文字区域左上角y坐标
//            "width" : 100,  #文字区域宽度
//            "height" : 40     #文字区域高度
//        },
//            "keypoints": [  # 文字区域角点位置，以文字的真实左上角点为起始点，顺时针顺序
//            {"x": 54, "y": 41},
//            {"x": 152, "y": 58},
//            {"x": 145, "y": 98},
//            {"x": 47, "y": 81}
//            ],
//            "word":"文字内容"    #文字内容
//        }
//        ],
//        "success" : true
//    }
}