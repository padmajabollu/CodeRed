package com.example.codered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.codered.R;
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

public class total_payment extends AppCompatActivity {

    ListView listView;

    ArrayList<String> payment=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(getApplicationContext(),"Data is Loading.....",Toast.LENGTH_SHORT).show();

        Call<List<paymentData>> call=ApiClient.getInstance().getApi().getPayment("getPayment");
        call.enqueue(new Callback<List<paymentData>>() {
            @Override
            public void onResponse(Call<List<paymentData>> call, Response<List<paymentData>> response) {
                List<paymentData> list=new ArrayList<>();
                list=response.body();
                if(list.size()>0)
                {
                    for (int i=0;i<list.size();i++)
                    {
                        payment.add("["+list.get(i).getDate()+"]        "+list.get(i).getDistributor()+" : "+list.get(i).getTotal());


                    }
                    listView=(ListView) findViewById(R.id.listview);
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                            getBaseContext(),
                            android.R.layout.simple_list_item_1,
                            payment
                    );

                    listView.setAdapter(arrayAdapter);

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_SHORT);

                }
            }

            @Override
            public void onFailure(Call<List<paymentData>> call, Throwable t) {

            }
        });


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
        else if (id==R.id.logout)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==android.R.id.home)
        {
            //finish();

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Logout ?")
                    .setTitle("Logout")
                    .setIcon(R.drawable.codered)
                    .setCancelable(false)
                    .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Logout ?")
        .setTitle("Logout")
        .setIcon(R.drawable.codered)
        .setCancelable(false)
        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}