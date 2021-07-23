package com.example.hellophone;

import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hellophone.utility.ApplicationInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Integer.min;

public class RunningActivity extends AppCompatActivity {
    List<String> logText = new LinkedList();
    ApplicationInfo app;
    TextView log;
    Button disconnet,send;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connected_view);
        app=(ApplicationInfo) this.getApplication();
        log = findViewById(R.id.logs);
        disconnet = findViewById(R.id.disconnect);
        send = findViewById(R.id.send);
        editText = findViewById(R.id.order);
        disconnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disconnect();
                Intent intent = new Intent(RunningActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText.getText().toString();
                byte[] b= new byte[0];
                try {
                    b = s.getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[b.length+2];
                bytes[0] = 1;
                bytes[0] = 1;
                for(int i=bytes.length-1;i>=2;i--)bytes[i]=b[i-2];

                ApplicationInfo app;
                app=(ApplicationInfo) getApplication();
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            app.dos.write(bytes);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        new Thread(){
            @Override
            public void run(){
                try {
                    while(true){
                        byte[] b = new byte[32];
                        int res = app.dis.read(b);
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putByteArray("data",b);
                        message.setData(bundle);
                        message.what=res;
                        mHandler.sendMessage(message);
                        if(res==-1)break;
                        Thread.sleep(5);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                Disconnect();
            }
        }.start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Disconnect();
    }
    void Disconnect(){
        new Thread(){
            @Override
            public void run(){
                byte[] b = new byte[32];
                b[0]=88;
                try {
                    app.dos.write(b);
                    app.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what!=-1){
                Bundle data = msg.getData();
                byte[] bytes=data.getByteArray("data");

                try {
                    String txt=new String(bytes, "GBK");
                    if(!Syntax.Rule(RunningActivity.this,txt)) return;
                    logText.add(txt);;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(logText.size()>10)logText.remove(0);
                String display="";
                for(String c:logText){
                    display+=c+"\n";
                }

                log.setText(display);
            }else{
                Toast.makeText(getApplicationContext(),"QAQ连接中断~",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RunningActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };
}
