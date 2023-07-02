package com.example.tenang_capstone.ui.milestones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.databinding.FragmentMilestonesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MilestonesFragment extends Fragment {

    private FragmentMilestonesBinding binding;
    private int currentProgress = 0;

    private String uid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MilestonesViewModel milestonesViewModel =
                new ViewModelProvider(this).get(MilestonesViewModel.class);

        binding = FragmentMilestonesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        uid = requireActivity().getIntent().getStringExtra("uid");

//        final TextView textView = binding.textSlideshow;
//        milestonesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressBar milestoneProgress = binding.progressBar2;
        getBerryCount();
        milestoneProgress.setMax(90);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("users").document(uid).collection("logs");
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    int ml = documentSnapshot.size();
                    Log.d("Milestone", String.valueOf(documentSnapshot.size()));
                    if (ml > 0 && ml <= 2) {
                        currentProgress = ml *5;
                        binding.textView13.setText("5 Days");
                    } else if (ml <= 5) {
                        currentProgress = ml *5;
                        binding.textView13.setText("7 Days");
                    } else if (ml <= 7) {
                        currentProgress = (int) (ml * 5.4);
                        binding.textView13.setText("15 Days");
                    } else if (ml <= 15) {
                        currentProgress = (int) (ml * 3.7);
                        binding.textView13.setText("30 Days");
                    } else if (ml <= 30) {
                        currentProgress = (int) (ml * 2.3);
                        binding.textView13.setText("60 Days");
                    } else if (ml <= 60) {
                        currentProgress = (int) (ml * 1.5);
                        binding.textView13.setText("90 Days");
                    } else if (ml <=89) {
                        currentProgress = ml;
                    } else {
                        currentProgress = 90;
                    }
                    milestoneProgress.setProgress(currentProgress);
                    binding.textView44.setText(ml+" day streak");
                });
    }

    public void getBerryCount() {
        String uid = requireActivity().getIntent().getStringExtra("uid");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Long berry = (Long) (task.getResult().getData().get("berry"));
                        binding.buttonBerryCount.setText(berry.toString());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}