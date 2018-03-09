package com.lawyee.myaudioandpaly;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lawyee.myaudioandpaly.adapter.MainListAdapter;
import com.lawyee.myaudioandpaly.baidu.recognization.online.InFileStream;
import com.lawyee.myaudioandpaly.baidu.util.Logger;
import com.lawyee.myaudioandpaly.vo.SavePath;

import java.util.ArrayList;
import java.util.List;

public abstract class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView mBtnPlay;
    protected ImageView mImgBlack;
    protected ImageView mImgSetting;
    protected Class settingActivityClass = null;
    protected Handler handler;
    protected boolean running = false;
    private ListView mRlvContent;
    private Context mContext;
    ArrayList mListData;
    private ArrayList<Object> mData;
    private MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InFileStream.setContext(this);
        setContentView(R.layout.activity_main);
        initView();
        handler = new Handler() {

            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handleMsg(msg);
            }

        };
        Logger.setHandler(handler);
        initPermission();
        initRecog();
    }

    protected abstract void initRecog();


    protected void initView() {
        mBtnPlay = (TextView) findViewById(R.id.btn_play);
        mContext = this;
        mBtnPlay.setOnClickListener(this);
        mImgBlack = (ImageView) findViewById(R.id.img_black);
        mImgBlack.setOnClickListener(this);
        mImgSetting = (ImageView) findViewById(R.id.img_setting);
        mImgSetting.setOnClickListener(this);
        mRlvContent = findViewById(R.id.lv_content);
        if (mImgSetting != null && settingActivityClass != null) {
            mImgSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    running = true;
                    Intent intent = new Intent(MainActivity.this, settingActivityClass);
                    startActivityForResult(intent, 1);
                }
            });
        }
        setAdapter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                break;
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

    private void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }


    protected void handleMsg(Message msg) {
        if (msg.obj != null) {
            Log.e("yfl", "handleMsg: " + msg.obj.toString());
        }
    }

    public void sendMessage(Object obj) {
        if (mRlvContent == null) {
            mRlvContent = findViewById(R.id.lv_content);
        }
        if (obj == null) {
            Toast.makeText(MainActivity.this, "参数异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mData == null) {
            mData = new ArrayList<>();
        } else {
            mData.clear();
        }
        SavePath savePath= (SavePath) obj;
        mData.add(savePath);
        addListData(mData);
        if (mAdapter==null){
        setAdapter();
        }
        mAdapter.refreshData(mListData);


    }

    private void setAdapter() {
        mAdapter = new MainListAdapter(mContext, mListData);
        mRlvContent.setDividerHeight(2);
        mRlvContent.setAdapter(mAdapter);
    }

    private void clearData() {
        if (mListData == null) {
            mListData = new ArrayList();
        } else {
            mListData.clear();
        }
    }

    private void addListData(List<?> list) {
        if (mListData == null) {
            clearData();
        }
        if (list == null || list.isEmpty()) {
            return;
        }
        mListData.addAll(list);

    }
}
