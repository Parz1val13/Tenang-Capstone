package com.example.tenang_capstone.utils.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserUtils {
    private String uid;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public UserUtils(String uid) {
        this.uid = uid;
    }

    public void userInfo() {
        DocumentReference docRef = db.collection("users").document(uid);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.get("name").toString();
                        String email = documentSnapshot.get("username").toString();
                        String date = documentSnapshot.get("date").toString();


                    } else {
                        Log.d("userInfo", "No such document");
                    }
                }
            }
        });
    }
}
