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
import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.FragmentHomeBinding;
import com.example.tenang_capstone.dialog.DailyLog;
import com.example.tenang_capstone.utils.firebase.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


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
        getBerryCount();
        getMood();
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

                    DailyLog dailyLog = new DailyLog(requireActivity(), mainViewModel.uuid, mainViewModel);
                    dailyLog.openSpecificDialog(mood, sleep, activity);

                } else {
                    Log.d("isLogAlreadyEntered", "not exist");
                    // NOT EXIST EXECUTE GET LOGS
                    DailyLog dailyLog = new DailyLog(requireActivity(), mainViewModel.uuid, mainViewModel);
                    dailyLog.openMoodDialog();
                }
            }
        });
    }

    public void getBerryCount() {
        String uid = requireActivity().getIntent().getStringExtra("uid");

        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Long berry = (Long) (task.getResult().getData().get("berry"));
                        binding.button5.setText(berry.toString());
                    }
                });
    }

    public void getMood() {
        String uid = requireActivity().getIntent().getStringExtra("uid");
        String currentDate = new Utility().currentDate();
        DocumentReference docRef = db.collection("users").document(uid).collection("logs").document(currentDate);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d("HomeFragment", "onComplete ");
                        Optional<Object> anxiousOptional = Optional.ofNullable(task.getResult().get("anxiousRate"));
                        Optional<Object> dayOptional = Optional.ofNullable(task.getResult().get("dayRate"));
                        Optional<Object> gratefulOptional = Optional.ofNullable(task.getResult().get("gratefulRate"));
                        Optional<Object> happyOptional = Optional.ofNullable(task.getResult().get("happyRate"));
                        Optional<Object> sadOptional = Optional.ofNullable(task.getResult().get("sadRate"));
                        Optional<Object> sleepHoursOptional = Optional.ofNullable(task.getResult().get("sleepHours"));
                        Optional<Object> sleepQualityOptional = Optional.ofNullable(task.getResult().get("sleepQuality"));
                        Optional<Object> stressRateOptional = Optional.ofNullable(task.getResult().get("stressRate"));

                        Long anxious = anxiousOptional.map(value -> (Long) value).orElse(0L);
                        Long day = dayOptional.map(value -> (Long) value).orElse(0L);
                        Long grateful = gratefulOptional.map(value -> (Long) value).orElse(0L);
                        Long happy = happyOptional.map(value -> (Long) value).orElse(0L);
                        Long sad = sadOptional.map(value -> (Long) value).orElse(0L);
                        Long sleep = sleepHoursOptional.map(value -> (Long) value).orElse(0L);
                        Long sleepQuality = sleepQualityOptional.map(value -> (Long) value).orElse(0L);
                        Long stressRate = stressRateOptional.map(value -> (Long) value).orElse(0L);

                        int average = 0;

                        average += anxious + happy + sad;
                        if (sleep != 0 || sleepQuality != 0) {
                            average += (sleep + sleepQuality) / 12.6;
                        }
                        if (day != 0 || grateful != 0 || stressRate != 0) {
                            average += (day + grateful + stressRate) / 3;
                        }

                        if (average <=5) {
                            binding.faceImage.setImageResource(R.drawable.sad);
                        } else if (average <=10) {
                            binding.faceImage.setImageResource(R.drawable.normal);
                        } else if (average <=15) {
                            binding.faceImage.setImageResource(R.drawable.happy);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("HomeFragment", "Failed ");
                    }
                });
    }
}