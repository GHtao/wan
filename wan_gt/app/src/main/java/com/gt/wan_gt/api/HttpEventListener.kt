package com.gt.wan_gt.api

import android.util.Log
import com.gt.wan_gt.utils.DateUtil
import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * time 2020/8/4 0004
 * author GT
 */
class HttpEventListener(private val url:String):EventListener() {
    companion object{
        val FACTORY = Factory {
            it.request().url()
            HttpEventListener(it.request().url().toString())
        }
    }
    /*******call *******/
    override fun callStart(call: Call) {
        super.callStart(call)
        Log.e("gt","okhttp event url:${url} call start:${DateUtil.getStringDate()}")
    }

    override fun callEnd(call: Call) {
        super.callEnd(call)
        Log.e("gt","okhttp event url:${url} call end:${DateUtil.getStringDate()}")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        super.callFailed(call, ioe)
        Log.e("gt","okhttp event url:${url} call failed:${DateUtil.getStringDate()}")

    }
    /*******dns *******/
    override fun dnsStart(call: Call, domainName: String) {
        super.dnsStart(call, domainName)
        Log.e("gt","okhttp event url:${url} dns start:${DateUtil.getStringDate()}")
    }

    override fun dnsEnd(call: Call, domainName: String, inetAddressList: MutableList<InetAddress>) {
        super.dnsEnd(call, domainName, inetAddressList)
        Log.e("gt","okhttp event url:${url} dns end:${DateUtil.getStringDate()}")

    }
    /*******connect *******/
    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        super.connectStart(call, inetSocketAddress, proxy)
        Log.e("gt","okhttp event url:${url} connect start:${DateUtil.getStringDate()}")
    }

    override fun connectEnd(call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?
    ) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol)
        Log.e("gt","okhttp event url:${url} connect end:${DateUtil.getStringDate()}")
    }

    override fun connectFailed(
        call: Call,
        inetSocketAddress: InetSocketAddress,
        proxy: Proxy,
        protocol: Protocol?,
        ioe: IOException
    ) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe)
        Log.e("gt","okhttp event url:${url} connect failed:${DateUtil.getStringDate()}")
    }


    override fun secureConnectStart(call: Call) {
        super.secureConnectStart(call)
        Log.e("gt","okhttp event url:${url} secure connect start:${DateUtil.getStringDate()}")
    }

    override fun secureConnectEnd(call: Call, handshake: Handshake?) {
        super.secureConnectEnd(call, handshake)
        Log.e("gt","okhttp event url:${url} secure connect end:${DateUtil.getStringDate()}")
    }
    /*******request header *******/
    override fun requestHeadersStart(call: Call) {
        super.requestHeadersStart(call)
        Log.e("gt","okhttp event url:${url} request header start:${DateUtil.getStringDate()}")
    }
    override fun requestHeadersEnd(call: Call, request: Request) {
        super.requestHeadersEnd(call, request)
        Log.e("gt","okhttp event url:${url} request header end:${DateUtil.getStringDate()}")
    }

    /*******response header *******/
    override fun responseHeadersStart(call: Call) {
        super.responseHeadersStart(call)
        Log.e("gt","okhttp event url:${url} response header start:${DateUtil.getStringDate()}")
    }
    override fun responseHeadersEnd(call: Call, response: Response) {
        super.responseHeadersEnd(call, response)
        Log.e("gt","okhttp event url:${url} response header end:${DateUtil.getStringDate()}")
    }

    /*******request body *******/
    override fun requestBodyStart(call: Call) {
        super.requestBodyStart(call)
        Log.e("gt","okhttp event url:${url} request body start:${DateUtil.getStringDate()}")
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        super.requestBodyEnd(call, byteCount)
        Log.e("gt","okhttp event url:${url} request body end:${DateUtil.getStringDate()}")
    }
    /*******response body *******/
    override fun responseBodyStart(call: Call) {
        super.responseBodyStart(call)
        Log.e("gt","okhttp event url:${url} response body start:${DateUtil.getStringDate()}")
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        super.responseBodyEnd(call, byteCount)
        Log.e("gt","okhttp event url:${url} response body end:${DateUtil.getStringDate()}")
    }

}