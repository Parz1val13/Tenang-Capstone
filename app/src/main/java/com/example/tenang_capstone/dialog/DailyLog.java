package com.example.tenang_capstone.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.tenang_capstone.MainViewModel;
import com.example.tenang_capstone.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailyLog {
    Activity myActivity;
    String uid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    int happyRate = 0;
    int sadRate = 0;
    int anxiousRate = 0;

    int sleepRate = 0;
    int sleepHours = 0;

    int dayRate = 0;
    int stressRate = 0;
    int gratefulRate = 0;

    boolean mood = false, sleep = false, activity = false;
    MainViewModel mainViewModel;
    public DailyLog(Activity myActivity, String uuid, MainViewModel mainViewModel) {
        this.myActivity = myActivity;
        this.uid = uuid;
        this.mainViewModel = mainViewModel;
    }

    public void openSpecificDialog(boolean mood, boolean sleep, boolean activity) {
        this.mood = mood; this.sleep = sleep; this.activity = activity;

        if (!mood) {
            openMoodDialog();
        } else if (!sleep) {
            openSleepDialog();
        } else if (!activity) {
            openActivityDialog();
        }
    }

    public void openMoodDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(myActivity);

        bottomSheetDialog.setContentView(R.layout.mood_dialog);

        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog.findViewById(R.id.moodNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                submitMood();
                if (!sleep) {
                    openSleepDialog();
                } else if (!activity) {
                    openActivityDialog();
                }
            }
        });
        bottomSheetDialog.findViewById(R.id.moodSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.skip = true;
                bottomSheetDialog.cancel();
                happyRate = 0;
                sadRate = 0;
                anxiousRate = 0;
                openSleepDialog();
                if (!sleep) {
                    openSleepDialog();
                } else if (!activity) {
                    openActivityDialog();
                }
            }
        });
        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                mainViewModel.skip = true;
            }
        });

        SeekBar happySeekBar = bottomSheetDialog.findViewById(R.id.happySlider);
        assert happySeekBar != null;
        happySeekBar.setMax(5);
        happySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "happySeekBar: "+progress);
                happyRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (sadRate > 0 || anxiousRate > 0) {
                    seekBar.setMax(0);
                } else {
                    seekBar.setMax(5);
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar sadSeekBar = bottomSheetDialog.findViewById(R.id.sadSlider);
        assert sadSeekBar != null;
        sadSeekBar.setMax(5);
        sadSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "sadSeekBar: "+progress);
                sadRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (happyRate > 0 || anxiousRate > 0) {
                    seekBar.setMax(0);
                } else {
                    seekBar.setMax(5);
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar anxiousSeekBar = bottomSheetDialog.findViewById(R.id.anxiousSlider);
        assert anxiousSeekBar != null;
        anxiousSeekBar.setMax(5);
        anxiousSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "anxiousSeekBar: "+progress);
                anxiousRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (happyRate > 0 || sadRate > 0) {
                    seekBar.setMax(0);
                } else {
                    seekBar.setMax(5);
                }
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bottomSheetDialog.show();
    }


    public void openSleepDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(myActivity);

        bottomSheetDialog.setContentView(R.layout.sleep_dialog);

        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog.findViewById(R.id.sleepNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                submitSleep();
                if (!activity) {
                    openActivityDialog();
                }
            }
        });
        bottomSheetDialog.findViewById(R.id.sleepSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.skip = true;
                bottomSheetDialog.cancel();
                sleepHours = 0;
                sleepRate = 0;
                if (!activity) {
                    openActivityDialog();
                }
            }
        });
        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.skip = true;
                bottomSheetDialog.cancel();
            }
        });

        SeekBar sleepQualitySlider = bottomSheetDialog.findViewById(R.id.sleepQualitySlider);
        assert sleepQualitySlider != null;
        sleepQualitySlider.setMax(5);
        sleepQualitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "sleepQualitySlider: "+progress);
                sleepRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar hoursOfSleepSlider = bottomSheetDialog.findViewById(R.id.hoursOfSleepSlider);
        assert hoursOfSleepSlider != null;
        hoursOfSleepSlider.setMax(8);
        hoursOfSleepSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "hoursOfSleepSlider: "+progress);
                sleepHours = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bottomSheetDialog.show();
    }


    public void openActivityDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(myActivity);

        bottomSheetDialog.setContentView(R.layout.your_day_dialog );

        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog.findViewById(R.id.daySubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitActivity();
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.findViewById(R.id.daySkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.skip = true;
                dayRate = 0;
                stressRate = 0;
                gratefulRate = 0;
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.skip = true;
                bottomSheetDialog.cancel();
            }
        });

        SeekBar dayRatingSlider = bottomSheetDialog.findViewById(R.id.dayRatingSlider);
        assert dayRatingSlider != null;
        dayRatingSlider.setMax(5);
        dayRatingSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "dayRatingSlider: "+progress);
                dayRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar stressSlider = bottomSheetDialog.findViewById(R.id.stressSlider);
        assert stressSlider != null;
        stressSlider.setMax(5);
        stressSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "stressSlider: "+progress);
                stressRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar gratefulnessSlider = bottomSheetDialog.findViewById(R.id.gratefulnessSlider);
        assert gratefulnessSlider != null;
        gratefulnessSlider.setMax(5);

        gratefulnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "gratefulnessSlider: "+progress);
                gratefulRate = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bottomSheetDialog.show();
    }

    void submitMood() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        Map<String, Object> user = new HashMap<>();
        user.put("happyRate", happyRate);
        user.put("sadRate", sadRate);
        user.put("anxiousRate", anxiousRate);

        DocumentReference docRef = db.collection("users").document(uid).collection("logs").document(formattedDate);
        docRef.set(user, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d("SubmitMoodLog", "onSuccess");
                    getBerries();
                });
    }

    void submitSleep() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        Map<String, Object> user = new HashMap<>();
        user.put("sleepQuality", sleepRate);
        user.put("sleepHours", sleepHours);

        DocumentReference docRef = db.collection("users").document(uid).collection("logs").document(formattedDate);
        docRef.set(user, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d("SubmitSleepLog", "onSuccess");
                    getBerries();
                });
    }

    void submitActivity() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        Map<String, Object> user = new HashMap<>();
        user.put("dayRate", dayRate);
        user.put("stressRate", stressRate);
        user.put("gratefulRate", gratefulRate);

        DocumentReference docRef = db.collection("users").document(uid).collection("logs").document(formattedDate);
        docRef.set(user, SetOptions.merge())
                .addOnSuccessListener(unused -> {
                    Log.d("SubmitActivityLog", "onSuccess");
                    getBerries();
                });
    }


    void getBerries() {
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    Long berry = (Long) documentSnapshot.get("berry");
                    addBerries(berry);
                });
    }

    void addBerries(Long berry) {
        Map<String, Object> user = new HashMap<>();
        user.put("berry", berry+2);
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.set(user, SetOptions.merge());
    }
}
