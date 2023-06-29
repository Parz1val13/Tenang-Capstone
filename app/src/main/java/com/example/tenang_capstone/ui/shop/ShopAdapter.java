package com.example.tenang_capstone.ui.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenang_capstone.R;

import java.util.List;
import java.util.stream.Collectors;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    private Context context;
    private final List<ShopItemList> shopItemListList;
    private String option;

    public ShopAdapter(Context context, List<ShopItemList> shopItemListList, String option) {
        this.context = context;
        this.shopItemListList = shopItemListList;
        this.option = option;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.MyViewHolder holder, int position) {
//        ShopItemList shopItem = shopItemListList.get(position);
        ShopItemList shopItem = shopItemListList.stream().filter(item -> item.type.equals(option)).collect(Collectors.toList()).get(position);

        holder.itemName.setText(shopItem.name);
        holder.itemDescription.setText(shopItem.description);
        holder.itemCost.setText(shopItem.cost);
        Log.d("ShopAdapter", "option2 is "+option);

//        holder.itemImage.setText(shopItem.image);
    }

    @Override
    public int getItemCount() {
        return (int) shopItemListList.stream().filter(item -> item.type.equals(option)).count();
//        return shopItemListList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        public TextView itemName;
        public TextView itemDescription;
        public TextView itemCost;
//        public TextView itemImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemCost = itemView.findViewById(R.id.itemCost);
        }
    }
}
