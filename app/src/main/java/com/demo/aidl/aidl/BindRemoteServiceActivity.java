package com.demo.aidl.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.aidl.IRemoteService;
import com.demo.aidl.IRemoteServiceCallBack;
import com.demo.aidl.ParcelableData;
import com.demo.aidl.R;

/**
 * Created by KyoWang on 2016/12/20 .
 */
public class BindRemoteServiceActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 连接远端服务按钮
     */
    private Button mBindBtn;
    /**
     * 断开连接按钮
     */
    private Button mUnBindBtn;
    /**
     * 获取远端服务信息按钮
     */
    private Button mGetServiceInfoBtn;
    /**
     * 向远端服务传递数据按钮
     */
    private Button mDeliverDataBtn;
    /**
     * 信息展示
     */
    private TextView mInfoTv;
    /**
     * 远端服务
     */
    private IRemoteService mService;
    /**
     * 是否绑定远端服务
     */
    private boolean isBound = false;
    /**
     * 连接回调
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * 连接服务器成功回调
         *
         * @param className
         * @param service
         */
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            mService = IRemoteService.Stub.asInterface(service);

            try {
                mService.registerCallback(mCallback);

                mInfoTv.setText("Connected Service");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        /**
         * 服务器因为一场情况断开连接时候回调
         *
         * @param className
         */
        public void onServiceDisconnected(ComponentName className) {

            mService = null;

            mInfoTv.setText("Disconnected");
        }
    };
    /**
     * 远端服务回调
     */
    private IRemoteServiceCallBack mCallback = new IRemoteServiceCallBack.Stub() {
        public void valueChanged(int value) {
            Log.d("BindRemoteServiceActivity", "value changed : " + value);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bind_remote_service_aidl);

        mBindBtn = (Button) findViewById(R.id.remote_bind);
        mUnBindBtn = (Button) findViewById(R.id.remote_unbind);
        mGetServiceInfoBtn = (Button) findViewById(R.id.remote_get_service_info);
        mDeliverDataBtn = (Button) findViewById(R.id.remote_deliver_data);
        mInfoTv = (TextView) findViewById(R.id.remote_info);

        mBindBtn.setOnClickListener(this);
        mUnBindBtn.setOnClickListener(this);
        mGetServiceInfoBtn.setOnClickListener(this);
        mDeliverDataBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.remote_bind) {
            doBindService();
        } else if(id == R.id.remote_unbind) {
            doUnbindService();
        } else if(id == R.id.remote_get_service_info) {
            doGetServiceInfo();
        } else if(id == R.id.remote_deliver_data) {
            doSendMsg();
        }
    }

    /**
     * 绑定服务
     */
    private void doBindService() {
        isBound = true;
        Intent intent = new Intent(BindRemoteServiceActivity.this, RemoteService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 解除绑定
     */
    private void doUnbindService() {
        if(isBound && mService != null) {
            isBound = false;
            try {
                mService.unregisterCallback(mCallback);
                mInfoTv.setText("Unbind");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            unbindService(mConnection);
        }
    }

    /**
     * 向服务端发送信息
     */
    private void doSendMsg() {
        if(!isBound || mService == null) {
            return;
        }
        ParcelableData data = new ParcelableData(1);
        try {
            mService.handleData(data);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用服务端方法获取信息
     */
    private void doGetServiceInfo() {
        if(!isBound || mService == null) {
            return;
        }
        try {
            String info = mService.getServiceName();

            mInfoTv.setText("Service info :" + info);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
