package com.demo.aidl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.demo.aidl.aidl.BindRemoteServiceActivity;
import com.demo.aidl.local.BindLocalServiceActivity;
import com.demo.aidl.messenger.BindMessengerActivity;

/**
 * Created by KyoWang on 2016/12/08 .
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mIBinderBtn;
    private Button mMessengerBtn;
    private Button mAIDLBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        initView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.main_local_service) {
            Intent intent = new Intent(this, BindLocalServiceActivity.class);
            startActivity(intent);
        } else if(id == R.id.main_messenger) {
            Intent intent = new Intent(this, BindMessengerActivity.class);
            startActivity(intent);
        } else if(id == R.id.main_adil) {
            Intent intent = new Intent(this, BindRemoteServiceActivity.class);
            startActivity(intent);
        }
    }

    private void initView() {
        mIBinderBtn = (Button) findViewById(R.id.main_local_service);
        mMessengerBtn = (Button) findViewById(R.id.main_messenger);
        mAIDLBtn = (Button) findViewById(R.id.main_adil);

        mIBinderBtn.setOnClickListener(this);
        mMessengerBtn.setOnClickListener(this);
        mAIDLBtn.setOnClickListener(this);
    }
}
