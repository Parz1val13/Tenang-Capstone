package com.example.tenang_capstone.ui.friends_profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.FriendsProfilePageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class FriendsProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FriendsProfilePageBinding binding;

    String profileId;

    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "back-pressed");
        super.onBackPressed();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        binding = FriendsProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBar.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        profileId = this.getIntent().getStringExtra("profile");

        DocumentReference doc = db.collection("users").document(profileId);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                binding.profileName.setText("@"+task.getResult().get("name").toString());
            }
        });


        CollectionReference docRef = db.collection("users").document(profileId).collection("avatar");
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots.getDocuments()) {
                            if (documentSnapshot.getId().equals("shirts")) {
                                Picasso.get().load(documentSnapshot.get("image").toString()).into(binding.userAvatar.box3);
                            }
                            if (documentSnapshot.getId().equals("accessories")) {
                                Picasso.get().load(documentSnapshot.get("image").toString()).into(binding.userAvatar.box2);
                            }
                            if (documentSnapshot.getId().equals("color")) {
                                if (documentSnapshot.get("color").equals("yellow")) {
                                    binding.userAvatar.faceImage.setImageResource(R.drawable.yellowpet);
                                } else {
                                    binding.userAvatar.faceImage.setImageResource(R.drawable.bluepet);
                                }
                            }
                        }
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
