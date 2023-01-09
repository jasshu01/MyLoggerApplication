package com.example.myloggerapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewLog extends AppCompatActivity {
    TextView textView;
    SearchView searchView;

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);
        textView = findViewById(R.id.textView);
        searchView = findViewById(R.id.searchView);

        Intent intent = getIntent();
        String filePath = intent.getStringExtra("filePath");


        File myFile = new File(filePath);
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(myFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int byteLength = Integer.MAX_VALUE;
//        int byteLength = 1;


        byte[] buffer = new byte[1024 * 1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            try {
                if (!((byteLength = fIn.read(buffer)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] copy = Arrays.copyOf(buffer, byteLength);
            out.write(copy, 0, copy.length);
        }
        String output = out.toString();
        textView.setText(output);

        String[] mylines = output.split("\n\n");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                onQueryTextChange(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0)
                {
                    textView.setText(output);
                    return true;
                }


                String newOutput = "";
                s = s.toLowerCase();
                for (String str :
                        mylines) {
                    if (str.toLowerCase().contains(s)) {
                        newOutput += str + "\n\n";
                    }
                }
                textView.setText(newOutput);
                return true;
            }
        });

    }


}