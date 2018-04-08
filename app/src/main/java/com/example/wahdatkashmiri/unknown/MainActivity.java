package com.example.wahdatkashmiri.unknown;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wahdatkashmiri.unknown.Common.Common;
import com.example.wahdatkashmiri.unknown.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {


    Button btnSignIn,btnRegister;
    TextView skip,ForgotPassword;

    RelativeLayout rootLayout;

    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase db;
    DatabaseReference users;



    //ctrl+O this is for the font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //adding calligraphy font to the app
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);

        //initialize firebase authentication
        auth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        users=db.getReference("Users");


        btnRegister=(Button) findViewById(R.id.btnRegister);
        btnSignIn=(Button) findViewById(R.id.btnSignIn);
        rootLayout=(RelativeLayout) findViewById(R.id.rootLayout);
        ForgotPassword=findViewById(R.id.txt_rider_app);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ResetPassword.class);
                startActivity(intent);
            }
        });
        skip = findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,Unregister.class);
                startActivity(intent);
            }
        });


      //  mAuthStateListener=new FirebaseAuth.AuthStateListener() {
      //      @Override
      //      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
       //         if (firebaseAuth.getCurrentUser() !=null){
       //             startActivity(new Intent(MainActivity.this,Welcome.class));
        //        }
        //    }
       // };

        //login
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });


        //registration
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistrationDialog();//to show the pop up dialog
            }
        });
    }

  //  @Override
   // protected void onStart() {
   //     super.onStart();
     //   auth.addAuthStateListener(mAuthStateListener);
     //   User user=new User();

       // Common.currentUser =user;

  //  }

    private void showLoginDialog()
    {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater= LayoutInflater.from(this);
        View login_layout=inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText edtEmail=login_layout.findViewById(R.id.edtEmailLogin);

        final MaterialEditText edtPassword=login_layout.findViewById(R.id.edtPasswordLogin);


        dialog.setView(login_layout);

        //this set the buttons below the pop up dialog

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                // disable the sign in button when the request is processing
                btnSignIn.setEnabled(false);

                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Email address is required", Snackbar.LENGTH_SHORT).show();
                    btnSignIn.setEnabled(true);
                    return;
                }

                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Password is required", Snackbar.LENGTH_SHORT).show();
                    btnSignIn.setEnabled(true);
                    return;
                }
                if (edtPassword.getText().toString().length() < 6) {
                    Snackbar.make(rootLayout, "Password is too short!", Snackbar.LENGTH_SHORT).show();
                    btnSignIn.setEnabled(true);
                    return;
                }

                final SpotsDialog waitingDialog=new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                //Login


                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user=new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                Common.currentUser =user;
                                waitingDialog.dismiss(); //stopping the progress bar after the log in is successful

                                startActivity(new Intent(MainActivity.this,Welcome.class));

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss(); //stopping the progress bar after the log in fail
                        Snackbar.make(rootLayout, "Failed", Snackbar.LENGTH_SHORT).show();

                        //reactivating the sign in button if the sign in failed
                        btnSignIn.setEnabled(true);

                    }
                });
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void showRegistrationDialog()
    {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);

        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater= LayoutInflater.from(this);
        View register_layout=inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail=register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtName=register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone=register_layout.findViewById(R.id.edtPhone);
        final MaterialEditText edtPassword=register_layout.findViewById(R.id.edtPassword);



        dialog.setView(register_layout);

        //this set the buttons below the pop up dialog

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Save user to database

                                User user=new User();

                                 user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                Common.currentUser =user;
                                //USE firebase user ID as primary key for the table
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Registered successfully",Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Snackbar.make(rootLayout,"Registration failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(rootLayout,"Registration failed!!!"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });
            }
        });




        //this set the cancel button of the dialog
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}

