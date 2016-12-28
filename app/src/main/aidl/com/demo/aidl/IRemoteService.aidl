package com.demo.aidl;

import com.demo.aidl.ParcelableData;
import com.demo.aidl.IRemoteServiceCallBack;

interface IRemoteService {
    /**
     * 获取当前进程的pid
     */
    int getPid();
    /**
     * 获取当前服务名称
     */
    String getServiceName();
    /**
     * 处理客户端传过来的数据
     */
    void handleData(in ParcelableData data);
    /**
     * 注册回调
     */
    void registerCallback(IRemoteServiceCallBack cb);
    /**
     * 注销回调
     */
    void unregisterCallback(IRemoteServiceCallBack cb);
}
