package com.example.ecommerce1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce1.Model.AdminOrders;
import com.example.ecommerce1.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList=findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class).build();


        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder>adapter=
              new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                  @Override
                  protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, int position, @NonNull AdminOrders model) {
                      holder.userName.setText("Name: "+model.getName());
                      holder.userPhoneNumber.setText("Phone: "+model.getPhone());
                      holder.userShippingAddress.setText("Shipping address: "+model.getAddress()+" "+model.getCity());
                      holder.userDateTime.setText("Order at: "+model.getDate() +" "+model.getTime());
                      holder.userTotalPrice.setText("Total Amount: "+model.getTotalAmount());

                      holder.showOrderButton.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              String uId= Prevalent.currentOnlineUser.getPhone().toString();
                              Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                              intent.putExtra("uid",uId);
                              startActivity(intent);
                          }
                      });
                      holder.itemView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              CharSequence options[]=new CharSequence[]{
                                  "Yes",
                                  "No"
                              };
                              AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);
                              builder.setTitle("shipping done");
                              builder.setItems(options, new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                        if(which==0){
                                            String uId=getRef(position).getKey();
                                            RemoveOrder(uId);
                                        }
                                        else{
                                            finish();
                                        }
                                  }
                              });
                              builder.show();
                          }
                      });
                  }

                  @NonNull
                  @Override
                  public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                      return new AdminOrdersViewHolder(view);
                  }
              };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void RemoveOrder(String uId) {
        ordersRef.child(uId).removeValue();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrderButton;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=itemView.findViewById(R.id.order_User_Name);
            userPhoneNumber=itemView.findViewById(R.id.order_Phone_Number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address);
            showOrderButton=itemView.findViewById(R.id.show_all_products_btn);

        }
    }
}