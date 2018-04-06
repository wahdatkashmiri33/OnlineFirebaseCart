package com.example.wahdatkashmiri.unknown;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Database.Database;
import com.example.wahdatkashmiri.unknown.Model.Order;
import com.example.wahdatkashmiri.unknown.Model.Request;
import com.example.wahdatkashmiri.unknown.Model.User;
import com.example.wahdatkashmiri.unknown.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalprice;
    Button btnPlace;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database=FirebaseDatabase.getInstance();
        requests= database.getReference("Requests");

        recyclerView=findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalprice=findViewById(R.id.total);
        btnPlace=findViewById(R.id.btnPlaceorder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cart.size()>0)
               showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your Cart Is Empty", Toast.LENGTH_SHORT).show();
            }
        });
        loadListFood();
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
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
                        edtAddress.getText().toString(),
                        edtcontact.getText().toString(),
                        txtTotalprice.getText().toString(),
                        cart
                );

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                //delete cart
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You Your Order has been Successfully Placed", Toast.LENGTH_SHORT).show();
                     finish();
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

    private void loadListFood() {
       cart=new Database(this).getCarts();
       adapter = new CartAdapter(cart,this);
       adapter.notifyDataSetChanged();
       recyclerView.setAdapter(adapter);

       //calculate total price
        int total = 0;
        for (Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale=new Locale("en","IN");
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);
        txtTotalprice.setText(fmt.format(total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //we will remove item at list Order by position
        cart.remove(position);
       // After that we will delete all item from sqlite
        new Database(this).cleanCart();
        //and final data will be updated
        for (Order item:cart)
            new Database(this).addToCart(item);
        //refrsh
        loadListFood();

    }
}
