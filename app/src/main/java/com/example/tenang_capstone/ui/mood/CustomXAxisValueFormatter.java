package com.example.tenang_capstone.ui.mood;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class CustomXAxisValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        if (value == 0) {
            return "dayRate";
        } if (value == 1) {
            return "gratefulRate";
        } if (value == 2) {
            return "stressRate";
        } if (value == 3) {
            return "sleepHours";
        } if (value == 4) {
            return "sleepQuality";
        }else {
            // Handle other float values if necessary
            return String.valueOf(value);
        }
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        return getFormattedValue(value);
    }
}
