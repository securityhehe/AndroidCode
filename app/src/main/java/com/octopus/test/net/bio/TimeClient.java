package com.octopus.test.net.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TimeClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket();
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1",8080);
        socket.connect(socketAddress);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        printWriter.println("aaaaaaaaaaaaaaa\n");
        printWriter.flush();
        BufferedReader read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(read.readLine());
        new Thread(new WriteWork(printWriter)).start();
    }

    private static class WriteWork implements Runnable {

        PrintWriter writer;
        public WriteWork(PrintWriter printWriter) {
            this.writer = printWriter;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String read = null;
            try {
                while (true) {
                    read  = bufferedReader.readLine();
                    writer.println(read);
                    writer.flush();
                    if(read == "exit"){
                        return;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
