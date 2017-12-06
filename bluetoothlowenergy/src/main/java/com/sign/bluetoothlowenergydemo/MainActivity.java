package com.sign.bluetoothlowenergydemo;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 100;
    private DeviceListAdapter mDeviceListAdapter;
    private Handler mHandler;
    private boolean mScanning;
    //一次扫描时间为10秒
    private static final long SCAN_PERIOD = 10000;
    //扫描结果回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private List<BluetoothDevice> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

        initListener();

        checkWhetherSupportBLE();

        checkPermission();
    }

    private void initListener() {
        //点击跳转连接设备
        mDeviceListAdapter.setMyAdapterListener(new DeviceListAdapter.MyAdapterListener() {
            @Override
            public void onItemClick(int position) {
                BluetoothDevice device = mDeviceListAdapter.getList().get(position);
                if (device == null) {
                    return;
                }
                Intent intent = new Intent(mContext, DeviceControlActivity.class);
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
            }
        });

        //已存在的设备集合
        devices = mDeviceListAdapter.getList();
        mHandler = new Handler();
        mLeScanCallback =
                new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (device != null) {
                                    //过滤同一设备
                                    if (!devices.contains(device)) {
                                        mDeviceListAdapter.addData(device);
                                    }
                                }
                            }
                        });
                    }
                };
    }

    private void initView() {
        mTextView = findViewById(R.id.text_view);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDeviceListAdapter = new DeviceListAdapter();
        mRecyclerView.setAdapter(mDeviceListAdapter);
    }

    /**
     * 请求定位权限
     */
    private void checkPermission() {
        new RxPermissions((Activity) mContext).request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean granted) {
                        if (granted) {
                            initBlueTooth();
                        } else {
                            // 未获取权限
                            Toast.makeText(mContext, "未获取定位权限", Toast.LENGTH_LONG).show();
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

    private void initBlueTooth() {
        //BluetoothAdapter代表设备自己的蓝牙适配器（蓝牙无线电）
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //设备是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "该设备不支持蓝牙功能", Toast.LENGTH_LONG).show();
            finish();
        }

        //蓝牙是否启用，若未启用，请求用户启用
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        scanLeDevice(true);
    }

    /**
     * 扫描低功耗蓝牙设备
     * 您只能扫描蓝牙LE设备或扫描经典蓝牙设备，如蓝牙中所述  您无法同时扫描Bluetooth LE和传统设备。
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            //Because scanning is battery-intensive, you should observe the following guidelines:
            //As soon as you find the desired device, stop scanning.找到所需设备停止扫描
            //Never scan on a loop, and set a time limit on your scan.切勿扫描循环，并在扫描上加上时间限制
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mTextView.setText("扫描结束");
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            //如果只想扫描特定类型的外设，则可以改为调用startLeScan（UUID []，BluetoothAdapter.LeScanCallback）
            //提供指定您的应用程序支持的GATT服务的UUID对象数组
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mTextView.setText("扫描中...");
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mTextView.setText("扫描结束");
        }
        invalidateOptionsMenu();
    }

    /**
     * 设备是否支持低功耗蓝牙
     */
    private void checkWhetherSupportBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "此设备不支持BLE（低功耗蓝牙）", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //启用蓝牙
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            scanLeDevice(true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null) {
            scanLeDevice(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_start:
                mDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        if (mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_start).setVisible(false);
        } else {
            menu.findItem(R.id.menu_start).setVisible(true);
            menu.findItem(R.id.menu_stop).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
