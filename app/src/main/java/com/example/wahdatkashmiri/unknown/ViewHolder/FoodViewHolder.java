package com.example.wahdatkashmiri.unknown.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wahdatkashmiri.unknown.Interface.ItemClickListener;
import com.example.wahdatkashmiri.unknown.R;

/**
 * Created by WAHDAT KASHMIRI on 01-04-2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_name=itemView.findViewById(R.id.food_name);
        food_image=itemView.findViewById(R.id.food_image);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    itemClickListener.OnClick(view,getAdapterPosition(),false);
    }
}
