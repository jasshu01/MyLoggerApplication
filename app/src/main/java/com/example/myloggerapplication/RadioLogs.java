package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class RadioLogs extends AppCompatActivity {

    TextView tv;
    Button start;
    Boolean flag;
    //    Handler mHandler = new Handler(Looper.getMainLooper());
    Handler mHandler = new Handler();
    private MyLogsModel myLogsModel;

    final String[] str = {""};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_logs);

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

        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(folder, "RadioLOGS.txt");


        if (file.exists()) {
            file.delete();
        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str[0] = "";
                if (!flag) {
                    flag = true;
                    start.setText("Stop");
                    tv.setText("capturing ");

//                    AsyncTask.execute((new Runnable() {
                    MyrunOnUiThread((new Runnable() {
                        @Override
                        public void run() {
                            Thread myThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    capture();
                                }
                            });
                            myThread.start();
                        }
                    }));


                } else {

                    Toast.makeText(RadioLogs.this, "Stopping", Toast.LENGTH_SHORT).show();
                    str[0] = "";
                    Thread.interrupted();
                    flag = false;
                    start.setText("Start");
                    writeTextData(file, (String) tv.getText());
                    tv.setText("File Saved @ " + file.getAbsolutePath());
                }


            }
        });


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
            process = Runtime.getRuntime().exec("logcat radio");
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

//            Log.d("mylogs", "current thread " + Thread.currentThread().getName());


//            Log.d("mylogs", " " + i++);

//            try {
//                sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            str[0] = line + "\n\n" + str[0];


//
            String finalLine = line;
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


    public final void MyrunOnUiThread(Runnable action) {
        mHandler.post(action);
    }


}