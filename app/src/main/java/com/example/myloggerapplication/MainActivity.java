package com.example.myloggerapplication;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button start;
    Boolean flag;

    Button RadioLogs, ADBLogs, KernelLogs;
    static File RadioLogsFolder, ADBLogsFolder, KernelLogsFolder;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioLogs = findViewById(R.id.radioLogs);
        ADBLogs = findViewById(R.id.adbLogs);
        KernelLogs = findViewById(R.id.kernelLogs);


        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File ApplicationFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication");
        RadioLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/RadioLogs");
        ADBLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/ADBLogs");
        KernelLogsFolder = new File(folder.getAbsolutePath() + "/MyLoggerApplication/KernelLogs");

        ApplicationFolder.mkdir();
        RadioLogsFolder.mkdir();
        ADBLogsFolder.mkdir();
        KernelLogsFolder.mkdir();


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

//        tv = findViewById(R.id.textview_logs);
//        start = findViewById(R.id.button_start);
//        flag = false;
//
//        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//        File file = new File(folder, "RadioLOGS.txt");
//
//
//        if (file.exists()) {
//            file.delete();
//        }
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (!flag) {
//                    flag = true;
//                    start.setText("Stop");
//                    tv.setText("capturing ");
//
//                    Thread myThread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            capture();
//                        }
//                    });
//                    myThread.start();
//
//                } else {
//                    flag = false;
//                    start.setText("Start");
//                    writeTextData(file, (String) tv.getText());
//                    tv.setText("File Saved");
//                }
//
//
//            }
//        });


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