package com.example.tenang_capstone.ui.shop;

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
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    private Context context;
    private final List<ShopItemList> shopItemListList;
    private String option;

    private WhenSelected shopActivity;

    public ShopAdapter(Context context, List<ShopItemList> shopItemListList, String option, WhenSelected shopActivity) {
        this.context = context;
        this.shopItemListList = shopItemListList;
        this.option = option;
        this.shopActivity = shopActivity;
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
    public void onBindViewHolder(@NonNull ShopAdapter.MyViewHolder holder, int position) {
        ShopItemList shopItem = shopItemListList.stream().filter(item -> item.type.equals(option)).collect(Collectors.toList()).get(position);

        if (option != "color") {
            holder.itemName.setText(shopItem.name);
            holder.itemDescription.setText(shopItem.description);
            holder.itemCost.setText(shopItem.cost);
        }
        Picasso.get().load(shopItem.image).into(holder.itemImage);

        holder.itemSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ShopAdapter", "Selected is " + shopItem.name);
                onCreateDialog(context, shopItem.id, shopItem.name, shopItem.type, shopItem.cost, shopItem.image).show();
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
        public ImageView itemImage;

        public ConstraintLayout itemSelection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCost = itemView.findViewById(R.id.itemCost);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemSelection = itemView.findViewById(R.id.itemSelection);
        }
    }

    public Dialog onCreateDialog(Context context, String itemId, String name, String type, String cost, String image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Confirm ?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                        shopActivity.onItemSelected(context, itemId, name, type, cost, image);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }
}
