package com.example.tenang_capstone.ui.leaderboards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LeaderboardsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Leaderboards fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}