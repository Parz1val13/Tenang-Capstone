package com.example.tenang_capstone.ui.overview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String uid;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OverviewViewModel overviewViewModel =
                new ViewModelProvider(this).get(OverviewViewModel.class);

        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        uid = requireActivity().getIntent().getStringExtra("uid");
//        final TextView textView = binding.textDashboard;
//        overviewViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar currentCalendar = Calendar.getInstance();
        Drawable goodColor = ContextCompat.getDrawable(view.getContext(), R.drawable.round_green);
        Drawable normalColor = ContextCompat.getDrawable(view.getContext(), R.drawable.round_yellow);
        Drawable badColor = ContextCompat.getDrawable(view.getContext(), R.drawable.round_red);

        List<CalendarDay> good = new ArrayList<>();
        List<CalendarDay> normal = new ArrayList<>();
        List<CalendarDay> bad = new ArrayList<>();

        CollectionReference docRef = db.collection("users").document(uid).collection("logs");
        docRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int averageLog = 0;
                            Log.d("Overview", document.getId() + " => " + document.getData());
                            if (document.contains("anxiousRate")) {
                                averageLog += (Long) document.get("anxiousRate");
                                averageLog += (Long) document.get("happyRate");
                                averageLog += (Long) document.get("sadRate");
                            }
                            if (document.contains("sleepHours")) {
                                int sleepRate = 0;
                                sleepRate += (Long) document.get("sleepHours");
                                sleepRate += (Long) document.get("sleepQuality");

                                sleepRate = (int) (sleepRate / 12.6);
                                averageLog += sleepRate;
                            }
                            if (document.contains("dayRate")) {
                                int activityRate = 0;
                                activityRate += (Long) document.get("dayRate");
                                activityRate += (Long) document.get("gratefulRate");
                                activityRate += (Long) document.get("stressRate");

                                activityRate = activityRate / 3;
                                averageLog += activityRate;
                            }
                            Log.d("Overview", "averageLog" +averageLog);
                            String[] date = document.getId().split("-"); // day, month, year

                            if (averageLog <= 5) {
                                bad.add(CalendarDay.from(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0])));
                            } else if (averageLog <= 10) {
                                normal.add(CalendarDay.from(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0])));
                            } else if (averageLog <= 15) {
                                good.add(CalendarDay.from(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0])));
                            }
                        }
                        binding.calendarView.addDecorator(new EventDecorator(goodColor, good));
                        binding.calendarView.addDecorator(new EventDecorator(normalColor, normal));
                        binding.calendarView.addDecorator(new EventDecorator(badColor, bad));
                    }
                });

//        good.add(CalendarDay.from(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), 8)); // Example: Highlight the 15th of the current month
//        good.add(CalendarDay.from(2023, Calendar.JUNE, 1)); // Example: Highlight January 1, 2023
//        normal.add(CalendarDay.from(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), 7)); // Example: Highlight the 15th of the current month
//        normal.add(CalendarDay.from(2023, Calendar.JUNE, 2)); // Example: Highlight January 1, 2023
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}