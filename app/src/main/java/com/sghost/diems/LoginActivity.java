package com.sghost.diems;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity  {
    EditText mEmail ;
    EditText mPassword ;
    Button mLoginButton ;
    TextView mResetTXT ;
    TextView mSignupTXT;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email2);
        mPassword = findViewById(R.id.password2);
        mLoginButton = findViewById(R.id.loginButton2);
        mResetTXT = findViewById(R.id.resetTXT2);
        mSignupTXT = findViewById(R.id.signupTXT2);
        FirebaseApp.initializeApp(this);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(!(currentUser == null)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            mResetTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));
                finish();
            }
        });
            mSignupTXT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                    finish();
                }
            });
            mLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String _email = mEmail.getText().toString();
                    String _password = mPassword.getText().toString();
                    if(_email.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    }
                    if(_password.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    }
                    auth.signInWithEmailAndPassword(_email,_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (mPassword.length() < 6) {
                                            mPassword.setError("Password too short, enter minimum 6 characters!");
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                }
            });
        }

    }

}