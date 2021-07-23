package com.example.hellophone.utility;

import android.app.Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ApplicationInfo extends Application {
    public String ADDRESS = "";
    public int PORT = 25565;

    public Socket socket;
    public DataOutputStream dos = null;
    public DataInputStream dis = null;
}
