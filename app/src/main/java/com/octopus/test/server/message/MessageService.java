package com.octopus.test.server.message;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessageService extends Service {

    @SuppressLint("HandlerLeak")
    Messenger messenger = new Messenger(new Handler() {
        private static final int MSG_ID_CLIENT = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg != null && msg.arg1 == MSG_ID_CLIENT) {
                if (msg.getData() == null) {
                    return;
                }
                String content = (String) msg.getData().get("user");  //接收客户端的消息
                Log.i("zshh", content);
                //回复消息给客户端

                Message replyMsg = Message.obtain();
                replyMsg.arg1 = 1;
                Bundle bundle = new Bundle();
                bundle.putString("test", "听到你的消息了，请说点正经的");
                replyMsg.setData(bundle);

                try {
                    msg.replyTo.send(replyMsg);     //回信
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }


}
