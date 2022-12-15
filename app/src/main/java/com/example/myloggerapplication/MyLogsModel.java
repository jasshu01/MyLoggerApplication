package com.example.myloggerapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyLogsModel extends ViewModel {

    MutableLiveData<String> myLogs = new MutableLiveData<>();

    public MyLogsModel() {
        myLogs.setValue("");
    }


}