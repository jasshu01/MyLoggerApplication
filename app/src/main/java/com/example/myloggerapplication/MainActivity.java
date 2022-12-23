package com.example.myloggerapplication;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

    TextView RadioLogs, ADBLogs, KernelLogs, bugReports;
    static File ApplicationFolder, RadioLogsFolder, ADBLogsFolder, KernelLogsFolder,BugReportsFolder;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioLogs = findViewById(R.id.radioLogs);
        ADBLogs = findViewById(R.id.adbLogs);
        KernelLogs = findViewById(R.id.kernelLogs);
        bugReports = findViewById(R.id.bugReports);


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
            // do something here
            startActivity(new Intent(MainActivity.this, ViewAllLogs.class));
        }
        return super.onOptionsItemSelected(item);
    }


}