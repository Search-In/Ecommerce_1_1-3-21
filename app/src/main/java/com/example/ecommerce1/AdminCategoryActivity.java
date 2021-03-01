package com.example.ecommerce1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView tShirts,sportsTShirts,femaleDresses,sweaters;
    private ImageView glasses,hatsCaps,walletsBags,shoes;
    private ImageView headphones,laptops,watches,MobilePhones;
    private Button LogoutBtn,CheckOrderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        LogoutBtn=(Button)findViewById(R.id.admin_logout_btn);
        CheckOrderBtn=(Button)findViewById(R.id.check_orders_btn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminCategoryActivity.this,AdminNewOrdersActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });



        tShirts=(ImageView) findViewById(R.id.t_shirts);
        sportsTShirts=(ImageView) findViewById(R.id.sports_t_shirts);
        femaleDresses=(ImageView) findViewById(R.id.female_dresses);
        sweaters=(ImageView) findViewById(R.id.sweathers);

        glasses=(ImageView) findViewById(R.id.glasses);
        hatsCaps=(ImageView) findViewById(R.id.hats_caps);
        walletsBags=(ImageView) findViewById(R.id.purses_bags_wallets);
        shoes=(ImageView) findViewById(R.id.shoess);

        headphones=(ImageView) findViewById(R.id.headphones_handfree);
        laptops=(ImageView) findViewById(R.id.laptops_pc);
        watches=(ImageView) findViewById(R.id.watches);
        MobilePhones=(ImageView) findViewById(R.id.mobilesphones);





        tShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","tShirts");
                startActivity(intend);
            }
        });
        sportsTShirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Sports TShirts");
                startActivity(intend);
            }
        });
        femaleDresses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Female Dresses");
                startActivity(intend);
            }
        });
        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Sweaters");
                startActivity(intend);
            }
        });
        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Glasses");
                startActivity(intend);
            }
        });
        hatsCaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Hats Caps");
                startActivity(intend);
            }
        });
        walletsBags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Wallets Bags");
                startActivity(intend);
            }
        });
        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Shoes");
                startActivity(intend);
            }
        });
        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Headphones");
                startActivity(intend);
            }
        });
        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Laptop Pc");
                startActivity(intend);
            }
        });
        watches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","Watches");
                startActivity(intend);
            }
        });
        MobilePhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intend=new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                intend.putExtra("category","MobilePhone");
                startActivity(intend);
            }
        });
    }
}