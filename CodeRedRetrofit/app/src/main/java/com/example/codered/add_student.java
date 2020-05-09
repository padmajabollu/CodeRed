package com.example.codered;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class add_student extends AppCompatActivity {

    private static final int STORAGE=100;
    Button add,pickimg;
    ImageView photo;
    TextInputLayout sender,phno;
    String sender1,phno1;
    String media1,media_title;
    Long media_size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,STORAGE);

        ActionBar bar=getSupportActionBar();
        bar.setTitle("Add Student Details");
        sender=(TextInputLayout) findViewById(R.id.name);
        phno=(TextInputLayout) findViewById(R.id.phno);
        photo=findViewById(R.id.photo);
        pickimg=findViewById(R.id.pick_img);
        pickimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
        add=(Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender1=sender.getEditText().getText().toString().trim();
                phno1=phno.getEditText().getText().toString().trim();

                if (validateSender() && validatephno() && validateAttach())
                {
                    String toast=sender1+"\n"+phno1+"\n"+media_title;
                    //Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_SHORT).show();
                    Call<List<status>> call=ApiClient.getInstance().getApi().addStudent("insertStud",sender1,phno1,media_title,media1);
                    call.enqueue(new Callback<List<status>>() {
                        @Override
                        public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                            List<status> list=new ArrayList<>();
                            list=response.body();
                            Toast.makeText(getApplicationContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),total_payment.class);
                            startActivity(intent);
                            finish();

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


    private boolean validateAttach() {

            if((media_size/1000000)<=5)
            {
                return true;
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Size must be less or equal to 5MB",Toast.LENGTH_LONG).show();

                return false;
            }

    }

    private boolean validatephno() {
        if(!phno1.isEmpty())
        {
            String[] calls=phno1.split(",");
            int count=0;

            for (int j=0;j<calls.length;j++)
            {
                int c=0;
                String s=calls[j];

                for (int i=0;i<s.length();i++)
                {
                    //Toast.makeText(getContext(),Integer.toString((int)s.charAt(i)),Toast.LENGTH_SHORT).show();


                    if((int)s.charAt(i)>=48 && (int)s.charAt(i)<=57)
                    {
                        c+=1;
                    }
                    else if((int)s.charAt(i)==43)
                    {
                        c+=1;
                    }

                }

                if (c==calls[j].length() && (c==13 || c==10))
                {
                    count+=1;
                }

            }

            if(count==(calls.length))
            {
                phno.setError(null);
                return true;
            }
            else
            {
                phno.setError("Phone Number is Invalid");
                return false;
            }

        }
        else
        {
            phno.setError("Field can't be Empty");
            return false;
        }
    }


    private boolean validateSender() {

        if(!sender1.isEmpty())
        {
            int c=0;
            String l_sender=sender1.toLowerCase();
            for (int i=0;i<sender1.length();i++)
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

            if (sender1.length()>=4 && sender1.length()==c ) {
                sender.setError(null);
                return true;
            }
            else {
                sender.setError("Sender Name is Invalid");
                return false;
            }

        }

        else
        {
            sender.setError("Field can't be Empty");
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
        else if (id==R.id.logout)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                photo.setImageBitmap(bitmap);
                Cursor cursor=getContentResolver().query(uri,null,null,null,null);
                int nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
                cursor.moveToFirst();
                media_title=cursor.getString(nameIndex);
                media_size=cursor.getLong(sizeIndex);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                media1= Base64.encodeToString(bytes, Base64.DEFAULT);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (OutOfMemoryError outOfMemoryError)
            {
                outOfMemoryError.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==STORAGE)
        {
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Storage Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Storage Permission Denied",Toast.LENGTH_SHORT).show();
            }
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
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),total_payment.class);
        startActivity(intent);
        finish();

    }
}