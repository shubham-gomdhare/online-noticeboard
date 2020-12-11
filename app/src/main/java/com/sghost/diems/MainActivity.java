package com.sghost.diems;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;


public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private RecyclerView mRecyclerView;
    private NoticeAdapter mAdapter;
    private List<Upload> mUploads;
    private DatabaseReference mDatabaseRef;
    private ProgressBar mProgressCircle;
    private FirebaseStorage mStorage;
    private PhotoViewAttacher mAttacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        openNoticesActivity();
        }

    private void openNoticesActivity() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);
        mUploads = new ArrayList<>();
        mAdapter = new NoticeAdapter(MainActivity.this, mUploads);
        mAdapter.setOnItemClickListener(MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Notices/");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUploads.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setmKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem upload_option = menu.findItem(R.id.upload_notice);
       /* if(userEmail.equals("sgwthost@gmail.com")){
            upload_option.setVisible(true);
        }else{
            upload_option.setVisible(false);
        }*/
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        final FirebaseAuth auth =  FirebaseAuth.getInstance();
        //noinspection SimplifiableIfStatement

        if (id == R.id.signoutButton) {
            auth.signOut();
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        if (id == R.id.upload_notice) {
            Intent intent = new Intent(MainActivity.this,UploadNotice.class);
            startActivity(intent);
            finish();
            return true;
        }
/*
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Android Menu is Clicked", Toast.LENGTH_LONG).show();
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onDownloadClick(final int position) throws IOException {
       Upload selectedNotice = mUploads.get(position);
       String selectedKey = selectedNotice.getmKey();
       StorageReference storageReference = mStorage.getReferenceFromUrl(selectedNotice.getmUrl());
       final File file = new File(Environment.getExternalStoragePublicDirectory("DIEMS/"),"notice_"+selectedNotice.getmTitle()+".png");
       storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
               Toast.makeText(MainActivity.this,"Download successful!\n"+file.getPath(),Toast.LENGTH_LONG).show();
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(MainActivity.this,"Download failed!"+e.getMessage(),Toast.LENGTH_LONG).show();
           }
       });
    }

    @Override
    public void onShowClick(int position) {
        final long ONE_MEGABYTE = 1024 * 1024;

        Toast.makeText(this,"show clicked",Toast.LENGTH_LONG).show();
        Upload selectedNotice = mUploads.get(position);
        StorageReference storageReference = mStorage.getReferenceFromUrl(selectedNotice.getmUrl());
        final Intent intent = new Intent(MainActivity.this, PopActivity.class);
        String textView = selectedNotice.getmTitle();
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                intent.putExtra("bitmap",bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error while making bitmap",Toast.LENGTH_LONG).show();
            }
        });
        //intent.putExtra("image", (Parcelable) bytes);
        intent.putExtra("title", textView);
        startActivity(intent);
    }
        /*try{
            ImageView imageView = findViewById(R.id.image_view_upload);
            TextView textView = findViewById(R.id.text_view_title);
            String text_title = textView.getText().toString();
            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Intent intent = new Intent(MainActivity.this, PopActivity.class);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            intent.putExtra("image", bytes);
            intent.putExtra("title", text_title);
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /*private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
*/
}
