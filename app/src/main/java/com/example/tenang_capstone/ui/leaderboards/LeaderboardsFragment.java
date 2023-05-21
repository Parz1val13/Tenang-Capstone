package com.example.tenang_capstone.ui.leaderboards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tenang_capstone.databinding.FragmentLeaderboardsBinding;

public class LeaderboardsFragment extends Fragment {

    private FragmentLeaderboardsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardsViewModel leaderboardsViewModel =
                new ViewModelProvider(this).get(LeaderboardsViewModel.class);

        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        leaderboardsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}