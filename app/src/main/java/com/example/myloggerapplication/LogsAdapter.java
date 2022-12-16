package com.example.myloggerapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {

    ArrayList<File> localDataSet = null;
    static ArrayList<File> checkedLogs = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {


        CheckBox myItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myItem = itemView.findViewById(R.id.checkBox);

            myItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if (b) {
                        checkedLogs.add(localDataSet.get(getAdapterPosition()));
                    } else {
                        checkedLogs.remove(localDataSet.get(getAdapterPosition()));
                    }

                    Log.d("mylogs", getAdapterPosition() + " " + b + " " + checkedLogs.toString());
                }
            });
        }
    }


    public LogsAdapter(ArrayList<File> dataSet) {
        this.localDataSet = dataSet;
    }


    @NonNull
    @Override
    public LogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LogsAdapter.ViewHolder holder, int position) {

        holder.myItem.setText(localDataSet.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
