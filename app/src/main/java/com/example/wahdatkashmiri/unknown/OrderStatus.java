package com.example.wahdatkashmiri.unknown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Interface.ItemClickListener;
import com.example.wahdatkashmiri.unknown.Model.Request;
import com.example.wahdatkashmiri.unknown.Model.User;
import com.example.wahdatkashmiri.unknown.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {



    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database= FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrders(Common.currentUser.getEmail());
    }

    private void loadOrders(String email) {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("email").equalTo(email)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {

                viewHolder.txtOrderid.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderphone.setText(model.getContact());

                viewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));



                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {

                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Placed";
        else if (status.equals("1"))
            return "Shipped";
        else
            return "Delivered";
    }
}
