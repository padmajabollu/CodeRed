package com.example.codered;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

public class deletestudent extends AppCompatActivity {

    boolean flag=false;
    ListView listView;
    List<studentData> list=new ArrayList<>();
    ArrayList<String> stud_id=new ArrayList<>();
    ArrayList<String> nm=new ArrayList<>();
    ArrayList<String> pn=new ArrayList<>();
    ArrayList<String> profile=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletestudent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toast.makeText(getApplicationContext(),"Data is Loading.....",Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Click on Item to Delete Entry",Toast.LENGTH_SHORT).show();

        ActionBar bar=getSupportActionBar();
        bar.setTitle("Delete Student");
        listView = (ListView) findViewById(R.id.listview);

        Call<List<studentData>> call=ApiClient.getInstance().getApi().getStudent("getStud");
        call.enqueue(new Callback<List<studentData>>() {
            @Override
            public void onResponse(Call<List<studentData>> call, Response<List<studentData>> response) {


                list=response.body();
                if(list.size()>0)
                {

                    for (int i=0;i<list.size();i++) {
                        stud_id.add(list.get(i).getStudent_id());

                        nm.add(list.get(i).getName());
                        pn.add(list.get(i).getPhno());
                        profile.add(list.get(i).getImage());
                        CustomAdapter1 customAdapter = new CustomAdapter1();
                        listView.setAdapter(customAdapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Call<List<status>> call1 = ApiClient.getInstance().getApi().deleteStudent("deleteStud", stud_id.get(position));
                                call1.enqueue(new Callback<List<status>>() {
                                    @Override
                                    public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                                        List<status> list = new ArrayList<>();
                                        list = response.body();
                                        //Toast.makeText(getApplicationContext(), list.get(0).getStatus(), Toast.LENGTH_LONG);

                                        Intent refresh = new Intent(getApplicationContext(), deletestudent.class);
                                        finish();

                                        startActivity(refresh);

                                    }

                                    @Override
                                    public void onFailure(Call<List<status>> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG);

                                    }
                                });
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No Data Found", Toast.LENGTH_SHORT);

                }
            }

            @Override
            public void onFailure(Call<List<studentData>> call, Throwable t) {

                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG);

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
        else if (id==R.id.logout)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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

    class CustomAdapter1 extends BaseAdapter {
        @Override
        public int getCount() {
            return nm.size();
        }

        @Override
        public Object getItem(int position) {
            return nm.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.listitem, null);
            TextView phno1 = (TextView) view.findViewById(R.id.phno);
            TextView name1 = (TextView) view.findViewById(R.id.name);
            ImageView profile1 = view.findViewById(R.id.img);

            try {
                byte[] encodeByte = Base64.decode(profile.get(position), Base64.DEFAULT);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                profile1.setImageBitmap(bitmap1);

            } catch (OutOfMemoryError outOfMemoryError) {
                outOfMemoryError.printStackTrace();
            }
            name1.setText(nm.get(position));
            phno1.setText(pn.get(position));
            return view;
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),total_payment.class);
        startActivity(intent);
        finish();
    }
}