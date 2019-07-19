package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity_2 extends AppCompatActivity {
    Button btnSubmit;
    EditText editMobile;
    EditText editCountryCode;
    String PhoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);

        btnSubmit = findViewById(R.id.btnSubmit);
        editMobile = findViewById(R.id.mobileNo);
        editCountryCode = findViewById(R.id.editCountryCode);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneNumber = editCountryCode.getText().toString().trim()+editMobile.getText().toString().trim();

                Log.e("TEST","TEST"+PhoneNumber);


                if (PhoneNumber.isEmpty()||PhoneNumber.length()<10){

                    editMobile.setError("Enter A Valid Phone Number!");
                    return;
                }

                Intent i = new Intent(RegisterActivity_2.this,RegisterActivity_3.class);
                i.putExtra("NUMBER",PhoneNumber);
                startActivity(i);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_2.this);
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

                Intent i = new Intent(RegisterActivity_2.this,WelcomeActivity.class);
                startActivity(i);
                finish();

            }
        });
        builder.show();

    }


}
