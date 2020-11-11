package com.gt.core.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.os.TraceCompat;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.swipeback.SwipeBackActivity;


/**
 * time 2020/3/18 0018
 * author GT
 */
public abstract class BaseActivity<T extends BaseViewModel> extends SwipeBackActivity {
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resId = contentView(savedInstanceState);
        if(resId != 0) setContentView(resId);
        viewModel = createVM();
        setSwipeBackEnable(isEnableSwipeBack());
        observe();
        initData(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 是否允许滑动返回 默认不允许 根据具体的使用场景来设置
     * activity的滑动需要用透明的主题 否则背景是黑色的
     */
    public boolean isEnableSwipeBack(){
        return false;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

    /**
     * 初始化view
     */
    public abstract int contentView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    public abstract void initData(Bundle savedInstanceState);

    /**
     * 创建view model
     */
    public abstract T createVM();
    /**
     * view model的观察
     */
    public abstract void observe();
}