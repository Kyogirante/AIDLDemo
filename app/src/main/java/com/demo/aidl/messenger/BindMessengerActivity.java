package com.demo.aidl.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.aidl.R;

/**
 * Created by KyoWang on 2016/12/28 .
 */
public class BindMessengerActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 绑定服务按钮
     */
    private Button mBindBtn;
    /**
     * 解除绑定按钮
     */
    private Button mUnbindBtn;
    /**
     * 向服务端发送信息按钮
     */
    private Button mSendMsgBtn;
    /**
     * 信息展示
     */
    private TextView mInfoTv;
    /**
     * 是否绑定远端服务
     */
    private boolean isBound = false;
    /**
     * 关联远端服务的messenger
     */
    private Messenger mServiceWrapper;
    /**
     * 用于处理服务端发送的信息
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceWrapper = new Messenger(service);

            mInfoTv.setText("Connected Service");


            try {
                // 添加监听注册
                Message msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceWrapper.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceWrapper = null;
            mInfoTv.setText("Disconnected");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bind_remote_service_messemger);

        mBindBtn = (Button) findViewById(R.id.remote_bind);
        mUnbindBtn = (Button) findViewById(R.id.remote_unbind);
        mSendMsgBtn = (Button) findViewById(R.id.remote_send_msg);

        mInfoTv = (TextView) findViewById(R.id.remote_info);

        mBindBtn.setOnClickListener(this);
        mUnbindBtn.setOnClickListener(this);
        mSendMsgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.remote_bind) {
            doBindService();
        } else if(id == R.id.remote_unbind) {
            doUnbindService();
        } else if(id == R.id.remote_send_msg) {
            doSendMsg();
        }
    }

    /**
     * 绑定服务
     */
    private void doBindService() {
        if(!isBound) {
            bindService(new Intent(this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);

            isBound = true;

            mInfoTv.setText("Binding...");
        }
    }

    /**
     * 移除监听并解绑服务
     */
    private void doUnbindService() {
        if(isBound) {
            if(mServiceWrapper != null) {
                try {
                    Message msg = Message.obtain(null, MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceWrapper.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            unbindService(mConnection);
            isBound = false;
            mInfoTv.setText("Unbind");
        }
    }

    private void doSendMsg() {
        if(mServiceWrapper != null) {
            try {
                Message msg = Message.obtain(null,
                        MessengerService.MSG_HANDLE, this.hashCode(), 0);
                mServiceWrapper.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理服务端发送的信息
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_HANDLE:
                    mInfoTv.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
