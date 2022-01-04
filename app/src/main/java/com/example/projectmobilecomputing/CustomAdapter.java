package com.example.projectmobilecomputing;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    Activity activity;
    ArrayList productName,price,quantity,categorie;
    private RecycleViewClickInterface recycleViewClickInterface;
    CustomAdapter(Context contex, ArrayList<String> productName, ArrayList<String> price, ArrayList<String> quantity, ArrayList<String> categorie,RecycleViewClickInterface anInterface){
        this.context=contex;
        this.productName=productName;
        this.price=price;
        this.quantity=quantity;
        this.categorie=categorie;
        this.recycleViewClickInterface=anInterface;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productName.setText(String.valueOf(productName.get(position)));
        holder.quantity.setText(String.valueOf(quantity.get(position)));
        holder.price.setText(String.valueOf(price.get(position)));
        holder.categorie.setText(String.valueOf(categorie.get(position)));
        holder.mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName,price,quantity,categorie;
        LinearLayout mainlayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.prodName);
            price=itemView.findViewById(R.id.price);
            quantity=itemView.findViewById(R.id.prodquantity);
            categorie=itemView.findViewById(R.id.categoriename);
            mainlayout=itemView.findViewById(R.id.mainLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

        }
    }
}

