//package com.example.myloggerapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProvider;
//
//import android.annotation.SuppressLint;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Message;
//import android.text.format.Time;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
////import com.github.nkzawa.emitter.Emitter;
////import com.github.nkzawa.socketio.client.IO;
////import com.github.nkzawa.socketio.client.Socket;
////import com.github.nkzawa.socketio.client.IO;
////import com.github.nkzawa.socketio.client.Socket;
////import com.here.oksse.OkSse;
////import com.here.oksse.ServerSentEvent;
////import com.squareup.okhttp.MediaType;
////import com.squareup.okhttp.OkHttpClient;
////
////import com.squareup.okhttp.RequestBody;
////import com.squareup.okhttp.Response;
//
//import com.github.nkzawa.engineio.client.transports.WebSocket;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
////import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//
////import com.github.nkzawa.emitter.Emitter;
////import com.github.nkzawa.socketio.client.IO;
////import com.github.nkzawa.socketio.client.Socket;
//
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import io.socket.emitter.Emitter;
////import io.socket.client.Socket;
//
//
//public class KernelLogs extends AppCompatActivity {
//    private Socket socket;
//    //    private SocketIO socketIO;
////    {
////        try {
//////            socket = IO.socket("http://192.168.1.11:3000");
////            socket = new Socket("192.168.1.11", 3000);
////        } catch (UnknownHostException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//    boolean userConnected = false;
//
//    private PrintWriter output;
//    TextView tv;
//    Button start;
//    Boolean flag;
//    Boolean alreadyCapturing_OnServerCommand;
//    private MyLogsModel myLogsModel;
//    boolean mainActivityisOpen = false;
//    final String[] str = {""};
//    final String[] strForServerCommand = {""};
//    String uploadData = "";
//
//    boolean activityIsOpen = false;
//
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        new PerformTaskOnServerCommand().execute();
//        setContentView(R.layout.activity_kernel_logs);
//        str[0] = "";
//
//        tv = findViewById(R.id.textview_logs);
//        start = findViewById(R.id.button_start);
//        flag = false;
//        alreadyCapturing_OnServerCommand = false;
//        myLogsModel = new ViewModelProvider(this).get(MyLogsModel.class);
//
//        myLogsModel.myLogs.observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                tv.setText(s);
//            }
//        });
//
//        start.setText("Capture Kernel Logs");
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                capture_0();
//
//            }
//        });
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
////                    socket = new Socket("192.168.1.11", 3000);
//                    socket = IO.socket("http://192.168.1.11:3000");
//                    socket.connect().emit("join", "Jasshu");
//
//
//                    socket.on("userjoinedthechat", new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//                            if (userConnected == false) {
//                                Log.d("socket", "message received : " + args[0]);
//                                userConnected = true;
//
//                            }
//
//                        }
//                    });
//                    socket.on("start_Logging", new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//                            Log.d("socket", "message received : " + args[0]);
//
//                            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//                            scheduledExecutorService.schedule(new Runnable() {
//                                @Override
//                                public void run() {
////                                    if (!alreadyCapturing_OnServerCommand)
//                                    {
//                                        strForServerCommand[0] = "";
//                                        alreadyCapturing_OnServerCommand = true;
//                                        captureOnServerCommand();
//                                    }
//
//                                }
//                            }, 1, TimeUnit.MILLISECONDS);
//
//                        }
//
//
//                    });
//                    socket.on("stop_Logging", new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//                            Log.d("socket", "message received : " + args[0]);
////                            if(alreadyCapturing_OnServerCommand)
//                            {
//                                try {
//                                    stopOnServerCommand();
//                                    alreadyCapturing_OnServerCommand = false;
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                        }
//                    });
//
//
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//
//
//    }
//
//
//    void capture_0() {
//        str[0] = "";
//        if (!flag) {
//            flag = true;
//            start.setText("Stop Capturing");
////                    tv.setText("capturing ");
//
//            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//            scheduledExecutorService.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    capture();
//                }
//            }, 1, TimeUnit.MILLISECONDS);
//
//        } else {
//            try {
//                stop();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//
//    void capture() {
//
//
//        try {
//            Runtime.getRuntime().exec("logcat -c");
//            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Process process = null;
//        try {
//            process = Runtime.getRuntime().exec("logcat kernel");
////            process = Runtime.getRuntime().exec("logcat system -f adb logcat -b system -f /storage/emulated/0/Downloads/myFile.txt");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
////        adb shell pm grant com.example.myloggerapplication android.permission.READ_LOGS
//
//        String line = "";
//
//        int i = 0;
//        while (true && flag) {
//
//            try {
//                if (!((line = br.readLine()) != null)) break;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            str[0] = line + "\n\n" + str[0];
//
//
////
//            String finalLine = line;
//
//
//            if (activityIsOpen)
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (flag) {
////                        tv.setText(finalLine + "\n\n" + tv.getText());
////                        tv.setText(str[0]);
//                            myLogsModel.myLogs.setValue(str[0]);
//
//                        }
//                    }
//                });
//
//
//        }
//
//
//    }
//
//
//    void stop() throws IOException {
//        uploadData = tv.getText().toString();
//
//        start.setText("Capture Kernel Logs");
//        Toast.makeText(KernelLogs.this, "Stopping", Toast.LENGTH_SHORT).show();
//        str[0] = "";
//        Thread.interrupted();
//        flag = false;
//
//
//        LocalDateTime now = null;
//        DateTimeFormatter dtf = null;
//        String FileName = "";
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
//            now = LocalDateTime.now();
//        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            FileName = "KernelLogs_" + (dtf.format(now) + ".txt");
//        }
//
//
//        File file = new File(MainActivity.KernelLogsFolder, FileName);
//        writeTextData(file, (String) tv.getText());
//        tv.setText("File Saved @ " + file.getAbsolutePath());
//
//
////        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
////        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
////        -H "Content-Type: text/plain" `# change to match the file's MIME type` \
////                -d "Example Data"             `# to upload a file: --data-binary @file.jpg`
//
////        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
////        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
////        -H "Content-Type: text/plain" \
////                -data-binary @output.txt
////
////
//        new PerformTask().execute();
//
//    }
//
//
//    class PerformTask extends AsyncTask<Void, Void, Void> {
//
//        private Exception exception;
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            String data = tv.getText().toString();
//            String uploadcommand = "curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary -H " + "\"Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9\" -H \"Content-Type: text/plain\" -d \"" + data + "\"";
////
//            Log.d("uploading", uploadcommand);
////        Runtime.getRuntime().exec(uploadcommand);
//
//
//            // avoid creating several instances, should be singleon
////            OkHttpClient client = new OkHttpClient();
////            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), uploadData);
////            Request request = new Request.Builder()
////                    .header("Authorization ", "Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9")
////                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
////                    .post(body)
////                    .build();
////
////            Response response = null;
////            try {
////                response = client.newCall(request).execute();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            try {
////                Log.d("uploading", response.body().string());
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//            return null;
//        }
//    }
//
//
//    private void writeTextData(File file, String data) {
//        FileOutputStream fileOutputStream = null;
//        try {
//            fileOutputStream = new FileOutputStream(file);
//            fileOutputStream.write(data.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (fileOutputStream != null) {
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        activityIsOpen = true;
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        activityIsOpen = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        activityIsOpen = false;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (userConnected)
//            socket.close();
//        userConnected = false;
////        Log.d("socket", "closed , connected " + socket.connected());
//    }
//
//
//    void captureOnServerCommand() {
//
//        strForServerCommand[0] = "";
//
//        try {
//            Runtime.getRuntime().exec("logcat -c");
////            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Process process = null;
//        try {
//            process = Runtime.getRuntime().exec("logcat kernel");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
////        adb shell pm grant com.example.myloggerapplication android.permission.READ_LOGS
//
//        String line = "";
//
//        Log.d("socket", "Capturing started");
//        int i = 0;
//        while (true && flag) {
//
//            try {
//                if (!((line = br.readLine()) != null)) break;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            strForServerCommand[0] = line + "\n\n" + strForServerCommand[0];
//
//
//        }
//
//
//    }
//
//
//    void stopOnServerCommand() throws IOException {
//
//
//        Thread.interrupted();
//        flag = false;
//
//
//        LocalDateTime now = null;
//        DateTimeFormatter dtf = null;
//        String FileName = "";
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
//            now = LocalDateTime.now();
//        }
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            FileName = "KernelLogs_" + (dtf.format(now) + ".txt");
//        }
//
//        String data = strForServerCommand[0];
//        Log.d("captureddata", data);
//
//
//        File file = new File(MainActivity.KernelLogsFolder, FileName);
//        writeTextData(file, data);
//
////        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
////        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
////        -H "Content-Type: text/plain" `# change to match the file's MIME type` \
////                -d "Example Data"             `# to upload a file: --data-binary @file.jpg`
//
////        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
////        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
////        -H "Content-Type: text/plain" \
////                -data-binary @output.txt
////
////
//        new PerformTaskOnServerCommand().execute();
//
//    }
//
//    class PerformTaskOnServerCommand extends AsyncTask<Void, Void, Void> {
//
//        private Exception exception;
//
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            uploadData = strForServerCommand[0];
////            String uploadcommand = "curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary -H " + "\"Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9\" -H \"Content-Type: text/plain\" -d \"" + uploadData + "\"";
////
////            Log.d("uploading", uploadcommand);
////            try {
////                Runtime.getRuntime().exec(uploadcommand);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//
//
//            Log.d("socket", " about to upload" + strForServerCommand[0]);
////             avoid creating several instances, should be singleon
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), strForServerCommand[0]);
//            Request request = new Request.Builder()
//                    .header("Authorization ", "Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9")
//                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
//                    .post(body)
//                    .build();
//
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                Log.d("uploading", response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void feed) {
//            // TODO: check this.exception
//            // TODO: do something with the feed
//        }
//    }
//
//}


package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.Settings;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;
//import com.here.oksse.OkSse;
//import com.here.oksse.ServerSentEvent;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
//import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyRep;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


//import com.github.nkzawa.emitter.Emitter;
//import com.github.nkzawa.socketio.client.IO;
//import com.github.nkzawa.socketio.client.Socket;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
//import io.socket.client.Socket;


public class KernelLogs extends AppCompatActivity {
    private Socket socket;
    //    private SocketIO socketIO;
//    {
//        try {
////            socket = IO.socket("http://192.168.1.11:3000");
//            socket = new Socket("192.168.1.11", 3000);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    boolean userConnected = false;
    TelephonyManager telephonyManager;
    private PrintWriter output;
    TextView tv;
    Button start;
    Boolean flag;
    private MyLogsModel myLogsModel;
    boolean mainActivityisOpen = false;
    final String[] str = {""};
    final String[] strForServerCommand = {""};
    String uploadData = "";

    boolean activityIsOpen = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new PerformTaskOnServerCommand().execute();
        setContentView(R.layout.activity_kernel_logs);
        str[0] = "";

        tv = findViewById(R.id.textview_logs);
        start = findViewById(R.id.button_start);
        flag = false;
        myLogsModel = new ViewModelProvider(this).get(MyLogsModel.class);

        myLogsModel.myLogs.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tv.setText(s);
            }
        });

        start.setText("Capture Kernel Logs");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                capture_0();

            }
        });
        telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(KernelLogs.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }


