package com.example.projectmobilecomputing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;


public class HomeActivity extends AppCompatActivity {
    EcommerceDB dbHelper;
    ArrayList<Product>products;
    ArrayList<String> intentproducts,productName,price,quantity,categorie;
    ListView prodlist;
    ListView catList;
    Button submit;
    Button clear;
    EditText proSearch;
    Button recordprod;
    Intent makeOrderIntent;
    int priceIntent;
    String CustomerUName;
    ImageButton camera;
    private static final int Request_Camera=1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:{
                if(resultCode==RESULT_OK && data!=null){
                    ArrayList<String> arrayList= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    proSearch.setText(arrayList.get(0));
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        dbHelper=MainActivity.dbHelper;
        prodlist=(ListView) findViewById(R.id.listproducts);
        catList=(ListView)findViewById(R.id.listcategory);
        priceIntent=0;
        Intent custIntent=getIntent();
        CustomerUName=custIntent.getStringExtra("Customername");
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ArrayAdapter<String> catadapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        prodlist.setAdapter(arrayAdapter);
        catList.setAdapter(catadapter);
        camera=(ImageButton)findViewById(R.id.imageButton);
        makeOrderIntent=new Intent(getApplicationContext(),MakeOrder.class);
        intentproducts=new ArrayList<>();
        products=new ArrayList<>();
        submit=(Button)findViewById(R.id.buttonsubmit);
        clear=(Button)findViewById(R.id.buttonclear);
        proSearch=(EditText)findViewById(R.id.productSearchNameIN);
        recordprod=(Button)findViewById(R.id.buttonRecordProduct);
        productName=new ArrayList<>();
        price=new ArrayList<>();
        quantity=new ArrayList<>();
        categorie=new ArrayList<>();
        Cursor c=dbHelper.readAllCatgs();
        if (c.getCount()==0){
            Toast.makeText(getApplicationContext(),"there is no Categories",Toast.LENGTH_SHORT).show();
        }else {
            do {
                catadapter.add(c.getString(1));
            }while (c.moveToNext());
        }
       Cursor cursor=dbHelper.readAllProducts();
        if(cursor.getCount()==0){
            Toast.makeText(getApplicationContext(), "There is no products", Toast.LENGTH_SHORT).show();
        }
        else {
            do {
                productName.add(cursor.getString(1));
                price.add(cursor.getString(2));
                quantity.add(cursor.getString(3));
                String cat=dbHelper.getCat(cursor.getInt(4));
                categorie.add(cat);
                arrayAdapter.add(cursor.getString(1)+"  "+cursor.getString(2));

            } while (cursor.moveToNext());
        }
        proSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                arrayAdapter.clear();
                String n=proSearch.getText().toString();
                if(!n.equals("")){
                    productName=new ArrayList<>();
                    price=new ArrayList<>();
                    quantity=new ArrayList<>();
                    categorie=new ArrayList<>();
                    arrayAdapter.clear();
                    Cursor c=dbHelper.readProds(n);
                    if(c.getCount()==0){
                        Toast.makeText(getApplicationContext(), "There is no products", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        do{
                            productName.add(c.getString(1));
                            price.add(c.getString(2));
                            quantity.add(c.getString(3));
                            String cat=dbHelper.getCat(c.getInt(4));
                            categorie.add(cat);
                            arrayAdapter.add(c.getString(1));
                        }  while (c.moveToNext());

                    }
                }

            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent cameraIntent=new Intent(getApplicationContext(),QrCodeActivity.class);
                startActivity(cameraIntent);
                cameraIntent=getIntent();
                proSearch.setText(cameraIntent.getStringExtra("QRText"));

            }
        });
        recordprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                startActivityForResult(intent,1000);
                proSearch.requestFocus();
            }
        });

        prodlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p=((TextView)view).getText().toString();
                String[] p1=p.split(" ");
                if(p1.length>3){
                    p=p1[0]+" "+p1[1];
                }
                else {
                    p=p1[0];
                }

                Cursor c1=dbHelper.readProds(p);
                Product pr=new Product();
                pr.product_Price=c1.getInt(2);
                pr.product_ID=c1.getInt(0);
                pr.product_quantity=1;
                pr.product_Name=p;
                pr.categorie=dbHelper.getCat(c1.getInt(4));
                products.add(pr);
                Toast.makeText(getApplicationContext(),p+" Added to the Order",Toast.LENGTH_SHORT).show();
                intentproducts.add(p);
                priceIntent+=pr.product_Price;
            }
        });
        catList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrayAdapter.clear();
                String ca=((TextView)view).getText().toString();
                int i=dbHelper.getCatID(ca);
                Cursor ncursor=dbHelper.readCatProds(i);

                if(ncursor.getCount()==0){
                    Toast.makeText(getApplicationContext(), "There is no products in this Categorie", Toast.LENGTH_SHORT).show();
                }
                else {
                    do {
                        arrayAdapter.add(ncursor.getString(1)+"  "+ncursor.getString(2));

                    } while (ncursor.moveToNext());
                }
              return false;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeOrderIntent.putExtra("products",intentproducts);
                makeOrderIntent.putExtra("price",priceIntent);
                makeOrderIntent.putExtra("Customername",CustomerUName);
                startActivity(makeOrderIntent);

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            products.clear();
            }
        });

    }
    private boolean checkPermision(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED);
    }
    private void request_Permission(){
        ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.CAMERA},Request_Camera);
    }




}