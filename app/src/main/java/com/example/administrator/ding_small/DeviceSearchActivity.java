package com.example.administrator.ding_small;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.ding_small.Adapter.DeviceListAdapter;
import com.example.administrator.ding_small.Adapter.WifiAdapter;
import com.example.administrator.ding_small.HelpTool.MD5Utils;
import com.example.administrator.ding_small.HelpTool.PermissionHelper;
import com.example.administrator.ding_small.HelpTool.WifiAdmin;
import com.example.administrator.ding_small.Utils.SysApplication;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by CZK on 2017/12/19.
 */

public class DeviceSearchActivity extends Activity implements View.OnClickListener {
    private List<ScanResult> scanResults = null;
    private ListView wifiListView, blueListView;
    private WifiAdapter wifiAdapter;
    private BluetoothAdapter adapter;

    //权限检测类
    private PermissionHelper mPermissionHelper;
    public static final int ACCESS_FINE_LOCATION_CODE = 1;//SDcard权限
    public static final int ACCESS_COARSE_LOCATION_CODE = 2;//SDcard权限

    public static final int ACCESS_WIFI_STATE_CODE = 3;//SDcard权限

    private WifiManager wifiManager;

    boolean pression = false;
    private LoadingLayout loading;
    private LinearLayout wifi_buttom, bluetooth_buttom, refresh_layout;

    //更改语言所要更改的控件 wifi连接、蓝牙连接、返回、附近设备、刷新频率
    private TextView wifi_text, bluetooth_text, back_text, nearby_devices_text, refresh_text;

    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 2000;//2秒扫描完毕
    private DeviceListAdapter mDevListAdapter;
    private List<BluetoothDevice> mBleArray;
    private boolean mScanning;
    private Dialog mCameraDialog;
    private ImageView default_img, five_img, one_minute_img;
    private int refresh_num = 1;

    private int refreshTime = 30000;//默认30秒