//        String imeiNumber = telephonyManager.getDeviceId();
        String deviceID = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
//        telephonyManager.getDeviceSoftwareVersion();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
//                    socket = new Socket("192.168.1.11", 3000);
                    socket = IO.socket("http://192.168.1.11:3000");
                    socket.connect().emit("join", deviceID);


                    socket.on("userjoinedthechat", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            if (userConnected == false)
                                Log.d("socket", "message received : " + args[0]);
                            userConnected = true;
                        }
                    });
                    socket.on("start_Logging", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("socket", "message received : " + args[0]);
                            flag = false;

                            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                            scheduledExecutorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    if (!flag) {
                                        flag = true;
                                        captureOnServerCommand();
                                    }

                                }
                            }, 1, TimeUnit.MILLISECONDS);

                        }


                    });
                    socket.on("stop_Logging", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("socket", "message received : " + args[0]);
                            try {
                                stopOnServerCommand();
                                flag = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    void capture_0() {
        str[0] = "";
        if (!flag) {
            flag = true;
            start.setText("Stop Capturing");
//                    tv.setText("capturing ");

            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    capture();
                }
            }, 1, TimeUnit.MILLISECONDS);

        } else {
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void captureOnServerCommand() {

        strForServerCommand[0] = "";

        try {
            Runtime.getRuntime().exec("logcat -c");
//            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec("logcat kernel");
//            process = Runtime.getRuntime().exec("logcat system -f adb logcat -b system -f /storage/emulated/0/Downloads/myFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        adb shell pm grant com.example.myloggerapplication android.permission.READ_LOGS

        String line = "";

        int i = 0;
        while (true && flag) {

            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }


            strForServerCommand[0] = line + "\n\n" + strForServerCommand[0];


//
//            String finalLine = line;
//
//
//            if (activityIsOpen)
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (flag) {
////                        tv.setText(finalLine + "\n\n" + tv.getText());
////                        tv.setText(str[0]);
//                            myLogsModel.myLogs.setValue(str[0]);
//
//                        }
//                    }
//                });
//

        }


    }

    void capture() {


        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec("logcat kernel");
//            process = Runtime.getRuntime().exec("logcat system -f adb logcat -b system -f /storage/emulated/0/Downloads/myFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        adb shell pm grant com.example.myloggerapplication android.permission.READ_LOGS

        String line = "";

        int i = 0;
        while (true && flag) {

            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }


            str[0] = line + "\n\n" + str[0];


//
            String finalLine = line;


            if (activityIsOpen)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (flag) {
//                        tv.setText(finalLine + "\n\n" + tv.getText());
//                        tv.setText(str[0]);
                            myLogsModel.myLogs.setValue(str[0]);

                        }
                    }
                });


        }


    }


    void stopOnServerCommand() throws IOException {


        Thread.interrupted();
        flag = false;


        LocalDateTime now = null;
        DateTimeFormatter dtf = null;
        String FileName = "";


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            now = LocalDateTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FileName = "KernelLogs_" + (dtf.format(now) + ".txt");
        }

        String data = strForServerCommand[0];
        Log.d("captureddata", data);


        File file = new File(MainActivity.KernelLogsFolder, FileName);
        writeTextData(file, data);

