package com.example.tenang_capstone.ui.diary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiaryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DiaryViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is diary fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}