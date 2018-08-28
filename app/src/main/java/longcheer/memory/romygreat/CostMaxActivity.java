package longcheer.memory.romygreat;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import static android.hardware.SensorManager.SENSOR_DELAY_FASTEST;
/**
 * Created by luominming on 2017/10/18.
 */
public class CostMaxActivity extends Activity  implements SurfaceHolder.Callback{
    private String TAG = "CostMaxActivity";
    private Button Disbrghtness, closeLight,GPSButton,mMemoryButton;
    private boolean mIsLight = false;
    private CameraManager cmeraManager;
    private boolean mIsFirst = true;
    private Vibrator vibrator;
    private BatteryReceiver batteryReceiver;
    private LocationManager locManager;
    private BluetoothAdapter bluetoothAdapter;
    private boolean mCpuFlag = true;
    private TextView mTextTime;
    private EditText editText;
    private long beigintime;
    private int percentFisrt;
    private Camera camera=null;
    private  int current=10;
    private TextView CurrenScale;
    private CameraManager mcameraserviceManger;
    private Timer timer=new Timer();
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            locManager.setTestProviderEnabled(provider, true);
        }

        @Override
        public void onProviderDisabled(String provider) {
            locManager.setTestProviderEnabled(provider, true);

        }
    };
    private SensorManager sensorManager;
    private Sensor sensor;
    int count=0;
    private File file;
    private MediaPlayer mMediaPlayer,mMediaPlayer1,mMediaPlayer2, mMediaPlayer3,mMediaPlayer4;
    private SensorListener sensorListener=null;
    private SensorListener temprature;
    private Sensor sensorTemprature;
    private MediaPlayer player;
    private SurfaceView surface;
    private SurfaceHolder surfaceHolder;
    private Button play;
    private boolean isRegsieter=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost);
        initView();
        CurrenScale.setKeepScreenOn(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getSystemService(Context.POWER_SERVICE);
        getBaterypercent();
        createMedia();
        flashControler(CostMaxActivity.this);//flash手电筒
        closeLight.setClickable(false);
        surfaceHolder=surface.getHolder();//SurfaceHolder是SurfaceView的控制接口
        surfaceHolder.addCallback(CostMaxActivity.this);//因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        surfaceHolder.setFixedSize(320, 220);//显示的分辨率,不设置为视频默认
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//Surface类型
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Log.i(TAG, "onClick: playPrepare");
             //  playPrepare();
//                ShellUtils shellUtils=new ShellUtils();
             //   shellUtils.
//                ShellUtils.checkRootPermission();
//                Log.i(TAG, "onClick: ShellUtils"+ShellUtils.checkRootPermission());
//               ShellUtils.CommandResult result= ShellUtils.execCommand("",false);
//                Log.i(TAG, "onClick:ShellUtils:sucessMsg "+result.successMsg+"错误信息"+result.errorMsg);
               player.start(); //添加视屏
//                Log.i(TAG, "onClick:  play");
                if(file.exists()==false){
                    dayin("请将mp4格式的视屏放到存储根目录,并重命名为test.mp4");}}
        });
        mMemoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CostMaxActivity.this,SyntasklongcheerActivity.class);
                startActivity(intent);
            }
        });
        GPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openLocation();
            }
        });
        Disbrghtness.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
              //  int percentagePower=Integer.valueOf(editText.getText().toString()) ;
                mTextTime.setText("预计放电时间：计算中----");
                closeLight.setClickable(true);
                mCpuFlag = true;
                startAlarm(true);
                changeAppBrightness(CostMaxActivity.this,255);
                openBluetooth(true);
                openOrCloseWifi(CostMaxActivity.this, true);
               // openLocation();
                getBaterypercent();
                openVibrator(1);
                CostCpu();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                setLight(true);

                openMagnecticSensor();
                openTemprature();
                notificationLight();
                wakeLockMethod();
//                recorder(true);
               // if(player.)