//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" `# change to match the file's MIME type` \
//                -d "Example Data"             `# to upload a file: --data-binary @file.jpg`

//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" \
//                -data-binary @output.txt
//
//
        new PerformTaskOnServerCommand().execute();

    }

    void stop() throws IOException {
        uploadData = tv.getText().toString();

        start.setText("Capture Kernel Logs");
        Toast.makeText(KernelLogs.this, "Stopping", Toast.LENGTH_SHORT).show();
        str[0] = "";
        Thread.interrupted();
        flag = false;


        LocalDateTime now = null;
        DateTimeFormatter dtf = null;
        String FileName = "";


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            now = LocalDateTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FileName = "KernelLogs_" + (dtf.format(now) + ".txt");
        }


        File file = new File(MainActivity.KernelLogsFolder, FileName);
        writeTextData(file, (String) tv.getText());
        tv.setText("File Saved @ " + file.getAbsolutePath());


//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" `# change to match the file's MIME type` \
//                -d "Example Data"             `# to upload a file: --data-binary @file.jpg`

//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" \
//                -data-binary @output.txt
//
//
        new PerformTask().execute();

    }

    private void writeTextData(File file, String data) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        activityIsOpen = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityIsOpen = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityIsOpen = false;
    }


    class PerformTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... voids) {
            String data = tv.getText().toString();
            String uploadcommand = "curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary -H " + "\"Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9\" -H \"Content-Type: text/plain\" -d \"" + data + "\"";
//
            Log.d("uploading", uploadcommand);
//        Runtime.getRuntime().exec(uploadcommand);


            // avoid creating several instances, should be singleon
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), uploadData);
//            Request request = new Request.Builder()
//                    .header("Authorization ", "Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9")
//                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
//                    .post(body)
//                    .build();
//
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                Log.d("uploading", response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }

    class PerformTaskOnServerCommand extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... voids) {
            uploadData = strForServerCommand[0];
//            String uploadcommand = "curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary -H " + "\"Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9\" -H \"Content-Type: text/plain\" -d \"" + uploadData + "\"";
//
//            Log.d("uploading", uploadcommand);
//            try {
//                Runtime.getRuntime().exec(uploadcommand);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//             avoid creating several instances, should be singleon
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), uploadData);
            Request request = new Request.Builder()
                    .header("Authorization ", "Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9")
                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
                    .post(body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Log.d("uploading", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userConnected)
            socket.close();
        userConnected = false;
//        Log.d("socket", "closed , connected " + socket.connected());
    }
}