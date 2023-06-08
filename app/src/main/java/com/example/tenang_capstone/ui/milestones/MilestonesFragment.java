package com.example.tenang_capstone.ui.milestones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.databinding.FragmentMilestonesBinding;

public class MilestonesFragment extends Fragment {

    private FragmentMilestonesBinding binding;
    private int currentProgress = 0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MilestonesViewModel milestonesViewModel =
                new ViewModelProvider(this).get(MilestonesViewModel.class);

        binding = FragmentMilestonesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textSlideshow;
//        milestonesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressBar milestoneProgress = binding.progressBar2;

        milestoneProgress.setMax(90);

        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ml = 85;
                if (ml <= 2) {
                    currentProgress = ml *5;
                } else if (ml <= 15) {
                    currentProgress = (int) (ml * 3.7);
                } else if (ml <= 30) {
                    currentProgress = (int) (ml * 2.3);
                } else if (ml <= 60) {
                    currentProgress = (int) (ml * 1.5);
                } else if (ml <=89) {
                    currentProgress = ml;
                } else {
                    currentProgress = 90;
                }
                milestoneProgress.setProgress(currentProgress);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}