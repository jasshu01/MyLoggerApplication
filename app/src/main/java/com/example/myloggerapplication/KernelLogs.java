package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.here.oksse.OkSse;
import com.here.oksse.ServerSentEvent;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;

public class KernelLogs extends AppCompatActivity {

    TextView tv;
    Button start;
    Boolean flag;
    private MyLogsModel myLogsModel;
    boolean mainActivityisOpen = false;
    final String[] str = {""};
    String uploadData="";

    boolean activityIsOpen = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kernel_logs);
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

        start.setText("Capture Kernel Logs");
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
                    try {
                        stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }
        });


        String path="http://192.168.1.1:5000/events";

        Request request = new Request.Builder().url(path).build();

        OkSse okSse = new OkSse();
        ServerSentEvent sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
            @Override
            public void onOpen(ServerSentEvent sse, okhttp3.Response response) {
                Log.d("oksee","connection open");
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                Log.d("oksee","Event : "+event+"\nMessage: "+message);
            }

            @Override
            public void onComment(ServerSentEvent sse, String comment) {

            }

            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                return false;
            }

            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, okhttp3.Response response) {
                return false;
            }

            @Override
            public void onClosed(ServerSentEvent sse) {

            }

            @Override
            public okhttp3.Request onPreRetry(ServerSentEvent sse, okhttp3.Request originalRequest) {
                return null;
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

        String line = "";

        int i = 0;
        while (true && flag) {

            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }


            str[0] = line + "\n\n" + str[0];


//
            String finalLine = line;


            if (activityIsOpen)
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


    void stop() throws IOException {
        uploadData=tv.getText().toString();

        start.setText("Capture Kernel Logs");
        Toast.makeText(KernelLogs.this, "Stopping", Toast.LENGTH_SHORT).show();
        str[0] = "";
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
            FileName = "KernelLogs_" + (dtf.format(now) + ".txt");
        }


        File file = new File(MainActivity.KernelLogsFolder, FileName);
        writeTextData(file, (String) tv.getText());
        tv.setText("File Saved @ " + file.getAbsolutePath());


//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" `# change to match the file's MIME type` \
//                -d "Example Data"             `# to upload a file: --data-binary @file.jpg`

//        curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary \
//        -H "Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9" \
//        -H "Content-Type: text/plain" \
//                -data-binary @output.txt
//
//
        new PerformTask().execute();

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


    class PerformTask extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... voids) {
            String data = tv.getText().toString();
            String uploadcommand = "curl https://api.upload.io/v2/accounts/FW25b1c/uploads/binary -H " + "\"Authorization: Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9\" -H \"Content-Type: text/plain\" -d \"" + data + "\"";
//
            Log.d("uploading", uploadcommand);
//        Runtime.getRuntime().exec(uploadcommand);


            // avoid creating several instances, should be singleon
//            OkHttpClient client = new OkHttpClient();
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), uploadData);
//            Request request = new Request.Builder()
//                    .header("Authorization ", "Bearer public_FW25b1cGPvomqGHEbkpyKP17i1N9")
//                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
//                    .post(body)
//                    .build();
//
//            Response response = null;
//            try {
//                response = client.newCall(request).execute();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                Log.d("uploading", response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }

        protected void onPostExecute(Void feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}