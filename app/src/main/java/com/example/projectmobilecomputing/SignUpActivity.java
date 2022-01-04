package com.example.projectmobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        EcommerceDB dbHelper=new EcommerceDB(this);
        Button regSign=(Button) findViewById(R.id.buttonSign);
        EditText cname=(EditText) findViewById(R.id.customerName);
        EditText cUsername=(EditText) findViewById(R.id.usernameCust);
        EditText cgender=(EditText)findViewById(R.id.cgender);
        EditText cjob=(EditText) findViewById(R.id.jobCust);
        EditText cpasswrod=(EditText) findViewById(R.id.passwrodCust);
        EditText forgetk=(EditText)findViewById(R.id.forgetkey);
        DatePicker datePicker=(DatePicker)findViewById(R.id.calendarView);


        regSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cUsername.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "please Enter User Name", Toast.LENGTH_SHORT).show();
                    cUsername.requestFocus();
                    return;
                }
                if(cname.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "please Enter your Name", Toast.LENGTH_SHORT).show();
                    cname.requestFocus();
                    return;
                }
                if(cpasswrod.getText().length()<8){
                    Toast.makeText(getApplicationContext(), "please Enter More string password which >= 8", Toast.LENGTH_SHORT).show();
                    cpasswrod.requestFocus();
                    return;
                }
                int day=datePicker.getDayOfMonth();
                int month=datePicker.getMonth();
                int year=datePicker.getYear();
                String cBirthDate=String.valueOf(day)+" / "+String.valueOf(month)+" / "+String.valueOf(year);

                dbHelper.Add_Customer(cname.getText().toString(),cUsername.getText().toString(),cpasswrod.getText().toString()
                   ,cgender.getText().toString(),cBirthDate,cjob.getText().toString(),forgetk.getText().toString());
                Toast.makeText(getApplicationContext(),cname.getText().toString()+" Registered Succesfully!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();

            }
        });
    }
}