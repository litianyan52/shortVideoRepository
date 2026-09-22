package com.libase.utils

import android.util.Log

object RunTimeCheck {

    val tag: String = "RunTimeMemory"

    fun getMemoryInfo() {
        val runtime = Runtime.getRuntime()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory
        val maxMemory = runtime.maxMemory()
        Log.d(tag, "maxMemory: ${maxMemory / 1024 / 1024}MB")
        Log.d(tag, "usedMemory: ${usedMemory / 1024 / 1024}MB")


    }
//// 当前已经申请的堆内存
//    long totalMemory = runtime.totalMemory();
//
//// 空闲内存
//    long freeMemory = runtime.freeMemory();
//
//// 实际已使用
//    long usedMemory = totalMemory - freeMemory;
//
//// 最大可使用堆
//    long maxMemory = runtime.maxMemory();
//
//    Log.d("Memory",
//    "used=" + usedMemory / 1024 / 1024 + "MB\n" +
//    "total=" + totalMemory / 1024 / 1024 + "MB\n" +
//    "max=" + maxMemory / 1024 / 1024 + "MB");
}