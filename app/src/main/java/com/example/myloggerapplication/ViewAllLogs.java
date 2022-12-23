package com.example.myloggerapplication;

import static android.os.Debug.isDebuggerConnected;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

import android.os.Debug;

public class ViewAllLogs extends AppCompatActivity {
    ArrayList<File> mylogs;
    Button shareSelectedLogs;

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_logs);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button deleteSelectedLogs = findViewById(R.id.deleteSelectedLogs);

        shareSelectedLogs = findViewById(R.id.shareSelectedLogs);

        TextView viewAllLogs = findViewById(R.id.viewAllLogs);
        TextView viewRadioLogs = findViewById(R.id.viewRadioLogs);
        TextView viewADBLogs = findViewById(R.id.viewADBLogs);
        TextView viewKernelLogs = findViewById(R.id.viewKernelLogs);


        File RadioLogsFolder = MainActivity.RadioLogsFolder;
        File ADBLogsFolder = MainActivity.ADBLogsFolder;
        File KernelLogsFolder = MainActivity.KernelLogsFolder;

        mylogs = new ArrayList<>();

        saveFileInSystem();

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

                if (LogsAdapter.checkedLogs.size() == 0) {
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
                if (LogsAdapter.checkedLogs.size() > 0) {
                    Toast.makeText(ViewAllLogs.this, "Sharing the selected Files", Toast.LENGTH_SHORT).show();
//                    Log.d("mylogss", LogsAdapter.checkedLogs.get(0).getAbsolutePath());

                    saveFileInSystem();

                } else
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

    public void saveFileInSystem() {


        Log.d("debuggingInfo", "" + isDebuggerConnected());

//
//        try {
//            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//physical device
//        adb pull /sdcard/Download/ch5.pdf /Users/tnluser/AndroidStudioProjects
//        adb push /Users/tnluser/AndroidStudioProjects/hello.txt /sdcard/Download/HelloWorld.txt

//emulator
//        adb pull /sdcard/Download/MyLoggerApplication/RadioLogs/RadioLogs_2022_12_18_09_54_51.txt  /Users/tnluser/AndroidStudioProjects
//         adb push /Users/tnluser/AndroidStudioProjects/hello.txt /sdcard/Download/HelloWorld.txt


        try {
            Log.d("mylogss", "pulling");

            Process process = null;
//                        process = Runtime.getRuntime().exec(" pull /sdcard/Download/MyLoggerApplication/RadioLogs/" + LogsAdapter.checkedLogs.get(0).getName() );//+ " /Users/tnluser/AndroidStudioProjects");
//
//                        process = Runtime.getRuntime().exec(" pull " +" /Users/tnluser/AndroidStudioProjects/hello.txt"+ "/sdcard/Download/helllo.txt");
//                        process = Runtime.getRuntime().exec("adb pull " + "/sdcard/Download/RadioLOGS.txt" + " /Users/tnluser/AndroidStudioProjects");

//                        process=Runtime.getRuntime().exec("push /storage/emulated/0/Download/MyLoggerApplication/RadioLogs/RadioLogs_2022_12_18_09_08_50.txt  /Users/tnluser/AndroidStudioProjects/mylog1.txt");
//            process = Runtime.getRuntime().exec(" pull /sdcard/Download/RadioLOGS.txt /Users/tnluser/AndroidStudioProjects/MyLoggerApplication --ez enabled true");
            process = Runtime.getRuntime().exec(" push /sdcard/Download/RadioLOGS.txt /Users/tnluser/AndroidStudioProjects/MyLoggerApplication ");
//
//
//            process=Runtime.getRuntime().exec("push "+LogsAdapter.checkedLogs.get(0).getAbsolutePath());

            Log.d("mylogss", "pulled");
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while (true) {

                try {
                    if (!((line = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("mylogss", "pulled " + line);
            }


//                    adb pull /sdcard/Download/RadioLOGS.txt /Users/tnluser/AndroidStudioProjects/MyLoggerApplication


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogsAdapter.checkedLogs.clear();
    }
}