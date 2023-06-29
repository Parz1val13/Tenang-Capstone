package com.example.tenang_capstone.ui.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.databinding.ShopPageBinding;

import java.util.ArrayList;
import java.util.List;
public class ShopActivity extends AppCompatActivity {
    private ShopPageBinding binding;

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "back-pressed");
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShopPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the back button
            actionBar.setTitle("Shop");
        }

        List<ShopItemList> shopList = new ArrayList<>();
        shopList.add(new ShopItemList("shirts", "65", "description", "shirts", "1", "image"));
        shopList.add(new ShopItemList("accessories", "75", "description", "accessories", "1", "image"));
        Log.d("ShopActivity", String.valueOf(shopList.size()));

        final String[] option = {"shirts"};
        RecyclerView shopView = binding.ShopItemView;
        shopView.setLayoutManager(new LinearLayoutManager(this));
        shopView.setAdapter(new ShopAdapter(this, shopList, option[0]));

        binding.optionShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "shirt option selected");
                option[0] = "shirts";
                shopView.setAdapter(new ShopAdapter(getApplicationContext(), shopList, option[0]));
            }
        });

        binding.optionAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionAccessories selected");
                option[0] = "accessories";
                shopView.setAdapter(new ShopAdapter(getApplicationContext(), shopList, option[0]));
            }
        });

        binding.optionFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionFood selected");
                option[0] = "food";
                shopView.setAdapter(new ShopAdapter(getApplicationContext(), shopList, option[0]));
            }
        });

        binding.optionSkins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionSkins selected");
                option[0] = "skins";
                shopView.setAdapter(new ShopAdapter(getApplicationContext(), shopList, option[0]));
            }
        });

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
