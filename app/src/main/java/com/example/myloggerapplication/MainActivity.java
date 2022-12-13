package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


                String[] command = new String[]{"logcat", "-d", "radio", "threadtime"};
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("logcat -d radio");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String str = "";
                String line = "";

                while (true) {
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    tv.setText(line + "\n\n" + tv.getText());

                }


            }
        });


    }
}