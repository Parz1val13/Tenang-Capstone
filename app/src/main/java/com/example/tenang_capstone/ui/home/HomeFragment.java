package com.example.tenang_capstone.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.MainActivity;
import com.example.tenang_capstone.MainViewModel;
import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.FragmentHomeBinding;
import com.example.tenang_capstone.dialog.DailyLog;
import com.example.tenang_capstone.ui.customize.CustomizeActivity;
import com.example.tenang_capstone.ui.panic.PanicActivity;
import com.example.tenang_capstone.ui.shop.ShopActivity;
import com.example.tenang_capstone.utils.firebase.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mainViewModel.skip) {
            isLogAlreadyEntered();
        }
        getBerryCount();
        getMood();
        getAvatar();
        binding.shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = requireActivity().getIntent().getStringExtra("uid");
                Intent intent = new Intent(getContext(), ShopActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        final int CLICK_DURATION_THRESHOLD = 500; // Threshold in milliseconds
        final long[] startTime = new long[1];

        binding.userAvatar.materialCardView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startTime[0] = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime[0];

                    if (duration >= CLICK_DURATION_THRESHOLD) {
                        // Click and hold action performed
                        // Add your custom logic here
                        Log.d("HomeFragment", "OnHold");
                        String uid = requireActivity().getIntent().getStringExtra("uid");
                        Intent intent = new Intent(getContext(), CustomizeActivity.class);
                        intent.putExtra("uid", uid);
                        startActivity(intent);
                    }
                    break;
            }
            return false;
        });
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

    public void getAvatar() {
        String uid = requireActivity().getIntent().getStringExtra("uid");
        CollectionReference docRef = db.collection("users").document(uid).collection("avatar");
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
                        }
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
                            binding.userAvatar.faceImage.setImageResource(R.drawable.sad);
                        } else if (average <=10) {
                            binding.userAvatar.faceImage.setImageResource(R.drawable.normal);
                        } else if (average <=15) {
                            binding.userAvatar.faceImage.setImageResource(R.drawable.happy);
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