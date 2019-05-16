package com.octopus.test.server;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.octopus.test.R;

import java.util.List;

public class StartIntentService_Bug_5_0 {

    private static final String NOTIFICATION_TAG = "target";

    /**
     * 问题1。
     * 启动服务>=5.0的时候如果使用掩式启动，需要设置包名。如果不设置会有异常。
      * @param context context
     */
    public void send(Context context){
        final Intent serviceIntent=new Intent();
        serviceIntent.setAction("com.android.ForegroundService");
        serviceIntent.setPackage(context.getPackageName());//设置应用的包名
        context.startService(serviceIntent);
    }

    /**
     * 启动服务的大于5.0的时候也可以使用这如下方式将掩式启动转换成显示启动。
     * @param context context
     */
    public void setartService(Context context){
        Intent mIntent=new Intent();//辅助Intent
        mIntent.setAction("com.android.ForegroundService");
        final Intent serviceIntent=new Intent(getExplicitIntent(context,mIntent));
        context.startService(serviceIntent);
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * 问题2。
     * 如果服务不给启动应用使用，需要设置export = false
     */

    /**
     * 问题3。
     * Android 8.0+ 对 context.startService()做了限制。我们需要在8.0+上做如下显示处理。  将服务谁为前台服务，只有提醒通知。
     * 这个设置需要在onCreate中执行。
     */
    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    public void  setForegroundService(Service context) {
        //设定的通知渠道名称
        String channelName = "notification" ;
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_LOW;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel("10001", channelName, importance);
        channel.setDescription("aaa");
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "10001");
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setContentTitle("Title")//设置通知标题
                .setContentText("set service Background")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                //.setContentIntent() //点击通知的行为。
                //.setDeleteIntent() //通知默认行为。
                //.setFullScreenIntent() //全屏通知行为。
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        context.startForeground(1,builder.build());
    }

    /**
     * 问题4。
     * Android 通知打开唤起应用应用打开多次问题。
     * startComponentName 一般指的是MainActivity.class.getName()
     */
    public void startApp(Context context,String startComponentName){
        Intent launchIntent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(new ComponentName(context, startComponentName))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launchIntent.putExtra(NOTIFICATION_TAG, true);
        context.startActivity(launchIntent);
    }


    /**
     * 问题5。
     * 通知Intent覆盖问题。 调用
     *
     这里介绍的是Notification在状态栏的显示方式Flag属性：

          格式如: notification.flags |= Notification.FLAG_AUTO_CANCEL;

     1、FLAG_AUTO_CANCEL; //在通知栏上点击此后将自动清除，即不再显示在通知栏

     2、FLAG_FOREGROUND_SERVICE; //将此通知标记为前台服务

     2、FLAG_INSISTENT; //重复发出声音，直到用户响应此通知为止

     3、FLAG_ONGOING_EVENT; //将此通知放到通知栏的"Ongoing"即"正在运行"分组中，通知就会像QQ一样一直在状态栏中显示

     4、 FLAG_NO_CLEAR; //在点击了通知栏中的"清除通知"后，此通知仍然不清除，即一直保持在通知栏

     5、FLAG_ONLY_ALERT_ONCE; //标记声音或震动一次
     6、FLAG_SHOW_LIGHTS; //控制闪光
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification(Context context, Class clz ){

        //如果这里的requestCode是一样的。那么就会出现一个问题。就是每次发送新的requestCode = 100会覆盖之前的requestCode值。
        int requestCode = 1000;
        PendingIntent foregroundService = PendingIntent.getForegroundService(context, requestCode ++ , new Intent(context, clz), PendingIntent.FLAG_UPDATE_CURRENT);

        //通知服务的build构建者对象。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"111" );
        builder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("content title")
                .setContentText("aaaaaaaaaaaaaaaaaaa")
                .setOngoing(true)
                .setContentIntent(foregroundService
                );

        //通知服务。
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //通知的通道。
        NotificationChannel channel = new  NotificationChannel("1111","online",NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);

        //发送通知。
        notificationManager.notify(1,builder.build());
    }

}
