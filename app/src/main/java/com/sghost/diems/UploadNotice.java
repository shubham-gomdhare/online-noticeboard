package com.sghost.diems;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class UploadNotice extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Button mButtonChoose;
    private Button mButtonUpload;
    private Button mButtonHome;
    private EditText mTitle;
    private ImageView mFile;
    private ProgressBar mProgressBar;
    private Uri mFileUri;
   // private String mUrl;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String mUser;
    private StorageTask mUploadTask;
    public int h,w;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_notice);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Upload New Notice");

        mButtonChoose = findViewById(R.id.choose_image_btn);
        mButtonUpload = findViewById(R.id.upload_notice_btn);
        mButtonHome = findViewById(R.id.home_btn);
        mTitle = findViewById(R.id.file_name);
        mFile = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
       // mFileUri = findViewById(R.id.image_view);
        mStorageRef = FirebaseStorage.getInstance().getReference("Notices/");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Notices/");
        mUser = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        mButtonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUploadTask != null && mUploadTask.isInProgress()){
                    Toast.makeText(UploadNotice.this,"Upload is in progress!",Toast.LENGTH_LONG).show();
                }else {
                    uploadNotice();
                }
            }
        });

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadNotice.this, MainActivity.class));
                finish();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadNotice() {
     if(mFileUri != null){
         final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mFileUri));
         mUploadTask = fileReference.putFile(mFileUri)
                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                                 Upload upload = new Upload(mTitle.getText().toString().trim(), mUser, String.valueOf(uri), h, w);
                                 String uploadID = mDatabaseRef.push().getKey();
                                 mDatabaseRef.child(uploadID).setValue(upload);
                             }
                         });
                         Handler handler = new Handler();
                         handler.postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 mProgressBar.setProgress(0);
                             }
                         },500);
                       //  Uri download = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
                         Toast.makeText(UploadNotice.this,"Upload successful!",Toast.LENGTH_LONG).show();

                     }

                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(UploadNotice.this,e.getMessage(),Toast.LENGTH_LONG).show();
                     }
                 })
                 .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                     @Override
                     public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                         double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                         mProgressBar.setProgress((int)progress);
                     }
                 });

     }else {
         Toast.makeText(UploadNotice.this,"No file selected!",Toast.LENGTH_LONG).show();
     }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mFileUri = data.getData();
            mFile.setImageURI(mFileUri);
             h = mFile.getHeight();
             w = mFile.getWidth();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_FILE_REQUEST);

    }
}
