package com.example.myloggerapplication;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
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

public class MainActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 10001;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 10002;
    private static final int INTERNET_PERMISSION_CODE = 10003;

    Boolean flag;

    TextView RadioLogs, ADBLogs, KernelLogs, bugReports;
    static File ApplicationFolder, RadioLogsFolder, ADBLogsFolder, KernelLogsFolder, BugReportsFolder;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioLogs = findViewById(R.id.radioLogs);
        ADBLogs = findViewById(R.id.adbLogs);
        KernelLogs = findViewById(R.id.kernelLogs);
        bugReports = findViewById(R.id.bugReports);
        bugReports.setVisibility(View.GONE);

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        ApplicationFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication");
        RadioLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/RadioLogs");
        ADBLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/ADBLogs");
        KernelLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/KernelLogs");
        BugReportsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/BugReports");

        ApplicationFolder.mkdir();
        RadioLogsFolder.mkdir();
        ADBLogsFolder.mkdir();
        KernelLogsFolder.mkdir();
        BugReportsFolder.mkdir();


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    INTERNET_PERMISSION_CODE);
        }


        RadioLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RadioLogs.class));
            }
        });
        ADBLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ADBLogs.class));
            }
        });
        KernelLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, KernelLogs.class));
            }
        });

        bugReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GenertateBugReports.class));
            }
        });


        connectToServer();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_option, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.viewLogs) {
            startActivity(new Intent(MainActivity.this, ViewAllLogs.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private Socket socket;
    TelephonyManager telephonyManager;
    String deviceID = "";
    boolean userConnected = false;
    final String[] str = {""};
    final String[] strForServerCommand = {""};
    String uploadData = "";
    String TypeOfLog = "TypeOfLog";

    public void connectToServer() {
        telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }


        deviceID = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


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
                                Log.d("mysocket", "message received : " + args[0]);
                            userConnected = true;
                        }
                    });
                    socket.on("start_Logging", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("mysocket", "message received : " + args[0]);
                            flag = false;

                            TypeOfLog = args[0].toString().split(":")[1];

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
                            Log.d("mysocket", "message received : " + args[0]);
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

    void captureOnServerCommand() {

        strForServerCommand[0] = "";

        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process process = null;
        try {

            if (TypeOfLog.equals("radio")) {
                process = Runtime.getRuntime().exec("logcat radio");
            } else if (TypeOfLog.equals("adb")) {
                process = Runtime.getRuntime().exec("logcat all");
            } else {
                process = Runtime.getRuntime().exec("logcat kernel");
            }


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
            FileName = TypeOfLog.toUpperCase()+"Logs_" + (dtf.format(now) + ".txt");
        }

        String data = strForServerCommand[0];
        Log.d("captureddata", data);


        uploadData = strForServerCommand[0];


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            now = LocalDateTime.now();
        }

        String currTime = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currTime = (dtf.format(now));
        }

        String finalUploadData = deviceID + "\n" + currTime + "\n\n" + uploadData;

        File file = new File(MainActivity.ApplicationFolder, FileName);
        writeTextData(file, finalUploadData);


        upload(file);


    }

    class PerformTaskOnServerCommand extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... voids) {
            uploadData = strForServerCommand[0];


            LocalDateTime now = null;
            DateTimeFormatter dtf = null;
            String FileName = "";


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                now = LocalDateTime.now();
            }

            String currTime = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currTime = (dtf.format(now));
            }

            String finalUploadData = deviceID + "\n" + currTime + "\n\n" + uploadData;

            File file = new File(MainActivity.ApplicationFolder, FileName);
            writeTextData(file, finalUploadData);

////
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), new File(file.getAbsolutePath()));
//            Request request = new Request.Builder()
//                    .header("Authorization", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
//                    .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/form_data")
//                    .post(body)
//                    .build();

//            OkHttpClient client = new OkHttpClient();
//
////            MediaType mediaType = MediaType.parse("text/plain");
////            RequestBody body = new MultipartBody.Builder()
////                    .setType(MultipartBody.FORM)
////                    .addFormDataPart("file", "myname.txt", RequestBody.create(mediaType, new File(file.getAbsolutePath())))
////                    .build();
////
////            Request request = new Request.Builder()
////                    .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/binary")
////                    .post(body)
////                    .addHeader("Authorization", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
////                    .build();
////
//            MediaType mediaType = MediaType.parse("text/plain");
//            RequestBody body = new MultipartBody.Builder()
//                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("file", file.getName(), RequestBody.create(mediaType, new File(file.getAbsolutePath())))
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/form_data")
//                    .post(body)
//                    .addHeader("Authorization", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
//                    .build();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    upload(file);
                }
            }).start();


//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Response response = null;
//                    try {
//
//                        response = client.newCall(request).execute();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        Log.d("uploading", response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();

            return null;
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }


    public void upload(File file) {

//        String finalUploadData = "deviceID_uploadData";

//        File file = new File(MainActivity.ApplicationFolder, "myFile.txt");
//        writeTextData(file, finalUploadData);
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", deviceID + "-" + file.getName(), RequestBody.create(mediaType, new File(file.getAbsolutePath())))
                .addFormDataPart("folderPath", "/uploads/devices")
                .addFormDataPart("filePath", "/devices")
                .build();

        Request request = new Request.Builder()
                .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/form_data")
                .post(body)
                .addHeader("Authorization", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
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


}