package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textview_logs);
        start = findViewById(R.id.button_start);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                String[] command = new String[]{"logcat", "radio", "threadtime"};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        capture();
                    }
                }).start();

            }
        });


    }


    void capture() {


        Process process = null;
        try {
            process = Runtime.getRuntime().exec("logcat radio");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String str = "";
        String line = "";
        int i = 0;
        while (true) {
            try {
                if (!((line = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

//            Log.d("mylogs", " " + i++);

            String finalLine = line;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    tv.setText(finalLine + "\n\n" + tv.getText());

                }
            });

        }


    }

}