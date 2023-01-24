package com.example.myloggerapplication;

import static com.google.auth.oauth2.OAuth2Utils.JSON_FACTORY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CODE = 10001;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 10002;
    private static final int INTERNET_PERMISSION_CODE = 10003;
    TextView tv;
    Button start;
    Boolean flag;

    TextView RadioLogs, ADBLogs, KernelLogs, bugReports;
    static File ApplicationFolder, RadioLogsFolder, ADBLogsFolder, KernelLogsFolder, BugReportsFolder;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String projectId = "logical-essence-375611";
//                Storage storage = null;
//
////                try (InputStream json = new FileInputStream("/logical-essence-375611-921d1eaa1577.json")) {
////                    Credentials credentials = Credentials.fromStream(json);
////                } catch (Exception exception) {
////
////                }
//
//
////                Credentials credentials = Credentials.fromStream("json");
//
//                storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
////                    storage = StorageOptions.newBuilder().setCredentials().setProjectId(projectId).build().getService();
//                Page<Bucket> buckets = storage.list();
//
//                for (Bucket bucket : buckets.iterateAll()) {
//                    Log.d("GCP", bucket.getName());
//                }
//            }
//        }).start();


        RadioLogs = findViewById(R.id.radioLogs);
        ADBLogs = findViewById(R.id.adbLogs);
        KernelLogs = findViewById(R.id.kernelLogs);
        bugReports = findViewById(R.id.bugReports);
        bugReports.setVisibility(View.GONE);

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


//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                EXTERNAL_STORAGE_PERMISSION_CODE);
//
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_CODE);
        }
