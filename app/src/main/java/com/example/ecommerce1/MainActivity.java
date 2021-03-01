package com.example.ecommerce1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ecommerce1.Model.Users;
import com.example.ecommerce1.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton,loginButton;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton=(Button)findViewById(R.id.main_join_now_btn);
        loginButton=(Button)findViewById(R.id.main_login_btn);
        loadingbar=new ProgressDialog(this );

        Paper.init(this);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(MainActivity.this,LOGINActivity.class);
                startActivity(intend);
            }
        });

        joinNowButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intend);
            }
        }));

        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!= null && UserPasswordKey!=null){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);

                loadingbar.setTitle("Already Logged in");
                loadingbar.setMessage("Please wait while we are checking");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

            }
        }
    }

    private void AllowAccess( final String phone,final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phone).exists()){
                    Users usersData=snapshot.child("Users").child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this,"Account Logged IN",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            Intent intend=new Intent(MainActivity.this,HomeActivity.class);
                            Prevalent.currentOnlineUser=usersData;
                            startActivity(intend);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Password incorrect",Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Account with this"+phone+"number do not exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(MainActivity.this,"Create new account ",Toast.LENGTH_SHORT).show();

                    Intent intend=new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intend);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}