    private static final String tokeFile = "tokeFile";//定义保存的文件的名称
    SharedPreferences sp = null;//定义储存源，备用
    String memid, token, UserSign, oldPass, newPass, ts, nameStr,sign;

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_device_search);
        SysApplication.getInstance().addActivity(this);
        getTimeCache();//获取修改的时间
        init();//初始化控件
        changeTextView();//更改语言
        wifiUtils();//wifi代码类
        blueToothUtils();//蓝牙代码类
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent=new Intent(DeviceSearchActivity.this,SelectDeviceActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void getTimeCache() {
        sp = this.getSharedPreferences(tokeFile, MODE_PRIVATE);
        refreshTime = Integer.parseInt(sp.getString("time", "30"))*1000;
    }
    private void changeTextView() {
        //wifi_text,bluetooth_text,back_text,nearby_devices_text,refresh_text
        if (Locale.getDefault().getLanguage().equals("en")) {
            wifi_text.setText("WIFI Connection");
            bluetooth_text.setText("Bluetooth Connection");
            back_text.setText("Back");
            nearby_devices_text.setText("Nearby Device");
            refresh_text.setText("Refresh Rate");
        }
    }

    private void init() {
        wifiListView = (ListView) findViewById(R.id.search_device_list);
        loading = findViewById(R.id.loading_layout);
        wifi_text = findViewById(R.id.wifi_text);
        bluetooth_text = findViewById(R.id.bluetooth_text);
        wifi_buttom = findViewById(R.id.wifi_buttom);
        bluetooth_buttom = findViewById(R.id.bluetooth_buttom);

        //wifi_text,bluetooth_text,back_text,nearby_devices_text,refresh_text
        back_text = findViewById(R.id.back_text);
        nearby_devices_text = findViewById(R.id.nearby_devices_text);
        refresh_text = findViewById(R.id.refresh_text);

        findViewById(R.id.wifi_layout).setOnClickListener(this);
        findViewById(R.id.bluetooth_layout).setOnClickListener(this);
        findViewById(R.id.footer).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);

        wifiListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DeviceSearchActivity.this, DeviceDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                System.out.println("WIFI：" + i);
                Bundle bundle1 = new Bundle();
                bundle1.putString("act", "DeviceSearch");
                bundle1.putString("device_mac", scanResults.get(i).BSSID);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
    }

    private void wifiUtils() {
        //授权
        mPermissionHelper = new PermissionHelper(this);
        //wifi管理器
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            openWifi();
        }
        one_handler.sendEmptyMessage(1);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    private void blueToothUtils() {
        starTimer.sendEmptyMessageDelayed(0, 100);
        mHandler = new Handler();

        //获取iBeacon设备
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            finish();
            return;
        }
        //判断蓝牙是否启动,关闭则启动
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // 设备不支持蓝牙
        }

        /**获取手机蓝牙设备*/
        // 打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
        mDevListAdapter = new DeviceListAdapter();
        mBleArray = new ArrayList<BluetoothDevice>();
        sendReceiver();//查找蓝牙
    }

    private void sendReceiver() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        this.registerReceiver(receiver, intentFilter);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        adapter.startDiscovery();

    }

    //打开wifi
    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    //扫描wifi
    public static List<ScanResult> getAllNetWorkList(Context context) {
        WifiAdmin mWifiAdmin = new WifiAdmin(context);
        // 开始扫描网络
        mWifiAdmin.startScan();
        return mWifiAdmin.getWifiList();
    }

    //每隔1000*30时间刷新一次wifi列表
    private Handler one_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            scanResults = new ArrayList<>();
            loading.setStatus(LoadingLayout.Loading);
            scanResults = getAllNetWorkList(DeviceSearchActivity.this);
            if (scanResults.size() > 0) {
                loading.setStatus(LoadingLayout.Success);
            }
            HashSet h = new HashSet(scanResults);
            scanResults.clear();
            scanResults.addAll(h);
            Collections.sort(scanResults, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult scanResult, ScanResult t1) {
                    return t1.level - scanResult.level;
                }
            });
            wifiAdapter = new WifiAdapter(DeviceSearchActivity.this, scanResults);
            wifiListView.setAdapter(wifiAdapter);
            System.out.println("刷新时间：" + refreshTime);
            int time=refreshTime/1000;
            //Toast.makeText(DeviceSearchActivity.this,"刷新时间：" + time,Toast.LENGTH_SHORT).show();
            one_handler.sendEmptyMessageDelayed(1, refreshTime);//默认30秒
        }
    };

    //判断wifi权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //如果请求成功，则进行相应的操作
                    //判断权限授权状态
                } else {
                    //如果请求失败
                    Toast.makeText(getApplicationContext(), "权限缺失，程序可能不能正常运行", Toast.LENGTH_SHORT).show();
                }
                break;
            case ACCESS_COARSE_LOCATION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //如果请求成功，则进行相应的操作

                } else {
                    //如果请求失败
                    Toast.makeText(getApplicationContext(), "权限缺失，程序可能不能正常运行", Toast.LENGTH_SHORT).show();
                    mPermissionHelper.startAppSettings();
                }
                break;
            case ACCESS_WIFI_STATE_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //如果请求成功，则进行相应的操作
                    one_handler.sendEmptyMessage(1);
                    pression = true;
                } else {
                    //如果请求失败
                    Toast.makeText(getApplicationContext(), "权限缺失，程序可能不能正常运行", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    boolean indexPression = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onPause() {

        super.onPause();
        indexPression = pression;
        pression = true;
        scanLeDevice(false);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();
        pression = indexPression;
        scanLeDevice(true);
    }

    @RequiresApi(api = VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onStop() {
        super.onStop();
        indexPression = pression;
        pression = true;

        scanLeDevice(false);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(receiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    // invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);

        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private LeScanCallback mLeScanCallback = new LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDevListAdapter.addDevice(device);
                    blueListView = findViewById(R.id.search_bluetooth_device_list);
                    blueListView.setAdapter(mDevListAdapter);
                    mDevListAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    //按钮点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wifi_layout://wifi
                wifi_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                bluetooth_text.setTextColor(ContextCompat.getColor(this, R.color.time_color));
                wifi_buttom.setBackgroundColor(ContextCompat.getColor(this, R.color.theme_color));
                bluetooth_buttom.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

                wifiListView = findViewById(R.id.search_device_list);
                wifiListView.setVisibility(View.VISIBLE);
                blueListView = findViewById(R.id.search_bluetooth_device_list);
                blueListView.setVisibility(View.GONE);
                break;
            case R.id.bluetooth_layout://蓝牙
                wifi_text.setTextColor(ContextCompat.getColor(this, R.color.time_color));
                bluetooth_text.setTextColor(ContextCompat.getColor(this, R.color.theme_color));
                wifi_buttom.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                bluetooth_buttom.setBackgroundColor(ContextCompat.getColor(this, R.color.theme_color));

                wifiListView = findViewById(R.id.search_device_list);
                wifiListView.setVisibility(View.GONE);
                blueListView = findViewById(R.id.search_bluetooth_device_list);
                blueListView.setVisibility(View.VISIBLE);
                blueListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(DeviceSearchActivity.this, DeviceDetailActivity.class);
                        System.out.println("蓝牙：" +  mBleArray.get(i).getAddress());
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("device_mac", mBleArray.get(i).getAddress());
                        bundle1.putString("act","DeviceSearch");
                        intent.putExtras(bundle1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.footer://刷新频率
                setDialog();
                break;
            case R.id.default_layout:
                default_img.setVisibility(View.VISIBLE);
                five_img.setVisibility(View.GONE);
                one_minute_img.setVisibility(View.GONE);
                refresh_num = 1;
                refreshTime = 30000;//30秒
                if (Locale.getDefault().getLanguage().equals("en")){
                    refresh_text.setText("Default");
                }else{
                    refresh_text.setText("默认刷新频率");
                }
                mCameraDialog.dismiss();
                break;
            case R.id.five_layout:
                five_img.setVisibility(View.VISIBLE);
                default_img.setVisibility(View.GONE);
                one_minute_img.setVisibility(View.GONE);
                refresh_num = 2;
                refreshTime = 5000;//5秒
                if (Locale.getDefault().getLanguage().equals("en")){
                    refresh_text.setText("Five Second");
                }else {
                    refresh_text.setText("5秒刷新频率");
                }
                mCameraDialog.dismiss();
                break;
            case R.id.one_minute_layout:
                five_img.setVisibility(View.GONE);
                default_img.setVisibility(View.GONE);
                one_minute_img.setVisibility(View.VISIBLE);
                refresh_num = 3;
                refreshTime = 1000 * 60;//1分钟
                if (Locale.getDefault().getLanguage().equals("en")){
                    refresh_text.setText("One Minute");
                }else {
                    refresh_text.setText("1分钟刷新频率");
                }
                mCameraDialog.dismiss();
                break;
            case R.id.back:
                Intent intent=new Intent(DeviceSearchActivity.this,SelectDeviceActivity.class);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    //默认30秒搜一次
    private Handler starTimer = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                scanLeDevice(true);//开始扫描ibeacon
                // mDevListAdapter = new DeviceListAdapter();
                sendReceiver();//查找蓝牙
                if (mDevListAdapter != null) {
                    blueListView = findViewById(R.id.search_bluetooth_device_list);
                    blueListView.setAdapter(mDevListAdapter);
                    mDevListAdapter.notifyDataSetChanged();
                }
                starTimer.sendEmptyMessageDelayed(0, refreshTime);//默认30秒
            }

        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    mDevListAdapter.addDevice(device);
                    System.out.println(device.getName() + "-" + device.getAddress());
                }
            }
        }
    };

    //蓝牙适配器
    class DeviceListAdapter extends BaseAdapter {
        private ViewHolder viewHolder;
        private String[] str = null;

        public DeviceListAdapter() {
        }

        public void addDevice(BluetoothDevice device) {
            if (device.getName() != null) {
                if (!mBleArray.contains(device)) {
                    mBleArray.add(device);
                }
            }
        }

        public void clear() {
            if (mBleArray != null) {
                mBleArray.clear();
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mBleArray.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return mBleArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            viewHolder = new ViewHolder();
            View contentView = null;
            if (contentView == null) {
                contentView = LayoutInflater.from(DeviceSearchActivity.this).inflate(
                        R.layout.wifi_item, null);

                viewHolder.number = (TextView) contentView
                        .findViewById(R.id.number);
                viewHolder.mac = (TextView) contentView
                        .findViewById(R.id.mac);
                viewHolder.ssid = (TextView) contentView
                        .findViewById(R.id.ssid);
                contentView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) contentView.getTag();
            }
            BluetoothDevice device = mBleArray.get(position);
            String devName = device.getName();
            if (devName != null && !"".equals(devName) && devName.length() > 0) {
                viewHolder.ssid.setText("" + devName);
            } else {
                viewHolder.ssid.setText("unknow_device" + (position + 1));
            }
            viewHolder.mac.setText(device.getAddress());
            viewHolder.number.setText(position + 1 + "");
            return contentView;
        }

        class ViewHolder {
            TextView number, mac, ssid;
        }
    }

    //底部弹出菜单
    private void setDialog() {
        LinearLayout root = null;
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.refresh_item, null);
        //初始化视图
        root.findViewById(R.id.default_layout).setOnClickListener(this);
        root.findViewById(R.id.five_layout).setOnClickListener(this);
        root.findViewById(R.id.one_minute_layout).setOnClickListener(this);

        default_img = root.findViewById(R.id.default_img);
        five_img = root.findViewById(R.id.five_img);
        one_minute_img = root.findViewById(R.id.one_minute_img);

        TextView default_text = root.findViewById(R.id.default_text);
        TextView five_text = root.findViewById(R.id.five_text);
        TextView one_minute_text = root.findViewById(R.id.one_minute_text);
        TextView refresh_text = root.findViewById(R.id.refresh_text);
        if (Locale.getDefault().getLanguage().equals("en")) {
            default_text.setText("Default");
            five_text.setText("Five Second ");
            one_minute_text.setText("One Minute");
            refresh_text.setText("Refresh Rate");
        }

        //判断哪个之前被选中
        if (refresh_num == 1) {
            default_img.setVisibility(View.VISIBLE);
            five_img.setVisibility(View.GONE);
            one_minute_img.setVisibility(View.GONE);
        } else if (refresh_num == 2) {
            default_img.setVisibility(View.GONE);
            five_img.setVisibility(View.VISIBLE);
            one_minute_img.setVisibility(View.GONE);
        } else {
            default_img.setVisibility(View.GONE);
            five_img.setVisibility(View.GONE);
            one_minute_img.setVisibility(View.VISIBLE);
        }

        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.DialogAnimation); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }
}
