package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener  {

    private EditText editSignMail;
    private EditText editSignPW;
    private Button btnSignIn;
    private TextView textSignIn,txtorgotPW;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        editSignMail = (EditText)findViewById(R.id.editSignMail);
        editSignPW = (EditText)findViewById(R.id.editSignPW);
        textSignIn = (TextView)findViewById(R.id.textSignIn);
        txtorgotPW = findViewById(R.id.textForgotPW);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        if (firebaseAuth.getCurrentUser()!=null)
        {
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }


        btnSignIn.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
        txtorgotPW.setOnClickListener(this);

    }

    private  void SignIn()
    {
        String signemail = editSignMail.getText().toString().trim();
        String signpw = editSignPW.getText().toString().trim();

        if (TextUtils.isEmpty(signemail))
        {
            Toast.makeText(getApplicationContext(),"Enter Your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (signemail.length()<12)
        {
            Toast.makeText(getApplicationContext(),"Enter A Valid Email Address",Toast.LENGTH_SHORT).show();
            return;

        }

        if (TextUtils.isEmpty(signpw))
        {
            Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            return;

        }
        if (signpw.length()<8)
        {
            Toast.makeText(getApplicationContext(),"Invalid Password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in, Please Wait..");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(signemail,signpw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"No Account Found! Check Your Email & Password..",Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v == btnSignIn)
        {
            SignIn();
        }
        if(v == textSignIn)
        {   finish();
            startActivity(new Intent(this,RegisterActivity_1.class));
        }
        if (v == txtorgotPW)
        {
            finish();
            startActivity(new Intent(this,ForgotPWActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Are You Sure You want to Quit?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        builder.show();

    }
}
