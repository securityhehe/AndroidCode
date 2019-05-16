package com.octopus.test.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import java.util.HashMap;
import java.util.List;

public abstract class BaseApplication extends Application {

    protected HashMap<String,IProcessInit> process = new HashMap<>();

    /**
     * 进程初始化流程。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = "";
        int processId = Process.myPid();

        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info: runningAppProcesses){
            if(processId == info.pid){
               processName = info.processName;
            }
        }

        initRegisterProcess(process);
        IProcessInit iProcessInit = process.get(processName);
        if(iProcessInit!=null){
            iProcessInit.onCreate();
        }
    }

    public abstract void initRegisterProcess(HashMap<String, IProcessInit> process);

}
