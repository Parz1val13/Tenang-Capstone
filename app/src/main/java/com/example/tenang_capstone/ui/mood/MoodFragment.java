package com.example.tenang_capstone.ui.mood;

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
import com.example.tenang_capstone.custom.DayAxisValueFormatter;
import com.example.tenang_capstone.databinding.FragmentMoodBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MoodFragment extends Fragment implements OnChartValueSelectedListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FragmentMoodBinding binding;
    private BarChart chart1;
    private BarChart chart2;
    private BarChart chart3;
    MaterialDatePicker datePicker;

    private MainViewModel mainViewModel;


    ArrayList<BarEntry> values = new ArrayList<>();
    ArrayList<BarEntry> values2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoodBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        chart1 = binding.chart1;
        chart2 = binding.chart2;

        chart1.setOnChartValueSelectedListener(this);
        chart2.setOnChartValueSelectedListener(this);
        chart1.setDrawBarShadow(false);
        chart2.setDrawBarShadow(false);
        chart1.setDrawValueAboveBar(true);
        chart2.setDrawValueAboveBar(true);

        chart1.getDescription().setEnabled(false);
        chart2.getDescription().setEnabled(false);
//        chart.setMaxVisibleValueCount(60);

        chart1.setPinchZoom(false);
        chart2.setPinchZoom(false);

        chart1.setDrawGridBackground(false);
        chart2.setDrawGridBackground(false);

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(new CustomXAxisValueFormatter());

        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(1f); // only intervals of 1 day
        xAxis2.setLabelCount(7);
        xAxis2.setValueFormatter(new CustomXAxisValueFormatter());

        Legend l = chart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        Legend l2 = chart2.getLegend();
        l2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l2.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l2.setDrawInside(false);
        l2.setForm(Legend.LegendForm.SQUARE);
        l2.setFormSize(9f);
        l2.setTextSize(11f);
        l2.setXEntrySpace(4f);


        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setTheme(R.style.materialCalendar)
                .build();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(requireActivity().getSupportFragmentManager(), "tag");
            }
        });
        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Date date = new Date((Long) selection);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(date);
                Log.d("MoodFragment", "Formatted Date: " + formattedDate);

                getData(formattedDate);
            }
        });
    }

    private void setData() {

        BarDataSet set1;
        BarDataSet set2;

        if (chart1.getData() != null &&
                chart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart1.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart1.getData().notifyDataChanged();
            chart1.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "Activity");
            BarData data = new BarData(set1);

            chart1.setData(data);
        }

        if (chart2.getData() != null &&
                chart2.getData().getDataSetCount() > 0) {
            set2 = (BarDataSet) chart2.getData().getDataSetByIndex(0);
            set2.setValues(values2);
            chart2.getData().notifyDataChanged();
            chart2.notifyDataSetChanged();

        } else {
            set2 = new BarDataSet(values2, "Sleep");
            BarData data = new BarData(set2);

            chart2.setData(data);
        }


    }

    private float convertDateToFloat(String dateString) {
        float floatValue = 0.0f;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(dateFormat.parse(dateString)));

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;

            floatValue = day + month / 100.0f;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return floatValue;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void getData(String dateString) {
        DocumentReference docRef = db.collection("users").document(mainViewModel.uuid).collection("logs").document(dateString);

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Log.d("MoodFragment", "getData");
                        Long sleepHours = 0L;
                        Long sleepQuality = 0L;
                        Long dayRate = 0L;
                        Long gratefulRate = 0L;
                        Long stressRate = 0L;
                        if (task.getResult().contains("sleepHours")) {
                            sleepHours = (Long) task.getResult().get("sleepHours");
                        }
                        if (task.getResult().contains("sleepQuality")) {
                            sleepQuality = (Long) task.getResult().get("sleepQuality");
                        }
                        if (task.getResult().contains("dayRate")) {
                            dayRate = (Long) task.getResult().get("dayRate");
                        }
                        if (task.getResult().contains("gratefulRate")) {
                            gratefulRate = (Long) task.getResult().get("gratefulRate");
                        }
                        if (task.getResult().contains("stressRate")) {
                            gratefulRate = (Long) task.getResult().get("stressRate");
                        }
                        Log.d("MoodFragment", "sleep: " + sleepQuality);

                        values.clear();
                        values2.clear();

                        values.add(new BarEntry(0, dayRate));
                        values.add(new BarEntry(1, gratefulRate));
                        values.add(new BarEntry(2, stressRate));

                        values2.add(new BarEntry(3, sleepHours));
                        values2.add(new BarEntry(4, sleepQuality));
                        setData();
                        chart1.invalidate();
                        chart2.invalidate();
                    }
                });
    }
}
