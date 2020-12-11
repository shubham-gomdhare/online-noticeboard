package com.sghost.diems;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;


public class AdminLogin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private EditText _mUsername;
    private EditText _mPassword;
    public Button mAdminLogin;
    public Spinner user_list;
    public String item;
    public String key;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_admin);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        _mUsername = findViewById(R.id.admin_username);
        _mPassword = findViewById(R.id.admin_pass);
        mAdminLogin = findViewById(R.id.admin_login_btn);
        user_list = findViewById(R.id.spinner);
        user_list.setOnItemSelectedListener(this);
        List<String> users = new ArrayList<String>();
        users.add("Director");
        users.add("HOD CSE");
        users.add("HOD CIVIL");
        users.add("HOD BSH");
        users.add("HOD MECHANICAL");
        users.add("HOD ETC");
        users.add("STUDENT SECTION");
        users.add("SUPER ADMIN");

        ArrayAdapter<String> list = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,users);
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_list.setAdapter(list);
        mAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String type =  item;
                if(_mUsername == null) {
                    Toast.makeText(AdminLogin.this, "Enter username!", Toast.LENGTH_LONG).show();
                }else if(_mPassword == null){
                    Toast.makeText(AdminLogin.this,"Enter password!",Toast.LENGTH_LONG).show();
                }else{
                    final String uname = _mUsername.getText().toString().trim();
                    final String pass = _mPassword.getText().toString().trim();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference reff = database.getReference().child("Users").child(type);
                    reff.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String _username = snapshot.child("mUsername").getValue().toString().trim();
                                String _pass = snapshot.child("mPassword").getValue().toString().trim();
                                key = reff.push().getKey();
                                if(!_username.equals(uname)){
                                    Toast.makeText(AdminLogin.this,"User not exist",Toast.LENGTH_LONG).show();
                                }else {
                                    if(!_pass.equals(pass)){
                                        wrongPass();
                                        _mPassword.setText("");
                                        _mPassword.requestFocus();
                                    }else {
                                        startActivity(new Intent(AdminLogin.this, UploadNotice.class));
                                        finish();
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
        }
            private void wrongPass() {
                Toast.makeText(AdminLogin.this,"Wrong Password",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         item = parent.getItemAtPosition(position).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
