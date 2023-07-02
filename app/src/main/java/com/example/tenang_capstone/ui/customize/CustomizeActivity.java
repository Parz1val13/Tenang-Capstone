package com.example.tenang_capstone.ui.customize;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.CustomizePageBinding;
import com.example.tenang_capstone.ui.shop.ShopItemList;
import com.example.tenang_capstone.utils.firebase.InventoryUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomizeActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView inventoryView;
    private CustomizePageBinding binding;
    List<ShopItemList> inventoryList = new ArrayList<>();
    String uid;
    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "back-pressed");
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomizePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Customise Your Pet");
        }

        uid = this.getIntent().getStringExtra("uid");
        InventoryUtils inventory = new InventoryUtils(uid);

        inventoryList.add(new ShopItemList("shirts", "65", "description", "shirts", "1", "https://d1w8c6s6gmwlek.cloudfront.net/retrogametees.com/products/224/974/22497459.png"));
        inventoryList.add(new ShopItemList("accessories", "75", "description", "accessories", "1", "https://d1w8c6s6gmwlek.cloudfront.net/retrogametees.com/products/224/974/22497459.png"));
        inventoryList.add(new ShopItemList("color", "75", "", "color", "1", "https://wallpaperaccess.com/full/2849664.jpg"));
        Log.d("CustomizeActivity", String.valueOf(inventoryList.size()));

        final String[] option = {"shirts"};
        inventoryView = binding.InventoryItemView;
        inventoryView.setLayoutManager(new LinearLayoutManager(this));
        inventoryView.setAdapter(new CustomizeAdapter(this, inventoryList, option[0], inventory::onSelection));
        getInventoryItem();
        getAvatar(this);
        binding.optionShirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "shirt option selected");
                option[0] = "shirts";
                inventoryView.setAdapter(new CustomizeAdapter(binding.getRoot().getContext(), inventoryList, option[0], inventory::onSelection));
            }
        });

        binding.optionAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionAccessories selected");
                option[0] = "accessories";
                inventoryView.setAdapter(new CustomizeAdapter(binding.getRoot().getContext(), inventoryList, option[0], inventory::onSelection));
            }
        });

        binding.optionColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionColor selected");
                option[0] = "color";
                inventoryView.setAdapter(new CustomizeAdapter(binding.getRoot().getContext(), inventoryList, option[0], inventory::onSelection));
            }
        });

        binding.optionSkins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopActivity", "optionSkins selected");
                option[0] = "skins";
                inventoryView.setAdapter(new CustomizeAdapter(binding.getRoot().getContext(), inventoryList, option[0], inventory::onSelection));
            }
        });
    }

    public void getAvatar(CustomizeActivity customizeActivity) {
        String uid = this.getIntent().getStringExtra("uid");
        CollectionReference docRef = db.collection("users").document(uid).collection("avatar");
        docRef.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            System.err.println("Listen failed: " + error);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                                if (documentSnapshot.getId().equals("shirts")) {
                                    Picasso.get().load(documentSnapshot.get("image").toString()).into(binding.userAvatar.box3);
                                }
                                if (documentSnapshot.getId().equals("accessories")) {
                                    Picasso.get().load(documentSnapshot.get("image").toString()).into(binding.userAvatar.box2);
                                }
                                if (documentSnapshot.getId().equals("color")) {
                                    SharedPreferences sharedPreferences = customizeActivity.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);

                                    sharedPreferences.edit().putString(uid, documentSnapshot.get("name").toString()).apply();
                                    Log.d("ShopActivity", "color selected " + sharedPreferences.getString(uid, "yellow"));

                                    if (documentSnapshot.get("name").toString().equals("yellow")) {
                                        binding.userAvatar.faceImage.setImageResource(R.drawable.yellowpet);
                                    } else {
                                        binding.userAvatar.faceImage.setImageResource(R.drawable.bluepet);
                                    }
                                }
                            }
                        } else {
                            System.out.print("Current data: null");
                        }
                    }
                }
        );
    }

    public void getInventoryItem() {
        CollectionReference docRef = db.collection("users").document(uid).collection("inventory");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        inventoryList.clear();
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()) {
                            inventoryList.add(
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
                        inventoryView.getAdapter().notifyDataSetChanged();
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
