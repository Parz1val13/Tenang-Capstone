package com.example.tenang_capstone.ui.milestones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MilestonesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MilestonesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is milestones fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}