//              notificationLight();
               // PlayMusicThread.start();
               // playMusic();
                Log.i(TAG, "onClick: 开始放电");
            }
        }) ;
        closeLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAllPower();
                CurrenScale.setKeepScreenOn(false);
            }
        });
    }
    private void playMusicPrepare() {
        player=new MediaPlayer();
//        player.setAudioStreamType_);
        player.setLooping(true);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(surfaceHolder);
    }
    private void createMedia() {
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer1 = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer2 = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer3 = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer4= MediaPlayer.create(this, getSystemDefultRingtoneUri());
    }
    private void closeAllPower() {
        openBluetooth(false);
        vibrator.cancel();
        openOrCloseWifi(CostMaxActivity.this, false);
        mCpuFlag = false;
     // unregisterReceiver(batteryReceiver);
       // stopMusic();
        player.pause();
      //  closeLight.setClickable(false);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        changeAppBrightness(CostMaxActivity.this,80);
        setLight(false);
        vibrator.cancel();
        startAlarm(false);
        if (sensorListener!=null){
            sensorManager.unregisterListener(sensorListener);
        }if(temprature!=null){
            sensorManager.unregisterListener(temprature);
        }
        Log.i(TAG, "closeAllPower: 停止放电");
        mTextTime.setText("预计放电时间：进行中-----");
    }
    private void CostCpu() {
        MyThread MyThread = new MyThread();
        MyThread MyThread1 = new MyThread();
        MyThread MyThread2 = new MyThread();
        MyThread myThread3 = new MyThread();
        MyThread myThread4 = new MyThread();
        MyThread myThread5 = new MyThread();
        MyThread myThread6 = new MyThread();
        MyThread myThread7 = new MyThread();
//        MyThread myThread8 = new MyThread();
//        MyThread myThread9 = new MyThread();
//        MyThread myThread0 = new MyThread();
        MyThread.start();
        MyThread2.start();
        MyThread1.start();
        myThread3.start();
        myThread4.start();
        myThread5.start();
        myThread6.start();
        myThread7.start();
//        myThread8.start();
//        myThread9.start();
//        myThread0.start();
    }
    //修改播放音乐
    private void openLocation() {
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //打开手机位置定位
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 0);
        }else{
            dayin("您已经打开了GPS");
        }
        getBaterypercent();
