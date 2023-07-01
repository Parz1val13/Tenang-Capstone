package com.example.tenang_capstone.utils.firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ShopUtils {
    private String uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ShopUtils(String uid) {
        this.uid = uid;
    }

    public void onPurchaseConfirmation(Context context, String itemId, String name, String description, String type, String cost, String image) {
        // Check berry balance
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Long berry = (Long) (task.getResult().getData().get("berry"));
                        Long balance = berry - Integer.parseInt(cost);
                        Log.d("ShopUtils", "onComplete:balance: " + balance);
                        Log.d("ShopUtils", "onComplete:berry: " + berry);
                        if (balance > 0) {
                            // Eligable to purchase
                            CollectionReference colRef = db.collection("users").document(uid).collection("inventory");

                            Map<String, Object> item = new HashMap<>();
                            item.put("itemId", itemId);
                            item.put("name", name);
                            item.put("cost", cost);
                            item.put("description", description);
                            item.put("type", type);
                            item.put("image", image);

                            colRef.get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for(DocumentSnapshot document: task.getResult()) {
                                                if (document.get("itemId").toString().equals(itemId)) {
                                                    Toast.makeText(context, "Item Already Purchased", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            }
                                            colRef.add(item)
                                                    .addOnSuccessListener(unused -> Log.d("ShopUtils", "onPurchaseConfirmation:success"))
                                                    .addOnFailureListener(unused -> Log.d("ShopUtils", "onPurchaseConfirmation:failed"));

                                            Map<String, Long> data = new HashMap<>();
                                            data.put("berry", balance);
                                            docRef.set(data, SetOptions.merge());

                                            Toast.makeText(context, "Purchase Complete!", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Not enough berry", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
