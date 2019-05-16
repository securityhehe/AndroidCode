package com.octopus.test.server.pool;

import android.os.RemoteException;

public class IMServiceBinder extends IMService.Stub{

    @Override
    public void sendMessage(String message) throws RemoteException {

    }

    @Override
    public void receiceMesage(String message) throws RemoteException {

    }

    @Override
    public void connect(String message) throws RemoteException {

    }

    @Override
    public void disConnect() throws RemoteException {

    }

    @Override
    public void onError(String errorMessage, int errorCode) throws RemoteException {

    }

}
