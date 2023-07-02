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
    ArrayList<BarEntry> values3 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMoodBinding.inflate(inflater, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        chart1 = binding.chart1;
        chart2 = binding.chart2;
        chart3 = binding.chart3;

        chart1.setOnChartValueSelectedListener(this);
        chart2.setOnChartValueSelectedListener(this);
        chart3.setOnChartValueSelectedListener(this);
        chart1.setDrawBarShadow(false);
        chart2.setDrawBarShadow(false);
        chart3.setDrawBarShadow(false);
        chart1.setDrawValueAboveBar(true);
        chart2.setDrawValueAboveBar(true);
        chart3.setDrawValueAboveBar(true);

        chart1.getDescription().setEnabled(false);
        chart2.getDescription().setEnabled(false);
        chart3.getDescription().setEnabled(false);
//        chart.setMaxVisibleValueCount(60);

        chart1.setPinchZoom(false);
        chart2.setPinchZoom(false);
        chart3.setPinchZoom(false);

        chart1.setDrawGridBackground(false);
        chart2.setDrawGridBackground(false);
        chart3.setDrawGridBackground(false);

        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart1);
        IAxisValueFormatter xAxisFormatter2 = new DayAxisValueFormatter(chart2);
        IAxisValueFormatter xAxisFormatter3 = new DayAxisValueFormatter(chart3);

        XAxis xAxis = chart1.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        XAxis xAxis2 = chart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        xAxis2.setGranularity(1f); // only intervals of 1 day
        xAxis2.setLabelCount(7);
        xAxis2.setValueFormatter(xAxisFormatter2);

        XAxis xAxis3 = chart3.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setDrawGridLines(false);
        xAxis3.setGranularity(1f); // only intervals of 1 day
        xAxis3.setLabelCount(7);
        xAxis3.setValueFormatter(xAxisFormatter3);

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

        Legend l3 = chart3.getLegend();
        l3.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l3.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l3.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l3.setDrawInside(false);
        l3.setForm(Legend.LegendForm.SQUARE);
        l3.setFormSize(9f);
        l3.setTextSize(11f);
        l3.setXEntrySpace(4f);

        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setData();

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
            }
        });
    }

    private void setData() {

        values.add(new BarEntry(convertDateToFloat("01-07-2023"), 13));
        values.add(new BarEntry(convertDateToFloat("02-07-2023"), 11));
        values.add(new BarEntry(convertDateToFloat("03-07-2023"), 8));

        BarDataSet set1;
        BarDataSet set2;
        BarDataSet set3;

        if (chart1.getData() != null &&
                chart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart1.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart1.getData().notifyDataChanged();
            chart1.notifyDataSetChanged();

        } else {
            set1 = new BarDataSet(values, "The year 2017");
            BarData data = new BarData(set1);

            chart1.setData(data);
        }

        if (chart2.getData() != null &&
                chart2.getData().getDataSetCount() > 0) {
            set2 = (BarDataSet) chart2.getData().getDataSetByIndex(0);
            set2.setValues(values);
            chart2.getData().notifyDataChanged();
            chart2.notifyDataSetChanged();

        } else {
            set2 = new BarDataSet(values, "The year 2017");
            BarData data = new BarData(set2);

            chart2.setData(data);
        }

        if (chart3.getData() != null &&
                chart3.getData().getDataSetCount() > 0) {
            set3 = (BarDataSet) chart3.getData().getDataSetByIndex(0);
            set3.setValues(values);
            chart3.getData().notifyDataChanged();
            chart3.notifyDataSetChanged();

        } else {
            set3 = new BarDataSet(values, "The year 2017");
            BarData data = new BarData(set3);

            chart3.setData(data);
        }

    }

    private float convertDateToFloat(String dateString) {
        float floatValue = 0.0f;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Objects.requireNonNull(dateFormat.parse(dateString)));

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH)+1;

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
        DocumentReference docRef = db.collection("user").document(mainViewModel.uuid).collection("logs").document(dateString);

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Long sleepHours = 0L;
                        Long sleepQuality = 0L;
                        Long dayRate = 0L;
                        Long gratefulRate = 0L;

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

                        Long sleep = sleepHours + sleepQuality;
                        Long activity = dayRate + gratefulRate;


                    }
                });
    }
}
