package com.example.tenang_capstone.ui.friends;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.ui.friends_profile.FriendsProfileActivity;
import com.example.tenang_capstone.ui.leaderboards.FriendsList;
import com.example.tenang_capstone.ui.shop.ShopActivity;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    private Activity activity;
    private final List<FriendsList> friendsLists;


    public FriendsAdapter(Activity activity, List<FriendsList> friendsLists) {
        this.activity = activity;
        this.friendsLists = friendsLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(friendsLists.get(position).name);
        holder.status.setText("Online");

        holder.visitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity.getApplicationContext(), FriendsProfileActivity.class);
                intent.putExtra("profile", friendsLists.get(position).id);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView status;

        public ImageButton visitProfile;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friendsName);
            status = itemView.findViewById(R.id.friendsOnlineStatus);
            visitProfile = itemView.findViewById(R.id.visitProfile);
        }
    }
}
