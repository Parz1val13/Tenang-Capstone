package com.example.tenang_capstone.ui.panic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.PanicPageBinding;
import com.example.tenang_capstone.dialog.DailyLog;

import java.util.Objects;

public class PanicActivity extends AppCompatActivity {

    private PanicPageBinding binding;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PanicPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the back button
            actionBar.setTitle("Help Center");
        }

        binding.callHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1234567890")); // Replace with the phone number you want to call
                startActivity(intent);
            }
        });
        openDialog();

    }


    public void openDialog() {
        SeekHelpDialog seekHelpDialog = new SeekHelpDialog(this);
        seekHelpDialog.openSeekHelpDialog();
    }
}