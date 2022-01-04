package com.example.projectmobilecomputing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MakeOrder extends AppCompatActivity {
    Button getadd;
    ArrayList<String> products;
    EcommerceDB dbHelper;
    Button subm;
    String addr;
    FusedLocationProviderClient fusedLocationProviderClient;
    int totalPrice=0;
    ArrayList<Integer> quantities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        Intent intent = getIntent();
        dbHelper = MainActivity.dbHelper;
        subm = (Button) findViewById(R.id.buttonsubmitOrder);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        products = intent.getStringArrayListExtra("products");
        quantities=new ArrayList<Integer>();
        for (int i=0;i<products.size();i++){
            quantities.add(1);
        }
        getadd = (Button) findViewById(R.id.buttongetAddress);
        TextView date = (TextView) findViewById(R.id.dateidText);
        TextView custn = (TextView) findViewById(R.id.custnameTextbox);

        TextView totPrice = (TextView) findViewById(R.id.totalpriceText);
        ListView listView = (ListView) findViewById(R.id.productslist);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        for (int i = 0; i < products.size(); i++) {
            arrayAdapter.add(products.get(i));
        }
        totalPrice=intent.getIntExtra("price", 1);
        totPrice.setText(String.valueOf(intent.getIntExtra("price", 1)));
        custn.setText(intent.getStringExtra("Customername"));

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date1 = df.format(Calendar.getInstance().getTime());
        date.setText(date1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                quantities.set(position, quantities.get(position)+1);
                Toast.makeText(getApplicationContext(), ((TextView)view).getText().toString()+" 'quantity is now :"+quantities.get(position), Toast.LENGTH_SHORT).show();
                int p=dbHelper.getPrice(((TextView)view).getText().toString());
                totalPrice+=p;
                totPrice.setText(String.valueOf(totalPrice));
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String remo=((TextView)view).getText().toString();
                int p=dbHelper.getPrice(remo);
                if(quantities.get(position)>1){
                    quantities.set(position,quantities.get(position)-1);

                }
                else {
                    arrayAdapter.remove(remo);
                    products.remove(remo);
                }
                totalPrice-=p;
                totPrice.setText(String.valueOf(totalPrice));
                return false;
            }
        });
        getadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MakeOrder.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(MakeOrder.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
                Toast.makeText(getApplicationContext(), "Address: "+addr, Toast.LENGTH_LONG).show();
            }

        });


        subm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(products.size()<1){
                    Toast.makeText(getApplicationContext(), "Empty Cart!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int id = dbHelper.getCustID(custn.getText().toString());
                int oid = dbHelper.add_Order(date1, id, addr);
                for (int i = 0; i < products.size(); i++) {
                    int d = dbHelper.getproductId(products.get(i));
                    dbHelper.add_Order_Deatils(oid, d, 1);
                }
                Toast.makeText(getApplicationContext(), "Order Submitted !", Toast.LENGTH_SHORT).show();
                Intent home=new Intent(getApplicationContext(),HomeActivity.class);
                home.putExtra("Customername",custn.getText().toString());
                startActivity(home);
                finish();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(MakeOrder.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        addr = addressList.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}