package com.example.ecommerce1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce1.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText nameEdittext,phoneEdittext,addressEdittext,cityEdittext;
    private Button confirmOrderBtn;
    private TextView totalamountdisplay;
    private String totalAmount="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price ="+totalAmount, Toast.LENGTH_SHORT).show();

        confirmOrderBtn=(Button)findViewById(R.id.confirm_final_order_btn);

        nameEdittext=(EditText)findViewById(R.id.shippment_name);
        addressEdittext=(EditText)findViewById(R.id.shippment_address);
        phoneEdittext=(EditText)findViewById(R.id.shippment_phone_number);
        cityEdittext=(EditText)findViewById(R.id.shippment_city);
        totalamountdisplay=(TextView)findViewById(R.id.total_price_final_order_display);
        totalamountdisplay.setText("Total Price = "+String.valueOf(totalAmount)+" inr");

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check();
            }
        });

    }

    private void Check() {
        if(TextUtils.isEmpty(nameEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide Your Full name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide Your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide Your Full address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEdittext.getText().toString())){
            Toast.makeText(this, "Please Provide Your city ", Toast.LENGTH_SHORT).show();
        }
        else{
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String savecurrenttime,savecurrentdate;
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd,yyyy");
        savecurrentdate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currentDate.format(calForDate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object>orderMap=new HashMap<>();

        orderMap.put("TotalAmount",totalAmount);
        orderMap.put("name",nameEdittext.getText().toString());
        orderMap.put("phone",phoneEdittext.getText().toString());
        orderMap.put("date",savecurrentdate);
        orderMap.put("time",savecurrenttime);
        orderMap.put("address",addressEdittext.getText().toString());
        orderMap.put("city",cityEdittext.getText().toString());
        orderMap.put("status","not Shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your Final order is placed", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ConfirmFinalOrderActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}