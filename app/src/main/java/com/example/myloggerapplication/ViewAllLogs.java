package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class ViewAllLogs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_logs);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button deleteSelectedLogs = findViewById(R.id.deleteSelectedLogs);

        File RadioLogsFolder = MainActivity.RadioLogsFolder;
        File ADBLogsFolder = MainActivity.ADBLogsFolder;
        File KernelLogsFolder = MainActivity.KernelLogsFolder;

        ArrayList<File> mylogs = new ArrayList<>();
        mylogs.addAll(fetchLogs(RadioLogsFolder));

        LogsAdapter logsAdapter = new LogsAdapter(mylogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(logsAdapter);


        deleteSelectedLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (File myFile :
                        LogsAdapter.checkedLogs) {
                    myFile.delete();
                    mylogs.remove(myFile);
                }
                recyclerView.setAdapter(logsAdapter);
                logsAdapter.notifyDataSetChanged();


            }
        });


    }

    private ArrayList<File> fetchLogs(File radioLogsFolder) {
        ArrayList<File> radioLogs = new ArrayList<>();
        File[] myFiles = radioLogsFolder.listFiles();

        for (File myfile : myFiles) {
            if (myfile.getName().startsWith("Radio")) {
                radioLogs.add(myfile);
            }
        }

        return radioLogs;
    }
}