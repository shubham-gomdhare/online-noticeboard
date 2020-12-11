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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText mEmail ;
    EditText mPassword ;
    EditText mConf_password ;
    Button mSignupButton ;
    TextView mRegisteredTXT ;
    FirebaseAuth auth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mEmail = findViewById(R.id.email1);
        mPassword = findViewById(R.id.password1);
        mConf_password = findViewById(R.id.conf_password1);
        mSignupButton = findViewById(R.id.signupButton1);
        mRegisteredTXT = findViewById(R.id.registeredTXT1);
        auth = FirebaseAuth.getInstance();

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = mEmail.getText().toString();
                String _password = mPassword.getText().toString();
                String _conf_password = mConf_password.getText().toString();
                if(_email.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                }
                if(_password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                }
                if(_password.equals(_conf_password)){
                    auth.createUserWithEmailAndPassword(_email, _password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRegisteredTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

}