package com.example.projectmobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RestoreActivity extends AppCompatActivity {
    EditText username;
    EditText forgetKey;
    EditText newpass;
    Button check;
    Button reset;
    EcommerceDB dbHelper;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        username=(EditText) findViewById(R.id.usernameCheck);
        dbHelper=MainActivity.dbHelper;
        forgetKey=(EditText) findViewById(R.id.forgetkeyCheck);
        newpass=(EditText) findViewById(R.id.newpassword);
        newpass.setEnabled(false);
        check=(Button) findViewById(R.id.buttoncheck);
        reset=(Button) findViewById(R.id.resetPassword);
        reset.setEnabled(false);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor=dbHelper.getForgetKey(username.getText().toString());
                String k=cursor.getString(7);
                if(k.equals(forgetKey.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Welcome back"+username.getText().toString(),Toast.LENGTH_LONG).show();
                    reset.setEnabled(true);
                    newpass.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Now Reset Your Password!", Toast.LENGTH_SHORT).show();
                }
                else if(k.equals("")){
                    Toast.makeText(getApplicationContext(),"Wrong User Name !",Toast.LENGTH_LONG).show();
                    username.requestFocus();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Wrong Password !",Toast.LENGTH_LONG).show();
                    forgetKey.requestFocus();
                }
                
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpass.getText().toString().length()<8){
                    Toast.makeText(getApplicationContext(),"Enter 8 letters or more!",Toast.LENGTH_SHORT).show();
                }
                else {
                    ContentValues row=new ContentValues();
                    row.put("CutName",cursor.getString(1));
                    row.put("Username",cursor.getString(2));
                    row.put("Password",newpass.getText().toString());
                    row.put("Gender",cursor.getString(4));
                    row.put("birthdate",cursor.getString(5));
                    row.put("job",cursor.getString(6));
                    row.put("ForgetKey",cursor.getString(7));
                    dbHelper.updateCustomer(row,cursor.getInt(0));
                    Toast.makeText(getApplicationContext(), "Password Reset Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }
        });
    }
}