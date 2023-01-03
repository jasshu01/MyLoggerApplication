package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenertateBugReports extends AppCompatActivity {

    Button generateBugReportStart;
    ProgressBar progressBar;
    TextView bugReportLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genertate_bug_reports);

        generateBugReportStart = findViewById(R.id.generateBugReports);
        progressBar = findViewById(R.id.progressBar);
        bugReportLocation = findViewById(R.id.bugReportLocation);

        generateBugReportStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GenertateBugReports.this, "Generating bug report", Toast.LENGTH_SHORT).show();


                try {
//                    Process process1 = Runtime.getRuntime().exec("root ");
//                    Process process = Runtime.getRuntime().exec("shell su");
                    Process process = Runtime.getRuntime().exec("bugreportz -p");
                    DataOutputStream os = new DataOutputStream(process.getOutputStream());
//                    os.writeBytes("bugreportz");
//                    Process process = Runtime.getRuntime().exec("bugreportz");
//                    Process process = Runtime.getRuntime().exec("dumpstate -o bugreportz ");
//                    Process process = Runtime.getRuntime().exec("ls /bugreports/ ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    String line="";
                    while((line=br.readLine())!=null)
                    {
                        Log.d("mybugreport",line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}