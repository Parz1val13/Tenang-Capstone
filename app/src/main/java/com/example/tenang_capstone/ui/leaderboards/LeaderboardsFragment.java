package com.example.tenang_capstone.ui.leaderboards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.databinding.FragmentLeaderboardsBinding;
import com.example.tenang_capstone.ui.shop.ShopItemList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardsFragment extends Fragment {

    private FragmentLeaderboardsBinding binding;

    private RecyclerView leaderboardView;

    List<FriendsList> friendsLists = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LeaderboardsViewModel leaderboardsViewModel =
                new ViewModelProvider(this).get(LeaderboardsViewModel.class);

        binding = FragmentLeaderboardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        leaderboardView = binding.friendsList;
        leaderboardView.setLayoutManager(new LinearLayoutManager(requireContext()));
        leaderboardView.setAdapter(new LeaderboardsAdapter(requireContext(), friendsLists));
        getFriends();
        return root;
    }


    public void getFriends() {
        CollectionReference docRef = db.collection("users");
        String uid = requireActivity().getIntent().getStringExtra("uid");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                friendsLists.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (!document.getId().equals(uid)) {
                        friendsLists.add(new FriendsList(document.getId(), (String) document.get("name")));
                    }
                }
                leaderboardView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}