//        else {
//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    INTERNET_PERMISSION_CODE);
        }
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
//        }
//
//        else {
//            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }


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

        bugReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GenertateBugReports.class));
            }
        });


        connectToServer();


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
            startActivity(new Intent(MainActivity.this, ViewAllLogs.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private Socket socket;
    TelephonyManager telephonyManager;
    String deviceID = "";
    boolean userConnected = false;
    final String[] str = {""};
    final String[] strForServerCommand = {""};
    String uploadData = "";

    public void connectToServer() {
        telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);

        }


        deviceID = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
//                    socket = new Socket("192.168.1.11", 3000);
                    socket = IO.socket("http://192.168.1.11:3000");
                    socket.connect().emit("join", deviceID);


                    socket.on("userjoinedthechat", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            if (userConnected == false)
                                Log.d("mysocket", "message received : " + args[0]);
                            userConnected = true;
                        }
                    });
                    socket.on("start_Logging", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("mysocket", "message received : " + args[0]);
                            flag = false;

                            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                            scheduledExecutorService.schedule(new Runnable() {
                                @Override
                                public void run() {
                                    if (!flag) {
                                        flag = true;
                                        captureOnServerCommand();
                                    }

                                }
                            }, 1, TimeUnit.MILLISECONDS);

                        }


                    });
                    socket.on("stop_Logging", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Log.d("mysocket", "message received : " + args[0]);
                            try {
                                stopOnServerCommand();
                                flag = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    void captureOnServerCommand() {

        strForServerCommand[0] = "";

        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("pm grant com.example.myloggerapplication android.permission.READ_LOGS");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec("logcat all");
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


            strForServerCommand[0] = line + "\n\n" + strForServerCommand[0];


        }


    }

    void stopOnServerCommand() throws IOException {


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

        String data = strForServerCommand[0];
        Log.d("captureddata", data);


        File file = new File(MainActivity.KernelLogsFolder, FileName);
//        writeTextData(file, data);

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
        new PerformTaskOnServerCommand().execute();

    }

    class PerformTaskOnServerCommand extends AsyncTask<Void, Void, Void> {

        private Exception exception;


        @Override
        protected Void doInBackground(Void... voids) {
            uploadData = strForServerCommand[0];


            LocalDateTime now = null;
            DateTimeFormatter dtf = null;
            String FileName = "";


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
                now = LocalDateTime.now();
            }

            String currTime = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currTime = (dtf.format(now));
            }

            String finalUploadData = deviceID + "\n" + currTime + "\n\n" + uploadData;

            File file = new File(ApplicationFolder, FileName);
            writeTextData(file, finalUploadData);


            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");

            RequestBody requestBody = RequestBody.create(mediaType, new File(file.getAbsolutePath()));
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(deviceID+"_"+currTime+".txt", deviceID+"_"+currTime+".txt",requestBody)
                    .build();

//            Request request = new Request.Builder()
//                    .header("Authorization ", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
//                    .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/binary")
//                    .post(body)
//                    .build();
            Request request = new Request.Builder()
                    .url("https://api.upload.io/v2/accounts/12a1xwJ/uploads/binary")
                    .post(body)
                    .addHeader("Authorization", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
                    .build();


            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            try {
//                Log.d("uploading", response.body().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


//
//            // The ID of your GCP project
//            String projectId = "logical-essence-375611";
//
//            // The ID of your GCS bucket
//            String bucketName = "my_logger_application";
//            String objectName = deviceID + "/" + currTime + ".txt";

//            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//            BlobId blobId = BlobId.of(bucketName, objectName);
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//            byte[] content = finalUploadData.getBytes(StandardCharsets.UTF_8);
//            try {
//                storage.createFrom(blobInfo, new ByteArrayInputStream(content));
//                Log.d("GCP",
//                        "Object "
//                                + objectName
//                                + " uploaded to bucket "
//                                + bucketName
//                                + " with contents "
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//


//            // The path to your file to upload
//             String filePath = "/"+deviceID+"/"+currTime;
//
//            Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
//            BlobId blobId = BlobId.of(bucketName, objectName);
//            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
//
//            // Optional: set a generation-match precondition to avoid potential race
//            // conditions and data corruptions. The request returns a 412 error if the
//            // preconditions are not met.
//            Storage.BlobTargetOption precondition;
//            if (storage.get(bucketName, objectName) == null) {
//                // For a target object that does not yet exist, set the DoesNotExist precondition.
//                // This will cause the request to fail if the object is created before the request runs.
//                precondition = Storage.BlobTargetOption.doesNotExist();
//            } else {
//                // If the destination already exists in your bucket, instead set a generation-match
//                // precondition. This will cause the request to fail if the existing object's generation
//                // changes before the request runs.
//                precondition =
//                        Storage.BlobTargetOption.generationMatch(
//                                storage.get(bucketName, objectName).getGeneration());
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                storage.create(blobInfo, Storage.BlobTargetOption.userProject(finalUploadData), precondition);
//            }
////


//            curl https://api.upload.io/v2/accounts/12a1xwJ/uploads/form_data -H "Authorization: Bearer public_12a1xwJGEfWrJviHJAESB6scftus" -F file=@image.jpg


//            Log.d("GCP", "uploaded");
////            System.out.println(
//                    "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);


//            OkHttpClient client = new OkHttpClient();
//
//            RequestBody body = RequestBody.create(MediaType.parse("text/plain"),finalUploadData);
//            Request request = new Request.Builder()
//                    .header("Authorization ", "Bearer public_12a1xwJGEfWrJviHJAESB6scftus")
//                    .url("https://api.upload.io/v2/accounts/FW25b1c/uploads/binary")
//                    .post(body)
//                    .build();
//
//
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

//    public static String createFolder() throws IOException {
//        // Load pre-authorized user credentials from the environment.
//        // TODO(developer) - See https://developers.google.com/identity for
//        // guides on implementing OAuth2 for your application.
//        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
//                .createScoped(Arrays.asList(DriveScopes.DRIVE_FILE));
//        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
//                credentials);
//
//        // Build a new authorized API client service.
//        Drive service = new Drive.Builder(new NetHttpTransport(),
//                GsonFactory.getDefaultInstance(),
//                requestInitializer)
//                .setApplicationName("Drive samples")
//                .build();
//        // File's metadata.
//        File fileMetadata = new File();
//        fileMetadata.setName("Test");
//        fileMetadata.setMimeType("application/vnd.google-apps.folder");
//        try {
//            File file = service.files().create(fileMetadata)
//                    .setFields("id")
//                    .execute();
//            System.out.println("Folder ID: " + file.getId());
//            return file.getId();
//        } catch (GoogleJsonResponseException e) {
//            // TODO(developer) - handle error appropriately
//            System.err.println("Unable to create folder: " + e.getDetails());
//            throw e;
//        }
//    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = DriveQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

}