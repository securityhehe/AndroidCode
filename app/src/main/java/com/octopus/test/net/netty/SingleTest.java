package com.octopus.test.net.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.rc.im.client.netty.IMMessage;
import com.rc.im.client.netty.client.ClientUser;
import com.rc.im.client.netty.client.callback.EventCallBack;
import com.rc.im.client.netty.client.callback.StateCallBack;
import com.rc.im.protobuf.CommonProto;
import com.rc.im.protobuf.EventProto;
import com.rc.im.protobuf.IMReqProto;
import com.rc.im.protobuf.IMRespProto;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SingleTest {

    private ClientUser clientUser;
    private CountDownLatch connectedSignal;
    private long mMsgId =0;

    public static void main(String[] args) {
        try {
            SingleTest singleTest = new SingleTest();
            singleTest.test();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void test(){
        Scanner sc = new Scanner(System.in);
        ClientUser clientUser = null;
        connectedSignal = new CountDownLatch(1);
        System.out.println("输入指令 ");
        System.out.println("1:开始连接 2:发送私聊信息 3:发送群聊信息 4:绑定群 5:退群");
        while (sc.hasNext()) {

            int type = sc.nextInt();
            switch(type){
                case 1:{
                    connect(sc);
                    break;
                }case 2:{
                    alonePrivateMessage(sc);
                    break;
                }case 3:{
                    groupMessage(sc);
                    break;
                }case 4:{
                    bindGroup(sc);
                    break;
                }case 5:{
                    removeGroup(sc);
                    break;
                }
            }
            System.out.println("输入指令 ");
            System.out.println("1:开始连接 2:发送私聊信息 3:发送群聊信息 4:绑定群 5:退群");
        }
    }

    private void connect(Scanner sc){
        System.out.println("输入uid: ");
        int uid = sc.nextInt();
        clientUser = new ClientUser(uid,"http://192.168.3.188:8999/im/router;http://192.168.3.188:9000/im/router;http://192.168.3.188:9001/im/router");
        clientUser.setHeartBeatThread(5000,3);
        clientUser.connect(new StateCallBack() {
            public void callBack(Object obj) {
                System.out.println("连接上了");
                verifyReq();
                connectedSignal.countDown();
                stateCallBack(obj);
            }
        }, new EventCallBack() {
            public void callBack(Object obj) {
//                System.out.println("收到消息");
                eventCallBack(obj);
            }
        });
        try {
            connectedSignal.await(2000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void verifyReq(){
        IMReqProto.VerifyReq.Builder verifyReq = IMReqProto.VerifyReq.newBuilder();
        verifyReq.setUserId(clientUser.getUid());
        clientUser.sendMsg(EventProto.EventType.VERIFY_REQ_VALUE,verifyReq.build().toByteArray());
    }

    private void alonePrivateMessage(Scanner sc){
        System.out.println("输入:  目标id,msg");
        String str = sc.next();
        String[] strs = str.split(",");
        int targetUid = Integer.parseInt(strs[0]);
        String msg = strs[1];
        CommonProto.MessageBasePB.Builder messageBasePB = CommonProto.MessageBasePB.newBuilder();
        messageBasePB.setMessageState(EventProto.MessageStateType.SEND_VALUE);
        CommonProto.AlonePrivateMessagePB.Builder alonePrivateMessagePB = CommonProto.AlonePrivateMessagePB.newBuilder();
        alonePrivateMessagePB.setContent(msg);
        alonePrivateMessagePB.setMessageBase(messageBasePB);
        alonePrivateMessagePB.setSendUserId(clientUser.getUid());
        alonePrivateMessagePB.setReceiveUserId(targetUid);
        mMsgId++;
        IMReqProto.SendAlonePrivateMessageReq.Builder sendAlonePrivateMessageReq = IMReqProto.SendAlonePrivateMessageReq.newBuilder();
        sendAlonePrivateMessageReq.setAlonePrivateMessage(alonePrivateMessagePB);
        sendAlonePrivateMessageReq.setMsgId(mMsgId);
        clientUser.sendMsg(EventProto.EventType.SEND_ALONE_PRIVATE_MESSAGE_REQ_VALUE,sendAlonePrivateMessageReq.build().toByteArray());
    }

    private void groupMessage(Scanner sc){
        System.out.println("输入:  groupId,msg");
        String str = sc.next();
        String[] strs = str.split(",");
        int groupId = Integer.parseInt(strs[0]);
        String msg = strs[1];

        CommonProto.MessageBasePB.Builder messageBasePB = CommonProto.MessageBasePB.newBuilder();
        messageBasePB.setMessageState(EventProto.MessageStateType.SEND_VALUE);
        CommonProto.GroupMessagePB.Builder groupMessagePB = CommonProto.GroupMessagePB.newBuilder();
        groupMessagePB.setContent(msg);
        groupMessagePB.setMessageBase(messageBasePB);
        groupMessagePB.setSendUserId(clientUser.getUid());
        groupMessagePB.setGroupId(groupId);
        mMsgId++;
        IMReqProto.SendGroupMessageReq.Builder sendGroupMessageReq = IMReqProto.SendGroupMessageReq.newBuilder();
        sendGroupMessageReq.setGroupMessagePB(groupMessagePB);
        sendGroupMessageReq.setMsgId(mMsgId);
        clientUser.sendMsg(EventProto.EventType.SEND_GROUP_MESSAGE_REQ_VALUE,sendGroupMessageReq.build().toByteArray());
    }

    private void bindGroup(Scanner sc){
        System.out.println("输入:  groupId");
        int groupId = sc.nextInt();
        IMReqProto.BindGroupReq.Builder bindGroupReq = IMReqProto.BindGroupReq.newBuilder();
        bindGroupReq.setGroupId(groupId);
        clientUser.sendMsg(EventProto.EventType.BIND_GROUP_REQ_VALUE,bindGroupReq.build().toByteArray());
    }

    private void removeGroup(Scanner sc){
        System.out.println("输入:  groupId");
        int groupId = sc.nextInt();
        IMReqProto.RemoveGroupReq.Builder removeGroupReq = IMReqProto.RemoveGroupReq.newBuilder();
        removeGroupReq.setGroupId(groupId);
        clientUser.sendMsg(EventProto.EventType.REMOVE_GROUP_REQ_VALUE,removeGroupReq.build().toByteArray());
    }

    private void stateCallBack(Object obj){

    }

    private void eventCallBack(Object obj){
        if(obj instanceof IMMessage){
            IMMessage msg = (IMMessage)obj;
            switch (msg.getImHeader().getEventId()){
                case EventProto.EventType.PUSH_ALONE_PRIVATE_MESSAGE_RESP_VALUE:{
                    try {
                        IMRespProto.PushAlonePrivateMessageResp builder = IMRespProto.PushAlonePrivateMessageResp.parseFrom(msg.getContent());
                        System.out.println("UID:"+builder.getAlonePrivateMessage().getSendUserId()+" 发来ID:"+builder.getAlonePrivateMessage().getMessageBase().getMessageId()+" 私信："+builder.getAlonePrivateMessage().getContent());

                        IMReqProto.UpdateMessageReq.Builder send = IMReqProto.UpdateMessageReq.newBuilder();
                        send.setInformUserId(builder.getAlonePrivateMessage().getSendUserId());
                        send.setMessageType(EventProto.MessageStateType.RECEIVE_VALUE);
                        send.setMsgId(builder.getAlonePrivateMessage().getMessageBase().getMessageId());
                        clientUser.sendMsg(EventProto.EventType.UPDATE_MESSGAE_REQ_VALUE,send.build().toByteArray());

                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    break;
                }case EventProto.EventType.PUSH_GROUP_MESSAGE_RESP_VALUE:{
                    try {
                        IMRespProto.PushGroupMessageResp builder = IMRespProto.PushGroupMessageResp.parseFrom(msg.getContent());
                        System.out.println("UID:"+builder.getGroupMessagePB().getSendUserId()+" 发来ID:"+builder.getGroupMessagePB().getMessageBase().getMessageId()+" 群聊消息："+builder.getGroupMessagePB().getContent());
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
