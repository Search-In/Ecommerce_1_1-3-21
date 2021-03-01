package com.example.ecommerce1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce1.Model.Users;
import com.example.ecommerce1.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import io.paperdb.Paper;

public class LOGINActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingbar;
    String parentDbName="Users";
    private CheckBox chkBoxRememberMe;
    private android.widget.TextView AdminLink,NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_l_o_g_i_n);

        LoginButton=(Button) findViewById(R.id.login_btn);
        InputPhoneNumber=(EditText) findViewById(R.id.login_phone_number_input);
        InputPassword=(EditText) findViewById(R.id.login_password_input);
        loadingbar=new ProgressDialog(this );

        AdminLink=(android.widget.TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink=(android.widget.TextView)findViewById(R.id.not_admin_panel_link);

        chkBoxRememberMe =(CheckBox) findViewById(R.id.remember_me_chkb);
        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("LOGIN ADMIN");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton.setText("LOGIN");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });
    }

    private void loginUser() {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Please write your Phone Number...",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please write your Password...",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("Login  Account");
            loadingbar.setMessage("Please wait while we are checking");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(String phone, String password) {

        if(chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists()){
                    Users usersData=snapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone)){
                         if(usersData.getPassword().equals(password)){

                             if(parentDbName.equals("Admins")){

                                 Toast.makeText(LOGINActivity.this,"Account Logged IN Admin",Toast.LENGTH_SHORT).show();
                                 loadingbar.dismiss();
                                 Intent intend=new Intent(LOGINActivity.this,AdminCategoryActivity.class);
                                 startActivity(intend);
                             }
                             else if(parentDbName.equals("Users")){
                                 Toast.makeText(LOGINActivity.this,"Account Logged IN",Toast.LENGTH_SHORT).show();
                                 loadingbar.dismiss();
                                 Intent intend=new Intent(LOGINActivity.this,HomeActivity.class);
                                 Prevalent.currentOnlineUser=usersData;
                                 startActivity(intend);
                             }

                         }
                         else {
                             Toast.makeText(LOGINActivity.this,"Password incorrect",Toast.LENGTH_SHORT).show();
                             loadingbar.dismiss();
                         }
                    }
                }
                else {
                    Toast.makeText(LOGINActivity.this,"Account with this"+phone+"number do not exist",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(LOGINActivity.this,"Create new account ",Toast.LENGTH_SHORT).show();

                    Intent intend=new Intent(LOGINActivity.this,RegisterActivity.class);
                    startActivity(intend);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}