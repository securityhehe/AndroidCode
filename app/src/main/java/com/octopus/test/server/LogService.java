package com.octopus.test.server;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.octopus.test.R;

public class LogService extends Service {

    private static final String TAG = "LogService";


    private IBinder loggerService = new ILoggerService.Stub() {
        @Override
        public void logD(String s) throws RemoteException {
            Log.i("LogService",s);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                setForegroundService();
        }
        Log.i(TAG,"LogService -> onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"LogService -> onBind");
        return loggerService;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"LogService -> onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"LogService -> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "LogService -> onDestroy");
        super.onDestroy();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    /**
     * 将服务谁为前台服务，只有提醒通知。
     */
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public void  setForegroundService() {
        //设定的通知渠道名称
        String channelName = "notification" ;
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel("10001", channelName, importance);
        channel.setDescription("aaa");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "10001");
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setContentTitle("Title")//设置通知标题
                .setContentText("set service Background")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                //.setContentIntent() //点击通知的行为。
                //.setDeleteIntent() //通知默认行为。
                //.setFullScreenIntent() //全屏通知行为。
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(1,builder.build());
    }

}



