package com.octopus.test.server.message;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessageClient {
    private Messenger mServiceMessage;


    public void bindServer(Context context){
        Intent intent = new Intent(context,MessageService.class);
        context.bindService(intent,new MessageConnect(), Service.BIND_AUTO_CREATE);
    }

    Messenger messenger = new Messenger(new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //服务段发送的消息会在这个方法中接受到。


            if (msg != null && msg.arg1 == 1){
                if (msg.getData() == null){
                    return;
                }

                String content = (String) msg.getData().get("aaa");
                Log.d("zshh", "Message from server: " + content);
            }

        }
    });

    public class MessageConnect implements  ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
               mServiceMessage  = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
             mServiceMessage = null;
        }
    }

    public void sendMessage(String message){
        Message obtain = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putString("key",message);
        obtain.setData(bundle);
        obtain.replyTo = messenger;
        try {
            mServiceMessage.send(obtain);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
