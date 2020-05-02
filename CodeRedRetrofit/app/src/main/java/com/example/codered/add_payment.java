package com.example.codered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class add_payment extends AppCompatActivity {

    Button add_pay;
    TextInputLayout dis,cus,amt;
    String dis1,cus1,amt1;
    List<studentData> stud_names=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar bar=getSupportActionBar();
        bar.setTitle("Add Payment");
        dis=(TextInputLayout) findViewById(R.id.dis);
        cus=(TextInputLayout) findViewById(R.id.cust);
        amt=(TextInputLayout) findViewById(R.id.amount);
        add_pay=(Button) findViewById(R.id.add_pay);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Call<List<studentData>> call=ApiClient.getInstance().getApi().getStudent("getStud");
        call.enqueue(new Callback<List<studentData>>() {
            @Override
            public void onResponse(Call<List<studentData>> call, Response<List<studentData>> response) {
                stud_names=response.body();
            }

            @Override
            public void onFailure(Call<List<studentData>> call, Throwable t) {

            }
        });

        add_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dis1=dis.getEditText().getText().toString().trim();
                cus1=cus.getEditText().getText().toString().trim();
                amt1 = amt.getEditText().getText().toString().trim();
                if (validateCustomer() && validateDistributor() && validateAmt())
                {
                    String toast=dis1+"\n"+cus1+"\n"+amt1;
                    Toast.makeText(getApplicationContext(),toast, Toast.LENGTH_SHORT).show();

                    Call<List<status>> call=ApiClient.getInstance().getApi().addPayment("insertPay",dis1,cus1,amt1);
                    call.enqueue(new Callback<List<status>>() {
                        @Override
                        public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                            List<status> list=new ArrayList<>();
                            list=response.body();

                            Toast.makeText(getApplicationContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<List<status>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });
    }

    private boolean validateDistributor() {

        if(!dis1.isEmpty())
        {
            int c=0;
            String l_sender=dis1.toLowerCase();
            for (int i=0;i<dis1.length();i++)
            {
                if((int)l_sender.charAt(i)>=97 && (int)l_sender.charAt(i)<=122)
                {

                    c+=1;
                }
                else if((int)l_sender.charAt(i)==32)
                {

                    c+=1;
                }
            }

            if (dis1.length()>=4 && dis1.length()==c ) {
                boolean flag=false;
                for (int i=0;i<stud_names.size();i++)
                {

                    if(stud_names.get(i).getName().equalsIgnoreCase(dis1))
                    {
                        flag=true;
                        break;
                    }

                }
                if (flag)
                {
                    dis.setError(null);
                    return true;

                }
                else
                {

                    dis.setError("Distributer Not Added");
                    return false;
                }

            }
            else {
                dis.setError("Name is Invalid");
                return false;
            }

        }

        else
        {
            dis.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validateCustomer() {

        if(!cus1.isEmpty())
        {
            int c=0;
            String l_sender=cus1.toLowerCase();
            for (int i=0;i<cus1.length();i++)
            {
                if((int)l_sender.charAt(i)>=97 && (int)l_sender.charAt(i)<=122)
                {

                    c+=1;
                }
                else if((int)l_sender.charAt(i)==32)
                {

                    c+=1;
                }
            }

            if (cus1.length()>=4 && cus1.length()==c ) {
                cus.setError(null);
                return true;
            }
            else {
                cus.setError("Name is Invalid");
                return false;
            }

        }

        else
        {
            cus.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validateAmt()
    {
        if(!amt1.isEmpty())
        {
            int c=0;

            for (int i=0;i<amt1.length();i++) {
                if ((int) amt1.charAt(i) >= 48 && (int) amt1.charAt(i) <= 57) {

                    c += 1;
                }
            }
            if (c==amt1.length())
            {
                amt.setError(null);
                return true;
            }
            else
            {
                amt.setError("Amount Invalid");
                return false;
            }
        }
        else
        {
            amt.setError("Field Can't be Empty");
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        int id=item.getItemId();

        if(id==R.id.add)
        {
            Intent intent=new Intent(getApplicationContext(),add_student.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.delete)
        {
            Intent intent=new Intent(getApplicationContext(),deletestudent.class);
            startActivity(intent);
            finish();
        }
        else if (id==R.id.total)
        {
            Intent intent=new Intent(getApplicationContext(),total_payment.class);
            startActivity(intent);
            finish();
        }
        else if (id==R.id.payment)
        {
            Intent intent=new Intent(getApplicationContext(),add_payment.class);
            startActivity(intent);
            finish();
        }
        else if(id==android.R.id.home)
        {
            Intent intent=new Intent(getApplicationContext(),total_payment.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),total_payment.class);
        startActivity(intent);
        finish();

    }
}