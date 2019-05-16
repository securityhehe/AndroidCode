package com.octopus.test.server.pool;

import android.os.RemoteException;

public class LoggerBinder extends Logger.Stub{

    @Override
    public void logD(String message) throws RemoteException {

    }

}
