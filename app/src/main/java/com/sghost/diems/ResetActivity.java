package com.sghost.diems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetActivity extends AppCompatActivity {
    EditText mEmail ;
    Button mReset ;
    TextView mRegi ;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        auth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email3);
        mReset = findViewById(R.id.resetButton3);
        mRegi = findViewById(R.id.registeredTXT3);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = mEmail.getText().toString();
                auth.sendPasswordResetEmail(_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        mRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

}