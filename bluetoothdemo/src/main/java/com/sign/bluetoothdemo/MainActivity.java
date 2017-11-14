package com.sign.bluetoothdemo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static int REQUEST_ENABLE_BT = 100;
    private BluetoothAdapter mBluetoothAdapter;
    private TextView mTextView;
    private Button mButton;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initBlueTooth();

        registerReceiver();
    }

    /**
     * 注册蓝牙搜索广播
     */
    private void registerReceiver() {
        //异步搜索蓝牙设备
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        //搜索完成的广播
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    /**
     * 广播接收器
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //当搜索到附近蓝牙设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //从intent中获取设备信息
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 判断是否配对过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 添加到列表
                    mMyAdapter.addData("发现的设备：" + device.getName() + " address：" + device.getAddress());
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                mTextView.setText(mTextView.getText().toString() + "\n"
                        + "搜索完成 " + " isDiscovering ? " + mBluetoothAdapter.isDiscovering() + "\n");
            }
        }
    };

    /**
     * 初始化蓝牙
     */
    private void initBlueTooth() {
        //表示设备自身的蓝牙适配器（蓝牙无线装置）
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "本地设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }
        //若未启用蓝牙，申请开启蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            // 不做提示，强行打开
            // mBluetoothAdapter.enable();
            return;
        }
        //获取本地蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本地蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        mTextView.setText("my name is " + name + " , and my address is " + address);

        findHasConnect();
    }

    private void initView() {
        mContext = this;
        mTextView = findViewById(R.id.text_view);
        mButton = findViewById(R.id.btn_search);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);

        mButton.setOnClickListener(this);
    }

    /**
     * 6.0系统，使用蓝牙扫描，需要添加模糊定位的权限
     */
    private void requestPermission() {
        new RxPermissions(this).request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            // 已经获取权限
                            //是否在搜索，若正在搜索，先取消搜索
                            if (mBluetoothAdapter.isDiscovering()) {
                                mBluetoothAdapter.cancelDiscovery();
                            }
                            //开始搜索
                            if (mBluetoothAdapter.startDiscovery()) {
                                mTextView.setText(mTextView.getText().toString() + "\n" +
                                        "开始搜索...");
                            } else {
                                mTextView.setText(mTextView.getText().toString() + "\n" +
                                        "开始搜索失败");
                            }
                        } else {
                            // 未获取权限
                            Toast.makeText(mContext, "未获取权限", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 查询已配对的设备集
     */
    private void findHasConnect() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //如果有配对的设备
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mMyAdapter.addData("已配对的设备：" + device.getName() + " address：" + device.getAddress());
            }
        } else {
            mTextView.setText(mTextView.getText().toString() + "\n"
                    + "未获取到已配对设备\n");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_search) {
            requestPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            findHasConnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(mReceiver);
    }
}
