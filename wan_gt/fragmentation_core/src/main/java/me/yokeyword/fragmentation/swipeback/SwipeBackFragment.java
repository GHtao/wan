package me.yokeyword.fragmentation.swipeback;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import me.yokeyword.fragmentation.support.SupportFragment;
import me.yokeyword.fragmentation.swipeback.core.ISwipeBackFragment;
import me.yokeyword.fragmentation.swipeback.core.SwipeBackFragmentDelegate;


/**
 * You can also refer to {@link SwipeBackFragment} to implement YourSwipeBackFragment
 * (extends Fragment and impl {@link me.yokeyword.fragmentation.ISupportFragment})
 * <p>
 * Created by YoKey on 16/4/19.
 */
public class SwipeBackFragment extends SupportFragment implements ISwipeBackFragment,
        SwipeBackLayout.OnSwipeListener {
    final SwipeBackFragmentDelegate mDelegate = new SwipeBackFragmentDelegate(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDelegate.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDelegate.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View attachToSwipeBack(View view) {
        return mDelegate.attachToSwipeBack(view);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mDelegate.onHiddenChanged(hidden);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mDelegate.getSwipeBackLayout();
    }

    /**
     * 是否可滑动
     *
     * @param enable
     */
    public void setSwipeBackEnable(boolean enable) {
        mDelegate.setSwipeBackEnable(enable);
    }

    @Override
    public void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel) {
        mDelegate.setEdgeLevel(edgeLevel);
    }

    @Override
    public void setEdgeLevel(int widthPixel) {
        mDelegate.setEdgeLevel(widthPixel);
    }

    /**
     * Set the offset of the parallax slip.
     */
    public void setParallaxOffset(@FloatRange(from = 0.0f, to = 1.0f) float offset) {
        mDelegate.setParallaxOffset(offset);
    }

    @Override
    public void onDestroyView() {
        mDelegate.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDragStateChange(int state) {
        //滑动的状态改变
//       STATE_IDLE
//       STATE_DRAGGING
//       STATE_SETTLING
//       STATE_FINISHED
    }

    @Override
    public void onEdgeTouch(int oritentationEdgeFlag) {
        //滑动边缘触摸事件
    }

    @Override
    public void onDragScrolled(float scrollPercent) {
        //滑动事件
    }
}