package com.example.wahdatkashmiri.unknown;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.wahdatkashmiri.unknown.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UnFoodDetail extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_food_detail);

        database=FirebaseDatabase.getInstance();
        foods= database.getReference("Foods");

        numberButton= findViewById(R.id.number_button);
        btnCart=findViewById(R.id.btn_cart);

        food_description=findViewById(R.id.food_description);
        food_name=findViewById(R.id.food_name);
        food_price=findViewById(R.id.food_price);
        food_image= findViewById(R.id.img_food);

        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        //get food id from intent

        if (getIntent() !=null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty())
        {
            getDetailFood(foodId);
        }


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UnFoodDetail.this, "You need to Login First", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UnFoodDetail.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food food=dataSnapshot.getValue(Food.class);
                //set image
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(food.getName());

                food_name.setText(food.getName());
                food_price.setText(food.getPrice());
                food_description.setText(food.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

