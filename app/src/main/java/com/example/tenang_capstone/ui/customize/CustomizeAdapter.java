package com.example.tenang_capstone.ui.customize;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.R;
import com.example.tenang_capstone.ui.shop.ShopItemList;
import com.example.tenang_capstone.ui.shop.WhenSelected;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomizeAdapter extends RecyclerView.Adapter<CustomizeAdapter.MyViewHolder> {
    private Context context;
    private final List<ShopItemList> shopItemListList;
    private String option;

    private WhenSelected customizeActivity;

    public CustomizeAdapter(Context context, List<ShopItemList> shopItemListList, String option, WhenSelected customizeActivity) {
        this.context = context;
        this.shopItemListList = shopItemListList;
        this.option = option;
        this.customizeActivity = customizeActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (Objects.equals(option, "color")) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_color, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ShopItemList shopItem = shopItemListList.stream().filter(item -> item.type.equals(option)).collect(Collectors.toList()).get(position);

        if (!Objects.equals(option, "color")) {
            holder.itemStar.setVisibility(View.INVISIBLE);
            holder.itemCost.setVisibility(View.INVISIBLE);
            holder.itemName.setText(shopItem.name);
            holder.itemDescription.setText(shopItem.description);
            holder.itemCost.setText(shopItem.cost);
        }
        Picasso.get().load(shopItem.image).into(holder.itemImage);

        holder.itemSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopAdapter", "Selected is " + shopItem.name);
                customizeActivity.onItemSelected(context, shopItem.id, shopItem.name, shopItem.description, shopItem.type, shopItem.cost, shopItem.image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) shopItemListList.stream().filter(item -> item.type.equals(option)).count();
//        return shopItemListList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView itemName;
        public TextView itemDescription;
        public TextView itemCost;
        public ImageView itemStar;
        public ImageView itemImage;

        public ConstraintLayout itemSelection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCost = itemView.findViewById(R.id.itemCost);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemSelection = itemView.findViewById(R.id.itemSelection);
            itemStar = itemView.findViewById(R.id.itemStar);
        }
    }
}
