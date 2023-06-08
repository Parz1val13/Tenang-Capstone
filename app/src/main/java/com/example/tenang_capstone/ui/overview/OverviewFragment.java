package com.example.tenang_capstone.ui.overview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.databinding.FragmentOverviewBinding;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


public class OverviewFragment extends Fragment {

    private FragmentOverviewBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OverviewViewModel overviewViewModel =
                new ViewModelProvider(this).get(OverviewViewModel.class);

        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        overviewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar currentCalendar = Calendar.getInstance();

        List<CalendarDay> highlightedDates = new ArrayList<>();
        List<CalendarDay> highlightedDates2 = new ArrayList<>();
        highlightedDates.add(CalendarDay.from(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), 8)); // Example: Highlight the 15th of the current month
        highlightedDates.add(CalendarDay.from(2023, Calendar.JUNE, 1)); // Example: Highlight January 1, 2023
        highlightedDates2.add(CalendarDay.from(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), 7)); // Example: Highlight the 15th of the current month
        highlightedDates2.add(CalendarDay.from(2023, Calendar.JUNE, 2)); // Example: Highlight January 1, 2023

        // Set the custom highlight color
        int highlightColor = Color.parseColor("#FF0000"); // Example: Red color
        Drawable highlightDrawable = ContextCompat.getDrawable(view.getContext(), R.drawable.round_shape);
        Drawable highlightDrawable2 = ContextCompat.getDrawable(view.getContext(), R.drawable.round_green);

        // Set the custom highlight decorator
        binding.calendarView.addDecorator(new EventDecorator(highlightColor,highlightDrawable, highlightedDates));
        binding.calendarView.addDecorator(new EventDecorator(highlightColor,highlightDrawable2, highlightedDates2));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}