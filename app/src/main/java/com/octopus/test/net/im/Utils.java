package com.octopus.test.net.im;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    /**
     * 是否链接网络。
     * @param context 上下文对象。
     * @return 返回是否链接。
     */
    public static boolean isConentNet(Context context){

        if(context == null){
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
            return false;
        }

        //如果sdk 版本大于 Android M,使用新的api进行判断。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if(networkCapabilities == null){
                return false;
            }
            return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }else {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(activeNetworkInfo == null){
                return false;
            }
            return activeNetworkInfo.isConnected();
        }

    }

    /**
     * 判断网络是否可到服务， 并返回ttl时间。
     * 如下 String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
     */
    public static NetInfo connectServerNetInfo(Context context,String serverAddress){
        NetInfo info = new NetInfo();
        InputStream input = null;
        BufferedReader in = null;
        try {
            info.ip = serverAddress;
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + serverAddress);// ping网址3次
            input = p.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content;
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            info.pingInfo  = stringBuffer.toString();
            int status = p.waitFor();
            if (status == 0) {
                info.isSuccess = true;
            } else {
                info.isSuccess = false;
            }
        } catch (IOException e) {
            info.isSuccess = false;
        } catch (InterruptedException e) {
            info.isSuccess = false;
        } finally {
            close(in);
            close(input);
        }
        return info;
    }

    public static void close(Closeable closeable){
       if(closeable!=null) {
           try {
               closeable.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }

    public static class NetInfo{
        String ip;
        String pingInfo;
        boolean isSuccess;
    }
}


