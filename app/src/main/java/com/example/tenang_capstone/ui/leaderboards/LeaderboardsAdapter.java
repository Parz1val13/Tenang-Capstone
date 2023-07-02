package com.example.tenang_capstone.ui.leaderboards;

import static java.lang.String.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.ui.shop.ShopAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.MyViewHolder> {
    private Context context;
    private final List<FriendsList> friendsLists;

    private String option;

    public LeaderboardsAdapter(Context context, List<FriendsList> friendsLists) {
        this.context = context;
        this.friendsLists = friendsLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.leaderboardNo.setText(format("%d", position +1));
        holder.leaderboardName.setText(friendsLists.get(position).name);
    }

    @Override
    public int getItemCount() {
        return friendsLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView leaderboardNo;
        public TextView leaderboardName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leaderboardNo = itemView.findViewById(R.id.leaderboardNo);
            leaderboardName = itemView.findViewById(R.id.leaderboardName);
        }
    }
}
