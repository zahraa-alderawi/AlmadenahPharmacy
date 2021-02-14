package com.apps.almadenahpharmacy.ui.homeFridays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeFridaysViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeFridaysViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}