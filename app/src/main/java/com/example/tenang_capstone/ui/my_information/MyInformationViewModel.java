package com.example.tenang_capstone.ui.my_information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyInformationViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyInformationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my information fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}