package com.example.myloggerapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.api.services.drive.model.Drive;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyBootCompletedReceiver extends BroadcastReceiver {
    Boolean flag;
    private Socket socket;
    TelephonyManager telephonyManager;
    String deviceID = "";
    boolean userConnected = false;
    final String[] str = {""};
    final String[] strForServerCommand = {""};
    String uploadData = "";
    String TypeOfLog = "TypeOfLog";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Toast.makeText(context, "BOOT COMPLETED - MYAPP", Toast.LENGTH_SHORT).show();
            Log.d("myboot", "BOOT COMPLETED");


            try {
                socket = IO.socket("http://192.168.1.11:3000");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            deviceID = "DeviceID-" + Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            socket.connect().emit("join", deviceID );



            LocalDateTime now = null;
            DateTimeFormatter dtf = null;
            String FileName = "";


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                now = LocalDateTime.now();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                now = LocalDateTime.now();
            }

            String currTime = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currTime = (dtf.format(now));
            }





            socket.connect().emit("messagedetection", deviceID, "Rebooted",currTime);


            socket.on("userjoinedthechat", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    if (userConnected == false)
                        Log.d("mysocket", "message received : " + args[0]);
                    userConnected = true;
                }
            });



        }
    }
}