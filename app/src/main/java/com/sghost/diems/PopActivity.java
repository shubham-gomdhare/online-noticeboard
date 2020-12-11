package com.sghost.diems;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class PopActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);
        imageView = findViewById(R.id.image_view_show);
        textView = findViewById(R.id.text_view_show);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int )(width*0.90),(int)(height*0.85));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        String title = getIntent().getStringExtra("title");
        Bitmap bmp = getIntent().getParcelableExtra("bitmap");
       imageView.setImageBitmap(bmp);
        textView.setText(title);
/*
        try {
            imageView = findViewById(R.id.image_view_show);
            textView = findViewById(R.id.text_view_show);
            byte[] bytes = getIntent().getByteArrayExtra("image");
            String title = getIntent().getStringExtra("title");
            assert bytes != null;
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            textView.setText(title);
            imageView.setImageBitmap(bmp);
          /*  DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            getWindow().setLayout((int )(width),(int)(height*0.85));
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.gravity = Gravity.CENTER;
            getWindow().setAttributes(params);*/
       /* } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

}
