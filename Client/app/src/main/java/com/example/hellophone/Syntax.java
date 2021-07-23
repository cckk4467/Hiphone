package com.example.hellophone;

import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.example.hellophone.function.PhoneCall;
import com.example.hellophone.utility.ApplicationInfo;

import java.io.IOException;

import static java.lang.Integer.min;

public class Syntax {
    public static boolean Rule(RunningActivity runningActivity, String s){
        s=s.trim();
        String[] str = s.split(" ");
        if(str[0].equals("call")){
            if(str.length!=2){
                SendErrorCode(runningActivity, (byte) 1);
                return true;
            }
            PhoneCall phoneCall=new PhoneCall(runningActivity,str[1]);
            SendErrorCode(runningActivity, (byte) phoneCall.getPermission());
        }else if(str[0].equals("say")){
            String m="";
            for(int i=1;i<str.length;i++)m+=str[i]+" ";
            Toast.makeText(runningActivity.getApplicationContext(),m,Toast.LENGTH_LONG).show();
        }else if(str[0].equals("poo")){
            byte[] bytes = new byte[4];
            bytes[0]=(int)'p';
            bytes[1]=(int)'e';
            bytes[2]=(int)'e';
            Send(runningActivity,bytes);
            return false;
        }else {
            SendErrorCode(runningActivity, (byte) 2);
            return true;
        }
        SendErrorCode(runningActivity, (byte) 0);
        return true;
    }
    private static void Send(RunningActivity runningActivity, byte[] type){
        ApplicationInfo app;
        app=(ApplicationInfo) runningActivity.getApplication();
        new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run(){
                byte[] bytes = new byte[32];
                for(int i=0;i<min(type.length,32);i++)bytes[i]=type[i];
                try {
                    app.dos.write(bytes);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private static void SendErrorCode(RunningActivity runningActivity, byte type){
        byte[] bytes=new byte[2];
        bytes[0]=3;
        bytes[1]=type;
        Send(runningActivity,bytes);
    }
}
