package com.example.codered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;import android.app.ActionBar;

import com.google.android.material.textfield.TextInputLayout;

public class admin_login extends AppCompatActivity {
    Button login;
    TextInputLayout email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        email= (TextInputLayout) findViewById(R.id.mail);
        pass=(TextInputLayout) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un=email.getEditText().getText().toString().trim();
                String pa=pass.getEditText().getText().toString().trim();
                if(un.equals("CodeRed") && pa.equals("Codered@06"))
                {

                    Intent intent=new Intent(getApplicationContext(),total_payment.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        if(id==android.R.id.home)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}