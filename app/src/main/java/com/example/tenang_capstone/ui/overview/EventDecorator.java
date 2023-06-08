package com.example.tenang_capstone.ui.overview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    private int highlightColor;
    private HashSet<CalendarDay> highlightedDates;
    private Drawable highlightDrawable;

    public EventDecorator(int highlightColor, Drawable highlightDrawable, Collection<CalendarDay> dates) {
        this.highlightColor = highlightColor;
        this.highlightedDates = new HashSet<>(dates);
        this.highlightDrawable = highlightDrawable;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return highlightedDates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.addSpan(new DotSpan(10, highlightColor));
        view.setBackgroundDrawable(highlightDrawable);
    }

}
