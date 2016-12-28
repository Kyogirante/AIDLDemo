package com.demo.aidl.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by KyoWang on 2016/12/28 .
 */
public class MessengerService extends Service {

    public static final int MSG_REGISTER_CLIENT = 0X001;
    public static final int MSG_UNREGISTER_CLIENT = 0X010;
    public static final int MSG_HANDLE = 0X100;

    private ArrayList<Messenger> mClients = new ArrayList<>();

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Remote Service Destroy", Toast.LENGTH_SHORT).show();
    }

    /**
     * 处理客户端发送的信息
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_HANDLE:
                    for (Messenger mClient : mClients) {
                        try {
                            mClient.send(Message.obtain(null, MSG_HANDLE, msg.arg1, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            mClients.remove(mClient);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
}
