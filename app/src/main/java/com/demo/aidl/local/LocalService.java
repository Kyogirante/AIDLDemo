package com.demo.aidl.local;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 本地服务
 * <br/>
 * 和启动应用属于同一进程
 */

public class LocalService extends Service {
    /**
     * 自定的IBinder
     */
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 提供给客户端的方法
     *
     * @return
     */
    public String getServiceInfo() {
        return this.getPackageName() + " " + this.getClass().getSimpleName();
    }

    /**
     * 自定义的IBinder
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }
    }
}
