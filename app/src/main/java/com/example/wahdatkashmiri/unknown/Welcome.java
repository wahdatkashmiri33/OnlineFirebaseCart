package com.example.wahdatkashmiri.unknown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Interface.ItemClickListener;
import com.example.wahdatkashmiri.unknown.Model.Category;
import com.example.wahdatkashmiri.unknown.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Welcome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RelativeLayout rootlayout;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference category;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtFullname;
    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        rootlayout=findViewById(R.id.rootLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);


        //init firebase
        database = FirebaseDatabase.getInstance();
        category= database.getReference("Category");
        auth=FirebaseAuth.getInstance();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent cartIntent =new Intent(Welcome.this,Cart.class);
               startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullname= headerView.findViewById(R.id.txtFullName);
//       txtFullname.setText(Common.currentUser.getName());


        //Load menu
      recyclerView= findViewById(R.id.recycler_menu);
      recyclerView.setHasFixedSize(true);
      layoutManager = new LinearLayoutManager(this);
      recyclerView.setLayoutManager(layoutManager);
      if (Common.isConnectedToInternet(this))
          loadmenu();
      else {
          Toast.makeText(this, "Please check your internet Connection", Toast.LENGTH_SHORT).show();
      }


    }

    private void loadmenu() {

        adapter=new FirebaseRecyclerAdapter<Category, MenuViewHolder>(
                Category.class,
                R.layout.menu_item,
                MenuViewHolder.class,
                category
        ) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
            viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int position, boolean isLongClick) {
                        Intent foodList=new Intent(Welcome.this,FoodList.class);
                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        } else if (id == R.id.nav_cart) {

            Intent Cartintent = new Intent(Welcome.this,Cart.class);
             startActivity(Cartintent);
        } else if (id == R.id.nav_orders) {

            Intent Ordersintent = new Intent(Welcome.this,OrderStatus.class);
            startActivity(Ordersintent);
        } else if (id == R.id.nav_sign_Out) {
            auth.signOut();
            Intent intent=new Intent(Welcome.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "You have Been Successfully Signed Out", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
