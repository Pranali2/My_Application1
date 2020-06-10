package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Sign_form extends AppCompatActivity {
    EditText txtEmail,txtPassword,txtConfirmPassword,username1 ;
    Button btn_register;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_form);
      txtEmail=(EditText) findViewById(R.id.txt_email);
       username1=(EditText) findViewById(R.id.et_name);
        txtPassword=(EditText) findViewById(R.id.txt_password);
        txtConfirmPassword= (EditText) findViewById(R.id.txt_confirmpassword);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        btn_register=(Button)findViewById(R.id.buttonregister_);
        firebaseAuth=FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                final String uname = username1.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                String confirmpassword = txtConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Sign_form.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Sign_form.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(Sign_form.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (password.length() < 6) {
                    Toast.makeText(Sign_form.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                if (password.equals(confirmpassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Sign_form.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility((View.GONE));
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        Toast.makeText(Sign_form.this, "Registration Sucessful", Toast.LENGTH_SHORT).show();
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        String user = firebaseUser.getUid();
                                        reference= FirebaseDatabase.getInstance().getReference("Users").child(user);
                                        HashMap<String,String> hashMap=new HashMap<>();
                                        hashMap.put("id",user);
                                        hashMap.put("username", uname);
                                        hashMap.put("imageURL","default");
                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent=new Intent(Sign_form.this,Login_form.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Sign_form.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                           });
                }
            }
        });

    }

}

