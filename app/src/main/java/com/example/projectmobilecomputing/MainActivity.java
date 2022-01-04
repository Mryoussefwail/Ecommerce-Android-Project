package com.example.projectmobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static EcommerceDB dbHelper;
    EditText username;
    EditText password;
    Button login;
    TextView forgetPass;
    CheckBox rememberMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new EcommerceDB(getApplicationContext());
        SharedPreferences sharedPreferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=sharedPreferences.getString("remember","");
        username=findViewById(R.id.usernameIN2);
        password=findViewById(R.id.passwordIN2);
        login=findViewById(R.id.loginButton2);
        rememberMe=(CheckBox)findViewById(R.id.checkBoxRemember);
        Button sign=(Button)findViewById(R.id.buttonSignup);
        forgetPass=(TextView)findViewById(R.id.forgetPassword1);

        if(checkbox.equals("true")){
            Intent newintent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(newintent);
        }
        else if(checkbox.equals("false")){
            Toast.makeText(getApplicationContext(), "Please Sign IN !", Toast.LENGTH_SHORT).show();
        }

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),RestoreActivity.class);
                startActivity(in);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),username.getText().toString(),Toast.LENGTH_SHORT);
                if (username.getText().toString().equals("admin") || password.getText().toString().equals("admin")) {
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                }
                else if (password.getText().length()<8){
                    Toast.makeText(getApplicationContext(),"unValid Password Please Enter 8 or more !",Toast.LENGTH_LONG).show();
                }
                else{

                    String logAcessPass=dbHelper.login(username.getText().toString());
                    if(logAcessPass.equals(password.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Welcome "+username.getText().toString(),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                        intent.putExtra("Customername",username.getText().toString());
                        startActivity(intent);
                    }
                    else if(logAcessPass.equals("")){
                        Toast.makeText(getApplicationContext(),"Wrong User Name !",Toast.LENGTH_LONG).show();
                        username.requestFocus();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Wrong Password !",Toast.LENGTH_LONG).show();
                        password.requestFocus();
                    }
                }
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"checked",Toast.LENGTH_LONG).show();
                }
                else if(!buttonView.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Unchecked",Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}