package com.example.wahdatkashmiri.unknown;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Interface.ItemClickListener;
import com.example.wahdatkashmiri.unknown.Model.Food;
import com.example.wahdatkashmiri.unknown.ViewHolder.FoodViewHolder;
import com.example.wahdatkashmiri.unknown.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
     RelativeLayout rootLayout;
    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId="";

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database =FirebaseDatabase.getInstance();
        foodList=database.getReference("Foods");
       rootLayout=findViewById(R.id.rootLayout);
        recyclerView=findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
            if (getIntent() !=null)
                categoryId=getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty() && categoryId != null)
            {
                if (Common.isConnectedToInternet(this))
                loadFoodList(categoryId);
                else {
                    Toast.makeText(this, "Please check your internet Connection", Toast.LENGTH_SHORT).show();
                }
            }

    }

    private void loadFoodList(String categoryId) {
        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId)//select *from foods where menuid=categoryid
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
             viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);
               final Food local=model;
               viewHolder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void OnClick(View view, int position, boolean isLongClick) {
                       Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
                       foodDetail.putExtra("FoodId", adapter.getRef(position).getKey());
                       startActivity(foodDetail);

                   }
               });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
