package com.example.projectmobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    EditText priceprod;
    EditText nameProd;
    EditText quantityProd;
    EditText categorieProd;
    Button add;
    Button addCategorie;
    ArrayList<Product> products;
    EcommerceDB ecommerceDB ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        products=new ArrayList<>();
        addCategorie=(Button)findViewById(R.id.buttonAddCat);
        TextView categroienam=(TextView)findViewById(R.id.catNameAdminIn);
        ecommerceDB=MainActivity.dbHelper;
        categorieProd=(EditText)findViewById(R.id.procatIn);
        priceprod=(EditText) findViewById(R.id.proPriceIn);
        nameProd=(EditText) findViewById(R.id.proNameIN);
        quantityProd=(EditText)findViewById(R.id.proquantityIn);
        add=(Button) findViewById(R.id.buttonaddProduct);
        addCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=ecommerceDB.getCatID(categroienam.getText().toString());
                if(i==-1){
                    ecommerceDB.add_categorie(categroienam.getText().toString());
                    Toast.makeText(getApplicationContext(),"Categorie Added!",Toast.LENGTH_SHORT).show();
                    categroienam.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "Categorie Already Exists !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product p=new Product();
                p.product_Price=Integer.parseInt(priceprod.getText().toString());

                p.product_Name=nameProd.getText().toString();
                p.product_quantity=Integer.parseInt(quantityProd.getText().toString());
                p.product_ID=products.size()+1;
                p.categorie=categorieProd.getText().toString();
                products.add(p);

                int catid=ecommerceDB.getCatID(p.categorie);
                Toast.makeText(getApplicationContext(),String.valueOf(catid),Toast.LENGTH_SHORT).show();
                ecommerceDB.add_Product(p.product_Name,p.product_Price,p.product_quantity,catid);

                Toast.makeText(getApplicationContext(),"Product Added !",Toast.LENGTH_SHORT).show();
            }
        });

    }
}