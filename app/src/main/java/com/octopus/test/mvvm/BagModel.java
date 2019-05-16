package com.octopus.test.mvvm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BagModel{
    OkHttpClient okHttpClient ;
    public  BagModel(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.build();
    }

    //异步请求。
    public List<BagViewModel.Bag> getBag() {
        final Request.Builder request = new Request.Builder();
        request.url("http://www.baidu.com");
        request.get();
        Request build = request.build();
        Call call = okHttpClient.newCall(build);
        //调用okHttp进行网络请求。
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String string = response.body().string();
                    System.out.println(string);

                }
            }
        });
        return new ArrayList<BagViewModel.Bag>();
    }

    //okHttp 同步请求。
    public void syncRequest(){
        Request.Builder builder = new Request.Builder();
        Request build = builder.url("http://www.xiu8.com")
                .get()
                .build();
        Call call = okHttpClient.newCall(build);
        try {
            Response execute = call.execute();
            String string = execute.body().string();
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        //new BagModel().syncRequest();
        //new BagModel().getBag();
    }

    interface  AA{
        void getBody(String body);
    }
    AA a;

    public void setCallback(AA aa){
        this.a = aa;
    }

    public void queryBaidu() {
        Request.Builder builder =  new Request.Builder();
        Request request = builder.url("http://www.baidu.com")
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null){
                    if(response.isSuccessful()){
                        String string = response.body().string();
                        a.getBody(string);
                    }
                }

            }
        });

    }
}
