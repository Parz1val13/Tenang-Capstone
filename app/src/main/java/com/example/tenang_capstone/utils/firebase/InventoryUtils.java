package com.example.tenang_capstone.utils.firebase;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InventoryUtils {
    private String uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public InventoryUtils(String uid) {
        this.uid = uid;
    }

    public void onSelection(Context context, String itemId, String name, String description, String type, String cost, String image) {
        // Check berry balance
        DocumentReference docRef = db.collection("users").document(uid).collection("avatar").document(type);
        Map<String, Object> item = new HashMap<>();
        item.put("image", image);
        docRef.set(item)
                .addOnSuccessListener(unused -> Toast.makeText(context, "avatar updated!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show());

    }
}
