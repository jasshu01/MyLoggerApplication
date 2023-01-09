package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RadioLogs extends AppCompatActivity {

    TextView tv;
    Button start;
    Boolean flag;
    private MyLogsModel myLogsModel;
    boolean activityIsOpen = false;
    final String[] str = {""};

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("RadioLogMessages");
    String id = myRef.push().getKey();

    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = "http://www.androidexample.com/media/UploadToServer.php";
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    String uploadFileName = "service_lifecycle.png";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_logs);
        myRef.child(id).setValue("Hello Radio Logs here");

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

        start.setText("Capture Radio Logs");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    stop();

                }


            }
        });


    }


    void capture() {


        try {
            Runtime.getRuntime().exec("logcat -c");

            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
            Log.d("permissionaccess", "Done");
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


            str[0] = line + "\n\n" + str[0];

            myRef.child(id).setValue(tv.getText());
            if (activityIsOpen)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (flag) {
                            myLogsModel.myLogs.setValue(str[0]);

                        }
                    }
                });


        }


    }


    void stop() {
        Toast.makeText(RadioLogs.this, "Stopping", Toast.LENGTH_SHORT).show();
        str[0] = "";
        Thread.interrupted();
        flag = false;
        start.setText("Start Capturing Radio Logs");

        LocalDateTime now = null;
        DateTimeFormatter dtf = null;
        String FileName = "";


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
            now = LocalDateTime.now();
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            FileName = "RadioLogs_" + (dtf.format(now) + ".txt");
        }


        File file = new File(MainActivity.RadioLogsFolder, FileName);
        writeTextData(file, (String) tv.getText());
        tv.setText("File Saved @ " + file.getAbsolutePath());


        new Thread(new Runnable() {
            public void run() {
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        messageText.setText("uploading started.....");
//                    }
//                });
                Log.d("Upload_file", "uploading started");
//                uploadFile(uploadFilePath + "" + uploadFileName);
                uploadFileName = file.getName();
                uploadFile(file.getAbsolutePath());

            }
        }).start();


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


    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

//        if (!sourceFile.isFile()) {
//
//            dialog.dismiss();
//
//            Log.e("uploadFile", "Source File not exist :"
//                    +uploadFilePath + "" + uploadFileName);
//
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    messageText.setText("Source File not exist :"
//                            +uploadFilePath + "" + uploadFileName);
//                }
//            });
//
//            return 0;
//
//        }
//        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.androidexample.com/media/uploads/"
                                    + uploadFileName;

//                            messageText.setText(msg);
//                            Toast.makeText(UploadToServer.this, "File Upload Complete.",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

//                  dialog.dismiss();
                ex.printStackTrace();

//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("MalformedURLException Exception : check script url.");
//
//                      }
//                  });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

//                  dialog.dismiss();
//                  e.printStackTrace();
//
//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("Got Exception : see logcat ");
//                          Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
//                                  Toast.LENGTH_SHORT).show();
//                      }
//                  });
                Log.e("Upload_file_Exception", "Exception : "
                        + e.getMessage(), e);
            }
//              dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }
}