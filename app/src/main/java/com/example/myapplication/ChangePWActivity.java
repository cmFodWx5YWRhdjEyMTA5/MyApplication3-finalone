package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePWActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btnChangePW;
    EditText EditCrntPW,EditNewPW,EditCnfrmNewPW;
    FirebaseUser user;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        auth = FirebaseAuth.getInstance();
        tinyDB = new TinyDB(this);
        user = auth.getCurrentUser();

        btnChangePW = findViewById(R.id.btnChngePW);
        EditCrntPW = findViewById(R.id.EditCrntPW);
        EditNewPW = findViewById(R.id.EditNewPW);
        EditCnfrmNewPW = findViewById(R.id.EditCnfrmNewPW);

        getSupportActionBar().setTitle("RESET PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnChangePW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangePW();
            }
        });


    }


    public void ChangePW(){
        final String CrntPW = EditCrntPW.getText().toString().trim();
        final String NewPW = EditNewPW.getText().toString().trim();
        final String CnfrmNewPW = EditCnfrmNewPW.getText().toString().trim();

        final String Email = user.getEmail();
        final String Pass = tinyDB.getString("Password");


        Log.e("Email",Email);
        Log.e("Pass",CrntPW);

        if (Pass.matches(CrntPW)==false){

            EditCrntPW.setError("Password You Entered Is Incorrect");
            //Toast.makeText(getApplicationContext(),"Confirm Password Doesn't Match Your Password",Toast.LENGTH_SHORT).show();
            return;

        }
        if (CnfrmNewPW.matches(NewPW)==false)
        {
            EditCnfrmNewPW.setError("Confirm Password Doesn't Match Your Password");
            //Toast.makeText(getApplicationContext(),"Confirm Password Doesn't Match Your Password",Toast.LENGTH_SHORT).show();
            return;

        }else if (NewPW.length()<8){
            EditNewPW.setError("Password Should Contain Atleast 8 Characters");
            //Toast.makeText(getApplicationContext(),"Password Should Contain Atleast 8 Characters",Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            AuthCredential credential = EmailAuthProvider.getCredential(Email,CrntPW);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        user.updatePassword(CnfrmNewPW).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();

                                }else{
                                    Toast.makeText(getApplicationContext(), "Failed to Update Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }catch(Exception e){

            Toast.makeText(getApplicationContext(), "Failed to Update Password", Toast.LENGTH_SHORT).show();

        }

    }


}
