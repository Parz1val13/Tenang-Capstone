package com.example.tenang_capstone.ui.my_information;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.MainViewModel;
import com.example.tenang_capstone.databinding.FragmentMyInformationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyInformationFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MainViewModel mainViewModel;
    private FragmentMyInformationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyInformationViewModel myInformationViewModel =
                new ViewModelProvider(this).get(MyInformationViewModel.class);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        binding = FragmentMyInformationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        myInformationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void userInfo() {
        DocumentReference docRef = db.collection("users").document(mainViewModel.uuid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.get("name").toString();
                        String email = documentSnapshot.get("email").toString();
                        String date = documentSnapshot.get("date_joined").toString();

                        binding.myInformationName.setText(name);
                        binding.myInformationUsername.setText(email);
                        binding.myInformationDateJoined.setText(date);

                    } else {
                        Log.d("userInfo", "No such document");
                    }
                }
            }
        });
    }
}