package com.example.ecommerce1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce1.Model.Cart;
import com.example.ecommerce1.Prevalent.Prevalent;
import com.example.ecommerce1.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextProcessButton;
    private TextView txtTotalAmount,txtMsg1;
    private  float overalltotalprice=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextProcessButton=(Button)findViewById(R.id.next_process_button);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);
        txtMsg1=(TextView)findViewById(R.id.msg1);

        NextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtTotalAmount.setText("Total Price ="+String.valueOf(overalltotalprice)+"inr");
                Intent intent=new Intent(CartActivity.this,ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price",String.valueOf(overalltotalprice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart>options=
                new FirebaseRecyclerOptions.Builder<Cart>().setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone()).child("Products"),Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Quantity = "+model.getQuantity());
                holder.txtProductPrice.setText("Price = "+model.getPrice()+" inr");
                holder.txtProductName.setText(model.getPname());

                float oneTypeProductTprice=((Float.valueOf(model.getPrice())))*(Float.valueOf(model.getQuantity()));

                overalltotalprice=overalltotalprice+oneTypeProductTprice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]= new CharSequence[]{
                            "Edit",
                            "Remove"
                        };
                        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    if(which==0){
                                        Intent intent=new Intent(CartActivity.this,ProductDetailsActivity.class);
                                        intent.putExtra("pid",model.getPid());
                                        startActivity(intent);
                                    }
                                    if (which==1){
                                        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                        .child(model.getPid())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(CartActivity.this,HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                        cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products")
                                                .child(model.getPid())
                                                .removeValue();
                                    }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder=new CartViewHolder(view);
                return  holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void CheckOrderState(){
        DatabaseReference orderRef;
        orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String ShippingState=snapshot.child("status").getValue().toString();
                    String UserName=snapshot.child("name").getValue().toString();
                    if(ShippingState.equals("shipped")){
                        txtTotalAmount.setTextSize(15);
                        txtTotalAmount.setText("Dear, "+UserName+"\norder is shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congo your final order has been Shipped .Receive order at door step");
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can order when first order is shipped", Toast.LENGTH_SHORT).show();

                    }
                    else if(ShippingState.equals("not Shipped")){
                        txtTotalAmount.setTextSize(15);
                        txtTotalAmount.setText("Dear, "+UserName+"\norder is not shipped");
                        recyclerView.setVisibility(View.GONE);
                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congo your final order will be Shipped as soon as possible .Receive order at door step");
                        NextProcessButton.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "You can order when first order is shipped", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}