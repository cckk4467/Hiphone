package com.example.hellophone;

import android.content.Intent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.hellophone.utility.ApplicationInfo;
import com.example.hellophone.utility.ConnectThread;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button connect;
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect = findViewById(R.id.connect);
        progressBar = findViewById(R.id.connecting);
        editText = findViewById(R.id.address);
        textView = findViewById(R.id.text);
        progressBar.setVisibility(View.INVISIBLE);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "正在连接！", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                textView.setText("Connecting...");
                progressBar.setVisibility(View.VISIBLE);
                connect.setEnabled(false);
                new ConnectThread((ApplicationInfo) MainActivity.this.getApplication(),editText.getText().toString(),handler).start();
            }
        });
    }
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 23333333) {
                textView.setText("Succeeded!");
                Intent intent = new Intent(MainActivity.this,RunningActivity.class);
                startActivity(intent);
            }else{
                textView.setText("Failed to connect");
            }
            connect.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };
}