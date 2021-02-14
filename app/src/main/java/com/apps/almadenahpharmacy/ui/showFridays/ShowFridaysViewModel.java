package com.apps.almadenahpharmacy.ui.showFridays;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShowFridaysViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShowFridaysViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}