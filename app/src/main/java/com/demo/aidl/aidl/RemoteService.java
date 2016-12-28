package com.demo.aidl.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.demo.aidl.IRemoteService;
import com.demo.aidl.IRemoteServiceCallBack;
import com.demo.aidl.ParcelableData;

/**
 * Created by KyoWang on 2016/12/20 .
 */
public class RemoteService extends Service {

    private HandlerThread mTread = new HandlerThread("Count");
    /**
     * 定时任务，模拟回调
     */
    private Handler mHandler;
    /**
     * Ui线程的Handler,用于显示Toast
     */
    private Handler mUiHandler = new Handler();
    /**
     * 回调容器
     */
    private final RemoteCallbackList<IRemoteServiceCallBack> mCallBacks = new RemoteCallbackList<>();
    /**
     * aidl接口具体实现
     */
    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public String getServiceName() throws RemoteException {
            return RemoteService.this.getClass().getSimpleName();
        }

        @Override
        public void handleData(ParcelableData data) throws RemoteException {
            final int num = data.num;
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "num is " + num, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void registerCallback(IRemoteServiceCallBack cb) throws RemoteException {
            if(cb != null) {
                mCallBacks.register(cb);
            }
        }

        @Override
        public void unregisterCallback(IRemoteServiceCallBack cb) throws RemoteException {
            if(cb != null) {
                mCallBacks.unregister(cb);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mTread.start();

        mHandler = new Handler(mTread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                notifyCallBack();

                sendEmptyMessageDelayed(1, 10000);
            }
        };

        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(RemoteService.this, "Remote service Destroy", Toast.LENGTH_SHORT).show();

        // 清空队列
        mHandler.removeCallbacksAndMessages(null);
        // 注销所有回调
        mCallBacks.kill();
    }


    private void notifyCallBack() {
        int size = mCallBacks.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mCallBacks.getBroadcastItem(i).valueChanged(i);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }
}
