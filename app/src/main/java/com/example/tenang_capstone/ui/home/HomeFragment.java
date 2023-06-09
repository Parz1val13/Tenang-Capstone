package com.example.tenang_capstone.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.MainViewModel;
import com.example.tenang_capstone.databinding.FragmentHomeBinding;
import com.example.tenang_capstone.dialog.DailyLog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HomeFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MainViewModel mainViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mainViewModel.skip) {
            isLogAlreadyEntered();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void isLogAlreadyEntered() {
        String uid = requireActivity().getIntent().getStringExtra("uid");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        Log.d("isLogAlreadyEntered", "uid :" + uid);
        DocumentReference docRef = db.collection("users").document(uid).collection("logs").document(formattedDate);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("isLogAlreadyEntered", document.getData().toString());
                    boolean mood = document.getData().containsKey("happyRate");
                    boolean sleep = document.getData().containsKey("sleepQuality");
                    boolean activity = document.getData().containsKey("dayRate");
                    Log.d("onComplete", "containKey: "+mood+", "+sleep+", "+activity);

                    DailyLog dailyLog = new DailyLog(requireActivity(), mainViewModel.uuid);
                    dailyLog.openSpecificDialog(mood, sleep, activity);

                } else {
                    Log.d("isLogAlreadyEntered", "not exist");
                    // NOT EXIST EXECUTE GET LOGS
                    DailyLog dailyLog = new DailyLog(requireActivity(), mainViewModel.uuid);
                    dailyLog.openMoodDialog();
                }
            }
        });
    }
}