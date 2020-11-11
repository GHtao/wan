package com.gt.wan_gt.performance.tencent;

import com.tencent.matrix.Matrix;
import com.tencent.matrix.util.MatrixLog;
import com.tencent.mrs.plugin.IDynamicConfig;

public class DynamicConfigImplDemo implements IDynamicConfig {
    private static final String TAG = "Matrix.DynamicConfigImplDemo";

    public DynamicConfigImplDemo() {

    }

    public boolean isFPSEnable() {
        return true;
    }

    public boolean isTraceEnable() {
        return true;
    }

    public boolean isMatrixEnable() {
        return true;
    }

    /**
     * 改变配置的默认值 判断key的类型 对应更改
     */
    @Override
    public String get(String key, String defStr) {
        return defStr;
    }

    @Override
    public int get(String key, int defInt) {
        if (ExptEnum.clicfg_matrix_resource_max_detect_times.name().equals(key)) {
            MatrixLog.i(TAG, "key:" + key + ", before change:" + defInt + ", after change, value:" + 2);
            return 2;//new value
        }
        if (ExptEnum.clicfg_matrix_trace_fps_time_slice.name().equals(key)) {
            return 12000;
        }
        return defInt;

    }

    @Override
    public long get(String key, long defLong) {
        if (ExptEnum.clicfg_matrix_resource_detect_interval_millis.name().equals(key)) {
            MatrixLog.i(TAG, key + ", before change:" + defLong + ", after change, value:" + 2000);
            return 2000;
        }
        return defLong;
    }


    @Override
    public boolean get(String key, boolean defBool) {
        return defBool;
    }

    @Override
    public float get(String key, float defFloat) {
        return defFloat;
    }

}