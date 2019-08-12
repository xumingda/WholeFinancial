package com.ciba.wholefinancial.base;


import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.ciba.wholefinancial.service.LocationService;
import com.ciba.wholefinancial.util.LogUtils;
import com.ciba.wholefinancial.util.MyExceptionHandler;
import com.ciba.wholefinancial.util.TTSUtility;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;

public class BaseApplication extends Application {
    /** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
    private static BaseApplication mInstance;
    /** 主线程ID */
    private static int mMainThreadId = -1;
    /** 主线程 */
    private static Thread mMainThread;
    /** 主线程Handler */
    private static Handler mMainThreadHandler;
    /** 主线程Looper */
    private static Looper mMainLooper;
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
//        SpeechUtility speechUtility=SpeechUtility.createUtility(this, "appid=5cefcc06," + SpeechConstant.FORCE_LOGIN +"=true");
//        Log.e("SpeechUtility","SpeechUtility111:"+speechUtility);






        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        mInstance = this;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        /*初始化volley*/
        MyVolley.init(this);
        MyExceptionHandler.create(this);


        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

    }

    //旋转适配,如果应用屏幕固定了某个方向不旋转的话(比如qq和微信),下面可不写.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenAdapterTools.getInstance().reset(this);
    }
    public static BaseApplication getApplication() {
        return mInstance;
    }

    /** 获取主线程ID */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /** 获取主线程的handler */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /** 获取主线程的looper */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }
    public static TextToSpeech textToSpeech = null;//创建自带语音对象
    public static String todayOrderSucCount;//	今日订单数
    public static String todayTurnover	;//今日营业额
    public static String qrPayTurnover	;//扫码收

    public static String getTodayOrderSucCount() {
        return todayOrderSucCount;
    }

    public static void setTodayOrderSucCount(String todayOrderSucCount) {
        BaseApplication.todayOrderSucCount = todayOrderSucCount;
    }

    public static String getTodayTurnover() {
        return todayTurnover;
    }

    public static void setTodayTurnover(String todayTurnover) {
        BaseApplication.todayTurnover = todayTurnover;
    }

    public static String getQrPayTurnover() {
        return qrPayTurnover;
    }

    public static void setQrPayTurnover(String qrPayTurnover) {
        BaseApplication.qrPayTurnover = qrPayTurnover;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }



    public static void startAuto(String data) {
        TTSUtility.getInstance(getApplication()).speaking(data);
    }
}