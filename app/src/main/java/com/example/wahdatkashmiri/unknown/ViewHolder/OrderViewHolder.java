package com.example.wahdatkashmiri.unknown.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.wahdatkashmiri.unknown.Interface.ItemClickListener;
import com.example.wahdatkashmiri.unknown.R;

/**
 * Created by WAHDAT KASHMIRI on 05-04-2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtOrderid,txtOrderStatus,txtOrderphone,txtOrderName,txtOrderAddress;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderid = itemView.findViewById(R.id.order_id);
        txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderphone = itemView.findViewById(R.id.order_phone);
        txtOrderAddress = itemView.findViewById(R.id.order_address);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
  itemClickListener.OnClick(view,getAdapterPosition(),false);
    }
}
