package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class ViewAllLogs extends AppCompatActivity {
    ArrayList<File> mylogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_logs);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button deleteSelectedLogs = findViewById(R.id.deleteSelectedLogs);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button shareSelectedLogs = findViewById(R.id.shareSelectedLogs);

        TextView viewAllLogs = findViewById(R.id.viewAllLogs);
        TextView viewRadioLogs = findViewById(R.id.viewRadioLogs);
        TextView viewADBLogs = findViewById(R.id.viewADBLogs);
        TextView viewKernelLogs = findViewById(R.id.viewKernelLogs);


        File RadioLogsFolder = MainActivity.RadioLogsFolder;
        File ADBLogsFolder = MainActivity.ADBLogsFolder;
        File KernelLogsFolder = MainActivity.KernelLogsFolder;

        mylogs = new ArrayList<>();


        LogsAdapter logsAdapter = new LogsAdapter(mylogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mylogs.addAll(fetchLogs(RadioLogsFolder));
        mylogs.addAll(fetchLogs(ADBLogsFolder));
        mylogs.addAll(fetchLogs(KernelLogsFolder));

        recyclerView.setAdapter(logsAdapter);
        
        


        viewAllLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylogs.clear();
                mylogs.addAll(fetchLogs(RadioLogsFolder));
                mylogs.addAll(fetchLogs(ADBLogsFolder));
                mylogs.addAll(fetchLogs(KernelLogsFolder));

//                logsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(logsAdapter);
            }
        });

        viewRadioLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylogs.clear();
                mylogs.addAll(fetchLogs(RadioLogsFolder));
                recyclerView.setAdapter(logsAdapter);
            }
        });
        viewADBLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylogs.clear();
                Toast.makeText(ViewAllLogs.this, "Showing ADB Logs only", Toast.LENGTH_SHORT).show();
                mylogs.addAll(fetchLogs(ADBLogsFolder));
                recyclerView.setAdapter(logsAdapter);
            }
        });
        viewKernelLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylogs.clear();
                mylogs.addAll(fetchLogs(KernelLogsFolder));
                recyclerView.setAdapter(logsAdapter);
            }
        });


        deleteSelectedLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(LogsAdapter.checkedLogs.size()==0)
                {
                    Toast.makeText(ViewAllLogs.this, "Please select atleast one file", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (File myFile :
                        LogsAdapter.checkedLogs) {
                    myFile.delete();
                    mylogs.remove(myFile);
                }
                recyclerView.setAdapter(logsAdapter);
                logsAdapter.notifyDataSetChanged();
            }
        });
        
        
        
        shareSelectedLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LogsAdapter.checkedLogs.size()>0)
                Toast.makeText(ViewAllLogs.this, "Sharing the selected Files", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ViewAllLogs.this, "Please select atleast one file", Toast.LENGTH_SHORT).show();
            }
        });
        
    }

    private ArrayList<File> fetchLogs(File currentLogsFolder) {
        ArrayList<File> currentLogs = new ArrayList<>();
        File[] myFiles = currentLogsFolder.listFiles();

        for (File myfile : myFiles) {
            currentLogs.add(myfile);
        }

        return currentLogs;
    }


}