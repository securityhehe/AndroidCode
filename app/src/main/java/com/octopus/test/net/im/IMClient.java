package com.octopus.test.net.im;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IMClient {

    //心跳线程。
    public HeartBeatRunnable heartBeatRunnable;
    //访问过的服务端List列表。
    private List<ServerRouteBean> mQueryedServerList = new ArrayList<>();
    //当前没有访问过的服务端List列表。
    private List<ServerRouteBean> mCurrentServerList = new ArrayList<>();
    //IM回调接口。
    private IMCallback callback;
    private Context sConext;

    private HandlerThread thread  = new HandlerThread("IMClient-Thread");
    private Handler mHandler = new Handler(thread.getLooper());

    public IMClient(){
        thread.start();
    }

    public interface ImProvider{

        HeartBeatRunnable getHeartBeatRunable();

        IMCallback getImCallback();

        List<ServerRouteBean> getServerAddress();

        Context getContext();
    }

    public void build(){


    }


    private class HeartBeatRunnable implements Runnable{
        @Override
        public void run() {

        }
    }

    private class ServerRouteBean {
        public URL address;
    }

    private interface IMCallback {

    }

    private class ImServerAddressBean {

    }



    private ServerRouteBean getSerRouterBean(){
        //判断网络是否可用。
        ServerRouteBean address = null;
        //判断mCurrentServerList的大小是否大于0。
        if(mCurrentServerList.size() >0 ){
            //随机服务器地址中的任意一个。
            selectRouterAddress();
        }else{
            mCurrentServerList.addAll(mQueryedServerList);
            if(mCurrentServerList.size() > 0 ){
                selectRouterAddress();
            }
        }
        return address;
    }

    private ServerRouteBean selectRouterAddress(){
        ServerRouteBean address;
        Random random = new Random();
        int index = (int)(random.nextDouble() * mCurrentServerList.size());
        address = mCurrentServerList.remove(index);
        mQueryedServerList.add(address);
        return address;
    }

    public void connect(ImServerAddressBean bean){

    }

    public void reConnect(){

    }

    public void close(){

    }

    public void onError(int code){

    }

}
