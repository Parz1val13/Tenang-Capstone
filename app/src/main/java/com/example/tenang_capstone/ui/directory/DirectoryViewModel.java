package com.example.tenang_capstone.ui.directory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DirectoryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DirectoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Directory fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}