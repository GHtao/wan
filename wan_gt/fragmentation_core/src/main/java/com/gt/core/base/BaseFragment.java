package com.gt.core.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import me.yokeyword.fragmentation.swipeback.SwipeBackFragment;

/**
 * time 2020/3/18 0018
 * author GT
 */
public abstract class BaseFragment<T extends BaseViewModel> extends SwipeBackFragment {
    protected T viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setContentView(),container,false);
        viewModel = createVM();
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSwipeBackEnable(isEnableSwipeBack());
        initView(view,savedInstanceState);

        observe();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        hideSoftInput();
    }

    /**
     * 是否允许滑动返回 默认允许
     */
    public boolean isEnableSwipeBack(){
        return false;
    }
    /**
     * 设置显示的layout
     */
    public abstract int setContentView();

    /**
     * 初始化view
     */
    public abstract void initView(View view,@Nullable Bundle savedInstanceState);

    /**
     * 创建view model
     */
    public abstract T createVM();
    /**
     * view model的观察
     */
    public abstract void observe();

}
