
package com.octopus.test.net.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServerSocket {

    public static void main(String []args) throws InterruptedException {
        try {
            ServerSocket serverSocket  = new ServerSocket(8080);
            Socket socket = serverSocket.accept();
            new Thread(new SocketHandler(socket)).start();
            Thread.currentThread().join();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class SocketHandler implements Runnable{

        Socket socket;
        SocketHandler(Socket socket){
           this.socket = socket;
        }

        @Override
        public void run() {
            try {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter write  = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())) ;
                String buffer = null;
                while(true){
                    buffer = bufferedReader.readLine();
                    System.out.println(buffer);
                    write.println("time:"+System.currentTimeMillis());
                    write.flush();
                    if(buffer.equals("exit")){
                        System.exit(1);
                        // System.exit(1);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
