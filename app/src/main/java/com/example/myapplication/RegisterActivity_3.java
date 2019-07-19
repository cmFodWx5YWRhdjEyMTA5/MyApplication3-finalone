package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class RegisterActivity_3 extends AppCompatActivity {
    Button btnSubmit;
    private String verficationID;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private EditText editCode;
    private TextView txtSendAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_3);

        auth = FirebaseAuth.getInstance();
        editCode = findViewById(R.id.editCode);
        txtSendAgain = findViewById(R.id.txtSendAgain);
        progressBar = findViewById(R.id.progressBar1);
        final String PhoneNumber = getIntent().getStringExtra("NUMBER");
        sendVerificationCode(PhoneNumber);

        txtSendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode(PhoneNumber);

            }
        });
        btnSubmit = findViewById(R.id.btnConfirm);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    String code = editCode.getText().toString().trim();
//                    String code = "990077";

                    if (code.isEmpty()||code.length()<6){

                        editCode.setError("Enter Code..");
                        editCode.requestFocus();
                        return;
                    }


                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(code);

//                    Intent i = new Intent(RegisterActivity_3.this,CaptureFaceActivity.class);
//                    startActivity(i);
//                    finish();
                }catch (NullPointerException e){


                }


            }
        });
    }




    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_3.this);
        builder.setCancelable(true);
        builder.setTitle("Your SignUp Process Will Be Wiped. Are Your Sure to Start Over?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try{
                    Intent i = new Intent(RegisterActivity_3.this,WelcomeActivity.class);
                    startActivity(i);
                    finish();
                }catch (Exception e){

                }
            }
        });
        builder.show();

    }


    private void verifyCode(String code){

        try {

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verficationID,code);
            signInWithCredential(credential);

        }catch (Exception e){
            editCode.setError("Invalid Code!");
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            Intent i = new Intent(RegisterActivity_3.this,CaptureFaceActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();

                        }else {
                            Toast.makeText(RegisterActivity_3.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verficationID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code!=null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(RegisterActivity_3.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };









}
