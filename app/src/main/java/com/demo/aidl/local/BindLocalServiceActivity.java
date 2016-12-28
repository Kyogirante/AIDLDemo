package com.demo.aidl.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.demo.aidl.R;

/**
 * 绑定本地服务的组件
 *
 * Created by KyoWang on 2016/12/08 .
 */
public class BindLocalServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mShowServiceNameBtn;

    private LocalService mService;

    private boolean mBound = false;

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_bind_local_service);
        mShowServiceNameBtn = (Button) findViewById(R.id.bind_local_service_btn);
        mShowServiceNameBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.bind_local_service_btn) {
            if(mBound) {
                String info = mService.getServiceInfo();
                Toast.makeText(BindLocalServiceActivity.this, info, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
