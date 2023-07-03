package com.example.tenang_capstone.ui.panic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenang_capstone.databinding.PanicPageBinding;

public class PanicActivity extends AppCompatActivity {

    private PanicPageBinding binding;

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "back-pressed");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}