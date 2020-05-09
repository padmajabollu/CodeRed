package com.example.codered;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Base64;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public static final int PHONE_CALL=101;
    ListView listView;
    List<studentData> list=new ArrayList<>();
    ArrayList<String> nm=new ArrayList<>();
    ArrayList<String> pn=new ArrayList<>();
    ArrayList<String> profile=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),"Data is Loading.....",Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),"Click on Item to Call",Toast.LENGTH_LONG).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkPermission(Manifest.permission.CALL_PHONE,PHONE_CALL);

        Call<List<studentData>> call=ApiClient.getInstance().getApi().getStudent("getStud");
        call.enqueue(new Callback<List<studentData>>() {
            @Override
            public void onResponse(Call<List<studentData>> call, Response<List<studentData>> response) {

                list=response.body();

                if (list.size()>0)
                {
                    for (int i=0;i<list.size();i++)
                    {
                        nm.add(list.get(i).getName());
                        pn.add(list.get(i).getPhno());
                        profile.add(list.get(i).getImage());
                        listView=(ListView) findViewById(R.id.listview);
                        CustomAdapter customadapter=new CustomAdapter();
                        listView.setAdapter(customadapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String s = "tel:" + pn.get(position);
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(s));

                                try {
                                    startActivity(intent);
                                }
                                catch (SecurityException se)
                                {
                                    se.printStackTrace();
                                }

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
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_LONG).show();

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),admin_login.class);
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.admin_login) {
            Intent intent=new Intent(getApplicationContext(),admin_login.class);
            startActivity(intent);
            finish();


        }
        else if (id == R.id.exit) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            finish();
        }
        else if(id==android.R.id.home)
        {
            //finish();

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Exit ?")
                    .setTitle("Exit")
                    .setIcon(R.drawable.codered)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    class CustomAdapter extends BaseAdapter {
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
            View view=getLayoutInflater().inflate(R.layout.listitem,null);
            TextView phno1=(TextView) view.findViewById(R.id.phno);
            TextView name1=(TextView) view.findViewById(R.id.name);
            ImageView profile1=view.findViewById(R.id.img);

            try {
                byte[] encodeByte = Base64.decode(profile.get(position), Base64.DEFAULT);
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0,encodeByte.length);
                profile1.setImageBitmap(bitmap1);

            }
            catch (OutOfMemoryError outOfMemoryError)
            {
                outOfMemoryError.printStackTrace();
            }
            name1.setText(nm.get(position));
            phno1.setText(pn.get(position));
            return view;
        }
    }


    public void checkPermission(String permission,int requestCode)
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),permission)==PackageManager.PERMISSION_DENIED)
        {

            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
        }
        else
        {
            //Toast.makeText(getApplicationContext(),"Permission Already Granted",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==PHONE_CALL)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Contact Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Contact Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to Exit ?")
                .setTitle("Exit")
                .setIcon(R.drawable.codered)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}