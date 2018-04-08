package com.example.wahdatkashmiri.unknown;

import android.content.DialogInterface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Database.Database;
import com.example.wahdatkashmiri.unknown.Model.Food;
import com.example.wahdatkashmiri.unknown.Model.Order;
import com.example.wahdatkashmiri.unknown.Model.Request;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name,food_price,food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button BtnPlaceorder;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
     CoordinatorLayout coordinatorLayout;

    String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods,requests;
    Food currentfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        BtnPlaceorder=findViewById(R.id.btnPlaceorder);
        coordinatorLayout=findViewById(R.id.rootLayout);
        database=FirebaseDatabase.getInstance();
        foods= database.getReference("Foods");
        requests=database.getReference("Requests");

        numberButton= findViewById(R.id.number_button);
        BtnPlaceorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
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

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodDetail.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter your Full Address:  ");

        LayoutInflater inflater= LayoutInflater.from(this);
        View request_layout=inflater.inflate(R.layout.requestsfield,null);

        final MaterialEditText edtName=request_layout.findViewById(R.id.edtname);
        // final  MaterialEditText edtEmail=request_layout.findViewById(R.id.edtemail);
        final MaterialEditText edtcontact=request_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtAddress=request_layout.findViewById(R.id.edtaddress);

        alertDialog.setView(request_layout);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                //User user =new User();
                // Common.currentUser=user;
                Request request=new Request(


                        Common.currentUser.getEmail(),
                         edtName.getText().toString(),
                        edtAddress.getText().toString(),
                        edtcontact.getText().toString(),
                        food_name.getText().toString(),
                        food_price.getText().toString(),
                        numberButton.getNumber()

                );

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //delete cart
                new Database(getBaseContext()).cleanCart();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        FoodDetail.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("Order Placed");

                // Setting Dialog Message
                alertDialog.setMessage("Your Order Will Be Delivered Shortly");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_check_box_black_24dp);

                // Setting OK Button
                alertDialog.setButton(alertDialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
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
