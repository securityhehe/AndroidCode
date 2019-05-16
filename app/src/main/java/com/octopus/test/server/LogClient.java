package com.octopus.test.server;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class LogClient {

    private Context sContext;
    private LogServiceConnect sConnect;

    public LogClient(){
        sConnect = new LogServiceConnect();
    }

    private static AtomicBoolean mServiceStarted  = new AtomicBoolean(false);

    private ILoggerService mService ;

    public class LogServiceConnect implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("name", name.getPackageName());
            if (name.getPackageName().equals(sContext.getPackageName())) {
                mService = ILoggerService.Stub.asInterface(service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("name", name.getPackageName());
            if (name.getPackageName().equals(sContext.getPackageName())) {
                mService = null;
                mServiceStarted.compareAndSet(true,false);
            }
        }
    }

    public void initLogService(final Context  context){

        this.sContext = context;
        if(context == null){
            Log.e("LogClient","LogClient bindService() context is null");
            return ;
        }

        if(mServiceStarted.compareAndSet(false,true)) {
            Intent intent = new Intent(context, LogService.class);
            intent.setPackage(context.getPackageName());
            context.startService(intent);
            context.bindService(intent, sConnect, Context.BIND_AUTO_CREATE);
        }

    }

    public void destroyService(){

        if(sContext == null){
            return;
        }

        if(mServiceStarted.compareAndSet(true,false)) {
            sContext.unbindService(sConnect);
            Intent intent = new Intent(sContext, LogService.class);
            intent.setPackage(sContext.getPackageName());
            sContext.stopService(intent);
        }
    }

}
