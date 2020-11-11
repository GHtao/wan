package com.gt.wan_gt.performance.hook;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * time 2020/8/12 0012
 * author GT
 * 使用ele hook图片的加载 不能使用kotlin代码
 */
class EleHookJava {
    public static long startTime = 0;
    /**
     * hook activity的onCreate
     */
//    @Insert(value = "onCreate",mayCreateSuper = true)
//    @TargetClass(value = "androidx.core.app.ComponentActivity" ,scope = Scope.ALL)
    protected void  onCreate(Bundle savedInstanceState){
        Log.e("gt","bitmap width: onCreate");
//        Origin.callVoid();
    }

//    @Insert(value = "onCreateView",mayCreateSuper = true)
//    @TargetClass(value = "androidx.fragment.app.Fragment",scope = Scope.LEAF)
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //使用变量出错？？？
//        startTime = System.currentTimeMillis();
//        Log.e("gt",getClass().getCanonicalName()+"start time:"+(System.currentTimeMillis()));
//        return (View) Origin.call();
//    }


//    @Insert(value = "onViewCreated",mayCreateSuper = true)
//    @TargetClass(value = "androidx.fragment.app.Fragment",scope = Scope.LEAF)
    public void onViewCreated(View view, Bundle savedInstanceState){
        Log.e("gt",getClass().getCanonicalName()+"end time:"+(System.currentTimeMillis()));//- startTime
//        Origin.callVoid();
    }

//    @Insert("setImageBitmap")
//    @TargetClass(value = "android.widget.ImageView",scope = Scope.LEAF)
    public void hookImageView(Bitmap bitmap){
        // This仅用于Insert 方式的非静态方法的Hook中
//        Log.e("gt","name:"+This.CLASS_NAME);
//        Origin.callVoid();
    }
}
