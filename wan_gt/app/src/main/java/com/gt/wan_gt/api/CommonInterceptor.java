package com.gt.wan_gt.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * time 2019/5/14 0014
 * author GT
 * 公共处理的拦截器
 */
public class CommonInterceptor implements Interceptor {
    private Gson gson = new Gson();
    private CodeCallBack codeCallBack;
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody body = response.body();
        if(body != null && body.contentLength() != 0){
            //response.body().toString() 只能读取一次，读取完会将流关闭
            //这样流数据不会占据内存，所以用source得到buffer，再buffer.clone()得到数据
            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            String bodyString = buffer.clone().readString(StandardCharsets.UTF_8);
            if(!TextUtils.isEmpty(bodyString)){
                Bean bean = gson.fromJson(bodyString, Bean.class);
                Log.e("gt","code:"+bean.code);
                if(codeCallBack != null){
                    codeCallBack.onCodeBack(bean.code);
                }
            }
        }
        return response;
    }

    /**
     * 设置code回调
     */
    public void setCodeCallBack(CodeCallBack codeCallBack){
        this.codeCallBack = codeCallBack;
    }
    /**
     * 返回一些共性的code
     */
    public interface CodeCallBack{
        void  onCodeBack(int code);
    }
    /**
     * 响应结果
     */
    static class Bean{
        int code;
        int message;
    }

}
