package com.example.tenang_capstone.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.tenang_capstone.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class DailyLog {
    Activity myActivity;

    public DailyLog(Activity myActivity) {
        this.myActivity = myActivity;
    }

    public void openExampleDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(myActivity);

        bottomSheetDialog.setContentView(R.layout.mood_dialog);

        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog.findViewById(R.id.moodNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
                openSleepDialog();
            }
        });
        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        SeekBar happySeekBar = bottomSheetDialog.findViewById(R.id.happySlider);
        assert happySeekBar != null;
        happySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "happySeekBar: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar sadSeekBar = bottomSheetDialog.findViewById(R.id.sadSlider);
        assert sadSeekBar != null;
        sadSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "sadSeekBar: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar anxiousSeekBar = bottomSheetDialog.findViewById(R.id.anxiousSlider);
        assert anxiousSeekBar != null;
        anxiousSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "anxiousSeekBar: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
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
                openDayDialog();
            }
        });
        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        SeekBar sleepQualitySlider = bottomSheetDialog.findViewById(R.id.sleepQualitySlider);
        assert sleepQualitySlider != null;
        sleepQualitySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "sleepQualitySlider: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar hoursOfSleepSlider = bottomSheetDialog.findViewById(R.id.hoursOfSleepSlider);
        assert hoursOfSleepSlider != null;
        hoursOfSleepSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "hoursOfSleepSlider: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bottomSheetDialog.show();
    }


    public void openDayDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(myActivity);

        bottomSheetDialog.setContentView(R.layout.your_day_dialog );

        bottomSheetDialog.setCanceledOnTouchOutside(false);

        bottomSheetDialog.findViewById(R.id.daySubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.findViewById(R.id.closeDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        SeekBar dayRatingSlider = bottomSheetDialog.findViewById(R.id.dayRatingSlider);
        assert dayRatingSlider != null;
        dayRatingSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "dayRatingSlider: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar stressSlider = bottomSheetDialog.findViewById(R.id.stressSlider);
        assert stressSlider != null;
        stressSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "stressSlider: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        SeekBar gratefulnessSlider = bottomSheetDialog.findViewById(R.id.gratefulnessSlider);
        assert gratefulnessSlider != null;
        gratefulnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FARYZ", "gratefulnessSlider: "+progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bottomSheetDialog.show();
    }

}
