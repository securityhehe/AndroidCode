package com.octopus.test.server.pool;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.CountDownLatch;

public class BinderPool {

    private static final int MESSAGE = 1;
    private static final int CENSUS = 2;
    private static final int LOGGER = 3;
    private BinderPools pools ;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    public void bindServer(Context context) {
        Intent intent = new  Intent(context,PoolService.class);
        context.bindService(intent, new PoolConnect(), Service.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class PoolConnect implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pools  = BinderPools.Stub.asInterface(service);
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            pools = null;
        }
    }

    public class  PoolsBinder extends BinderPools.Stub{
        @Override
        public IBinder queryBinder(int code) throws RemoteException {
            return queryBind(code);
        }
    }

    public IBinder queryBind(int code){
        IBinder mBinder = null;
        if(code == MESSAGE ){
            mBinder = new IMServiceBinder();
        }else if (code == CENSUS){
            mBinder = new CensusServiceBinder();
        }else if(code == LOGGER){
            mBinder = new LoggerBinder();
        }
        return mBinder;
    }
}