//        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            //打开手电筒方法使用意图
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, 0);
//        }
//        getBaterypercent();
//        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            //打开手电筒方法使用意图
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivityForResult(intent, 0);
//        }
//        getBaterypercent();
    }
    //打开蓝牙的方法
    private void openBluetooth(boolean t) {
        changeAppBrightness(CostMaxActivity.this, 254);
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (!t) {
            bluetoothAdapter.disable();
        }
    }
    private void initView() {
        Disbrghtness = (Button) findViewById(R.id.Disbrghtness);
        closeLight = (Button) findViewById(R.id.closeLight);
        editText=findViewById(R.id.editcost);
        CurrenScale=findViewById(R.id.CurrenScale);
        GPSButton=findViewById(R.id.GPSButton);
        mTextTime=findViewById(R.id.time1);
        mMemoryButton=findViewById(R.id.memory);
        surface=(SurfaceView)findViewById(R.id.surfaceView);
        play=findViewById(R.id.playVedio);
    }
    // 根据亮度值修改当前window亮度
    public void changeAppBrightness(Context context, int brightness) {
        Window window = ((Activity) context).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        window.setAttributes(lp);
    }
    //获取手机放电的比例
    public void getBaterypercent() {
        batteryReceiver = new BatteryReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, intentFilter);
        isRegsieter=true;
    }
    public int getProcessCpuRate() {
        StringBuilder tv = new StringBuilder();
        int rate = 0;
        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                }
                else {
                    try {
                        String[] CPUusr = Result.split("%");
                        tv.append("USER:" + CPUusr[0] + "\n");
                        String[] CPUusage = CPUusr[0].split("User");
                        String[] SYSusage = CPUusr[1].split("System");
                        tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                        tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");
                        rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    }catch ( Exception e){
                        e.printStackTrace();
                        Log.i(TAG, "getProcessCpuRate: andoid o 出现了BUG,也就是android 8出现了BUG");//注意这里的代码
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(rate + "");
        return rate;
    }
    public void setLight(boolean enable) {
        try {
            Log.i(TAG, "setLight: try");
            String[] str= mcameraserviceManger.getCameraIdList();
            for(String s:str){
                Log.i(TAG, "onCreate:cameraID "+s);
            }
            mcameraserviceManger.setTorchMode("0",enable);
        } catch (CameraAccessException e) {
            Log.i(TAG, "onCreate: Exeception");
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "setLight: mcameraserviceManger.setTorchMode(\"0\",enable);");
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        player=new MediaPlayer();
////        player.setAudioStreamType_);
//        player.setLooping(true);
//        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        player.setDisplay(surfaceHolder);
        playMusicPrepare();
        try {
            String path="/sdcard/test.mp4";
           file=new File(path);
            Log.i(TAG, "surfaceCreated: file"+file.toString());
            Log.i(TAG, "surfaceCreated: player"+player);
//            if(file.exists()==false){
//                dayin("文件不存在");return;
//            }
            player.setDataSource(file.getAbsolutePath());
//            player.setDataSource("/sdcard/DCIM/Camera/test.mp4");
            Log.i(TAG, "surfaceCreated: ");
            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "surfaceCreated: 找不到资源player"+ player+ e.toString());
         //   dayin("请放入一段视频至于内置SD卡");
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: ");
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(player.isPlaying()){
            player.stop();}
        //if(batteryReceiver!=null)
        {
            if(isRegsieter)
            {  unregisterReceiver(batteryReceiver);
            isRegsieter=false;
            }
        }
        Log.i(TAG, "surfaceDestroyed: ");
        player.release();
    }
    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mIsFirst) {
                beigintime = System.currentTimeMillis();
                Log.i(TAG, "onReceive: begintime" + beigintime);
                int current = intent.getExtras().getInt("level");//获得当前电量
                int total = intent.getExtras().getInt("scale");//获得总电量
                percentFisrt = current * 100 / total;
                mIsFirst = false;
            }
            int percentagePower=Integer.valueOf(editText.getText().toString()) ;
            if(percentagePower>current){
                closeAllPower();
                dayin("目标电量不能低于现有电量");
                return;
            }
            current = intent.getExtras().getInt("level");//获得当前电量
            int total = intent.getExtras().getInt("scale");//获得总电量
            int percent = current * 100 / total;
            if (percentFisrt > percent) {
                percentFisrt = percent;
                long timeTotal = System.currentTimeMillis() - beigintime;
                Log.i(TAG, "onReceive: 时间timetotal" + timeTotal);
                long t = (timeTotal / 1000) / 60 * (percent-percentagePower);
                mTextTime.setText("预计放电时间:"+t+"分钟，" +
                        "仅供参考");
                Log.i(TAG, "onReceive: 剩余时间:" + t + "分钟");
                mIsFirst = true;
                Log.i(TAG, "onReceive: ");
            }
          //  Log.i(TAG, "onReceive: endtime使用时间" + (System.currentTimeMillis() - beigintime));
            CurrenScale.setText(" 当 前 电 量  ：    "+current+"%");
        }
    }
    public void openOrCloseWifi(Context context, boolean state) {
        WifiManager mWifiManager = (WifiManager)
                context.getSystemService(Context.WIFI_SERVICE);
        mWifiManager.setWifiEnabled(state);
    }
    private void openVibrator(int r) {

        this.vibrator.vibrate(new long[]{1000L, 1000L, 1000L, 1000L}, r);
    }
    private class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (mCpuFlag) {
                getProcessCpuRate();
              //  playMusic();
                for (int i = 0; i < 1024; i++) {
                    int t = 5;
                    int g = 9;
                    double de = t + g;
                    ArrayList list = new ArrayList(1024);
                    ArrayList list1 = new ArrayList(2048);
                    Intent inttent = new Intent();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void flashControler(Context mcontext){
        mcameraserviceManger= (CameraManager) mcontext.getSystemService(Context.CAMERA_SERVICE);

    }
    public void dayin(String toast){
        Toast.makeText(CostMaxActivity.this,toast,Toast.LENGTH_SHORT).show();
    }
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
    private void startAlarm(boolean ennable) {
        if (ennable){
            mMediaPlayer.setLooping(ennable);
            mMediaPlayer1.setLooping(ennable);
            mMediaPlayer2.setLooping(ennable);
            mMediaPlayer3.setLooping(ennable);
            mMediaPlayer3.setLooping(ennable);
            Log.i(TAG, "startAlarm: Enable"+ennable);
            mMediaPlayer.start();
            mMediaPlayer1.start();
            Log.i(TAG, "startAlarm: mMediaplayer"+mMediaPlayer);
            mMediaPlayer2.start();
            mMediaPlayer3.start();
            mMediaPlayer4.start();
        } else {
            Log.i(TAG, "startAlarm: enable"+ennable);
            mMediaPlayer.pause();
            mMediaPlayer1.pause();
            mMediaPlayer2.pause();
            mMediaPlayer3.pause();
            mMediaPlayer4.pause();
//            mMediaPlayer.release();
//            mMediaPlayer1.release();//不能释放,释放报错
//            mMediaPlayer2.release();
//            mMediaPlayer3.release();
//            mMediaPlayer4.release();
            Log.i(TAG, "startAlarm:pause ");
        }
    }
    private void openMagnecticSensor(){
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(CostMaxActivity.this,sensor);
        sensorManager.registerListener( sensorListener=new SensorListener() {
            @Override
            public void onSensorChanged(int sensor, float[] values) {
              //  sensor.getName();
              //  Log.i(TAG, "onSensorChanged:传感器名称： "+sensor.getName());
                if( count++ == 20){ //磁场传感器很敏感，每20个变化，显示一次数值
                    double value = Math.sqrt(values[0]*values[0] + values[1]*values[1]
                            +values[2]*values[2]);
                    Log.i(TAG, "onSensorChanged: "+value);
            String str = String.format("X:%8.4f , Y:%8.4f , Z:%8.4f \n总值为：%8.4f",
                    values[0],values[1],values[2],value);
            count = 1;
            Log.i(TAG, "onSensorChanged: "+str);
                }
            }
            @Override
            public void onAccuracyChanged(int sensor, int accuracy) {
            }
        },SensorManager.SENSOR_MAGNETIC_FIELD,SENSOR_DELAY_FASTEST);
        try{
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);}
        catch (Exception e){e.printStackTrace();}
    }
    public void openTemprature(){
        Log.i(TAG, "openTemprature: ");
        try{
            sensorTemprature = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
            Log.i(TAG, "openTemprature: "+"try");
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i(TAG, "openTemprature:当前温度为传感器 "+sensorTemprature);
        }
        temprature=new SensorListener() {
            @Override
            public void onSensorChanged(int sensor, float[] values) {
                float temperature=values[0];
                Log.i(TAG, "onSensorChanged: 当前温度为"+(String.valueOf(temperature)+"°C"));
            }
            @Override
            public void onAccuracyChanged(int sensor, int accuracy) {
            }
        };
    }
    public synchronized void  notificationLight(){
        //通知，还包括通知灯测试
        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder=new Notification.Builder(this);
        builder.setContentInfo("ContentInfo");
        builder.setSubText("研发中心");//12月21日添加
        builder.setContentText("龙旗测试部六组骆敏明设计，欢迎使用");
        builder.setSmallIcon(R.mipmap.logolongcheer);
        builder.setTicker("Tricker");
        Intent  intent=new Intent(this,CostMaxActivity.class);
        builder.setWhen(System.currentTimeMillis());
        PendingIntent pendingIntent=PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=builder.build();
        notification.ledARGB= Color.RED;
        notification.color=Color.BLACK;
        notification.ledOffMS=20;
        Log.i(TAG, "notificationLight: RED");
        notificationManager.notify(1,notification);
//        notificationManager.notify(3,notification);
        Log.i(TAG, "notificationLight: END");
    }
    public void wakeLockMethod(){
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock  wakeLock=powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyWakelockTag");
     // wakeLock.acquire();
        Log.i(TAG, "wakeLockMethod: ");
    }
    public void recorder(boolean record){
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String PATH_NAME="/sdcard/test.mp3";
        recorder.setOutputFile(PATH_NAME);
        try {
            recorder.prepare();
        } catch (IOException e) {
            //hello
            e.printStackTrace();
        }
        if(record)
        { recorder.start(); }else{
            recorder.stop();
        }  // 开始录音
    }
}