package com.example.hellophone.utility;

import android.os.Handler;
import android.os.Message;
import com.example.hellophone.utility.ApplicationInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectThread extends Thread{
    ApplicationInfo app;
    String host;
    Handler handler;
    public ConnectThread(ApplicationInfo _app, String _host,Handler _handler){
        app=_app;
        host=_host;
        handler=_handler;
    }
    @Override
    public void run(){
        if(app==null)return;
        int post=25565;

        Thread connecting=new Thread(){
            @Override
            public void run(){
                try {
                    app.socket = new Socket();
                    app.socket.connect(new InetSocketAddress(host, post), 6000);
                    app.dis = new DataInputStream(app.socket.getInputStream());
                    app.dos = new DataOutputStream(app.socket.getOutputStream());
                    byte[] b=new byte[2];
                    b[0]=(byte) 104;
                    b[1]=(byte) 105;
                    app.dos.write(b);
                    b=new byte[32];
                    app.dis.read(b);
                    if(b[0]!=104 || b[1]!=105) {
                        app.socket.close();
                    }
                } catch (IOException e) {
                    app.socket=null;
                }
                return;
            }
        };
        connecting.start();

        try {
            connecting.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (app.socket!=null){
            Message msg = new Message();
            msg.what = 23333333;
            handler.sendMessage(msg);
        }else{
            Message msg = new Message();
            msg.what = -23333333;
            handler.sendMessage(msg);
        }
    }
}
