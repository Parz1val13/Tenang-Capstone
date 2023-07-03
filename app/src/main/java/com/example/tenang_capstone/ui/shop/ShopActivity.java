package com.example.tenang_capstone.ui.shop;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.databinding.ShopPageBinding;
import com.example.tenang_capstone.utils.firebase.ShopUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ShopPageBinding binding;
    private RecyclerView shopView;
    List<ShopItemList> shopList = new ArrayList<>();
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

        String uid = this.getIntent().getStringExtra("uid");
        ShopUtils shop = new ShopUtils(uid);

        shopList.add(new ShopItemList("shirts", "65", "description", "shirts", "1", "https://d1w8c6s6gmwlek.cloudfront.net/retrogametees.com/products/224/974/22497459.png"));
        shopList.add(new ShopItemList("accessories", "75", "description", "accessories", "1", "https://d1w8c6s6gmwlek.cloudfront.net/retrogametees.com/products/224/974/22497459.png"));
        shopList.add(new ShopItemList("color", "75", "", "color", "1", "https://wallpaperaccess.com/full/2849664.jpg"));
        Log.d("ShopActivity", String.valueOf(shopList.size()));

        final String[] option = {"shirts"};
        shopView = binding.ShopItemView;
        shopView.setLayoutManager(new LinearLayoutManager(this));
        shopView.setAdapter(new ShopAdapter(this, shopList, option[0], shop::onPurchaseConfirmation));
        getshopItem();
        binding.optionShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "shirt option selected");
                option[0] = "shirts";
                shopView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                shopView.setAdapter(new ShopAdapter(binding.getRoot().getContext(), shopList, option[0], shop::onPurchaseConfirmation));
            }
        });

        binding.optionAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionAccessories selected");
                option[0] = "accessories";
                shopView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                shopView.setAdapter(new ShopAdapter(binding.getRoot().getContext(), shopList, option[0], shop::onPurchaseConfirmation));
            }
        });

        binding.optionColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionColor selected");
                option[0] = "color";
                shopView.setLayoutManager(new GridLayoutManager(binding.getRoot().getContext(), 3));
                shopView.setAdapter(new ShopAdapter(binding.getRoot().getContext(), shopList, option[0], shop::onPurchaseConfirmation));
            }
        });

        binding.optionSkins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionSkins selected");
                option[0] = "skins";
                shopView.setAdapter(new ShopAdapter(binding.getRoot().getContext(), shopList, option[0], shop::onPurchaseConfirmation));
            }
        });
    }

    public void getshopItem() {
        CollectionReference docRef = db.collection("shop");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        shopList.clear();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            shopList.add(
                              new ShopItemList(
                                      Objects.requireNonNull(documentSnapshot.get("name")).toString(),
                                      Objects.requireNonNull(documentSnapshot.get("cost")).toString(),
                                      Objects.requireNonNull(documentSnapshot.get("description")).toString(),
                                      Objects.requireNonNull(documentSnapshot.get("type")).toString(),
                                      Objects.requireNonNull(documentSnapshot.getId()),
                                      Objects.requireNonNull(documentSnapshot.get("image")).toString()
                              )
                            );
                        }
                        shopView.getAdapter().notifyDataSetChanged();
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

