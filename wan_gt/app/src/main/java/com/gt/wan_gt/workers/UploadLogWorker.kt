package com.gt.wan_gt.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * time 2020/8/6 0006
 * author GT
 */
class UploadLogWorker(context: Context,parameters: WorkerParameters)
    :CoroutineWorker(context,parameters) {

    override suspend fun doWork(): Result {
        val path = inputData.getString("file_path")
        Log.e("gt","UploadLogWorker doWork path:$path  thread:${Thread.currentThread().name}")
        return Result.success()
    }

}