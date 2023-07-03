package com.example.tenang_capstone.ui.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.ui.leaderboards.FriendsList;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.MyViewHolder> {

    private Context context;
    private final List<FriendsList> friendsLists;


    public FriendsAdapter(Context context, List<FriendsList> friendsLists) {
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return friendsLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.friendsName);
            status = itemView.findViewById(R.id.friendsOnlineStatus);
        }
    }
}
