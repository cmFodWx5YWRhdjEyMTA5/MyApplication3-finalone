package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPWActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button btnSendMail;
    EditText EditBckMail;
    String UserMail = "";
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        btnSendMail = findViewById(R.id.btnSendMail);
        EditBckMail = findViewById(R.id.EditBckMail);


        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMail();
            }
        });



    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ForgotPWActivity.this,WelcomeActivity.class);
        startActivity(i);
        finish();
    }

    public void SendMail(){

        String ProvidedMail = EditBckMail.getText().toString().trim();
//        UserMail = user.getEmail();


        if (TextUtils.isEmpty(ProvidedMail)){

            EditBckMail.setError("Enter Your Registered Email Id");
            return;
        }else{

            auth.sendPasswordResetEmail(ProvidedMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(getApplicationContext(),"Reset Link Sent",Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(getApplicationContext(),"Failed to Send the Reset Link Sent",Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


}
