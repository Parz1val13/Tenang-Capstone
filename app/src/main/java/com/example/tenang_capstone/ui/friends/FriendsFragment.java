package com.example.tenang_capstone.ui.friends;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.databinding.FragmentFriendsBinding;
import com.example.tenang_capstone.ui.leaderboards.FriendsList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private RecyclerView friendsView;

    List<FriendsList> friendsLists = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String search = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FriendsViewModel friendsViewModel =
                new ViewModelProvider(this).get(FriendsViewModel.class);

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        friendsView = binding.friendsView;
        friendsView.setLayoutManager(new LinearLayoutManager(requireContext()));
        friendsView.setAdapter(new FriendsAdapter(requireContext(), friendsLists));
        getFriends(search);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textField.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search = s.toString();
            }
        });

        binding.textField.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FriendsFragment", "search: " + search);
                getFriends(search);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getFriends(String search) {
        CollectionReference docRef = db.collection("users");
        String uid = requireActivity().getIntent().getStringExtra("uid");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                friendsLists.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (!document.getId().equals(uid)) {
                        if (!search.isEmpty() && document.get("name").toString().contains(search)) {
                            friendsLists.add(new FriendsList(document.getId(), (String) document.get("name"),
                                    (Long) document.get("berry")));
                        }
                        if (search.isEmpty()) {
                            friendsLists.add(new FriendsList(
                                    document.getId(),
                                    (String) document.get("name"),
                                    (Long) document.get("berry")
                            ));
                        }
                    }
                }
                friendsView.getAdapter().notifyDataSetChanged();
            }
        });
    }
}