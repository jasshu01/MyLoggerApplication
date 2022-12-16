package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KernelLogs extends AppCompatActivity {

    TextView tv;
    Button start;
    Boolean flag;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kernel_logs);

        tv = findViewById(R.id.textview_logs);
        start = findViewById(R.id.button_start);
        flag = false;



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!flag) {
                    flag = true;
                    start.setText("Stop");
                    tv.setText("capturing ");

                    Thread myThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            capture();
                        }
                    });
                    myThread.start();

                } else {
                    flag = false;
                    start.setText("Start");

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
            process = Runtime.getRuntime().exec("logcat kernel");
//            process = Runtime.getRuntime().exec("logcat system -f adb logcat -b system -f /storage/emulated/0/Downloads/myFile.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        adb shell pm grant com.example.myloggerapplication android.permission.READ_LOGS

        String str = "";
        String line = "";

        int i = 0;
        while (true && flag) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Log.d("mylogs", " " + i++);

//            try {
//                sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            String finalLine = line;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(finalLine + "\n\n" + tv.getText());
                }
            });


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            process.destroyForcibly();
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