package com.octopus.test.app;

import android.util.Log;

import com.octopus.test.server.LogClient;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class OctopusApplication extends BaseApplication{

    public static String MAIN_PROCESS = "";
    public static String LOG_PROCESS = ":LogService";

    private static OctopusApplication sOctopusApplication;

    public OctopusApplication(){
    }

    public static OctopusApplication instance(){
        return sOctopusApplication;
    }

    @Override
    public void initRegisterProcess(HashMap<String, IProcessInit> process) {

        process.put(this.getPackageName(),new MainProcess());

        process.put(this.getPackageName() + LOG_PROCESS,  new IProcessInit() {
            @Override
            public void onCreate() {
                Log.i("OctopusApplication", "LogService:" + android.os.Process.myPid());
            }
        });
    }

    public class MainProcess implements IProcessInit{
        private AtomicBoolean sInit = new AtomicBoolean(false);
        private LogClient mClient;

        @Override
        public void onCreate() {
            if(sInit.compareAndSet(false,true)){
                sOctopusApplication = OctopusApplication.this;
                mClient = new LogClient();
            }
        }

        public LogClient getLoginClient(){
            return mClient;
        }
    }

    public IProcessInit getProcessContext(String serviceName){
       return process.get(this.getPackageName()+serviceName);
    }

}
