package com.example.wahdatkashmiri.unknown;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Database.Database;
import com.example.wahdatkashmiri.unknown.Model.Food;
import com.example.wahdatkashmiri.unknown.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
     CoordinatorLayout coordinatorLayout;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;
    Food currentfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        coordinatorLayout=findViewById(R.id.rootLayout);
        database=FirebaseDatabase.getInstance();
        foods= database.getReference("Foods");

        numberButton= findViewById(R.id.number_button);
        btnCart=findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             new Database(getBaseContext()).addToCart(new Order(

                     foodId,
                     currentfood.getName(),
                     numberButton.getNumber(),
                     currentfood.getPrice(),
                     currentfood.getDiscount()
             ));
                Toast.makeText(FoodDetail.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

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
            if (Common.isConnectedToInternet(this))
            getDetailFood(foodId);
            else {
                Toast.makeText(this, "Please check your internet Connection", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentfood=dataSnapshot.getValue(Food.class);

                food_name.setText(currentfood.getName());
                //set image
                Picasso.with(getBaseContext()).load(currentfood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentfood.getName());

                food_price.setText(currentfood.getPrice());
                food_description.setText(currentfood.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
