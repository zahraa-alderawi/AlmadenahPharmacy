package com.apps.almadenahpharmacy.ui.employeeRecords;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeeRecordsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EmployeeRecordsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}