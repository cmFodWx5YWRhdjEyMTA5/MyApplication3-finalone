package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class RegisterActivity_1 extends AppCompatActivity {
    Button btnContinue;
    CheckBox chkAgree;
    CheckBox chkDisagree;
    TextView TEXT_STATUS_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_1);

        chkAgree = findViewById(R.id.chkAgree);
        chkDisagree = findViewById(R.id.chkDisagree);
        btnContinue = findViewById(R.id.btnContinue);
        //TEXT_STATUS_ID = findViewById(R.id.TEXT_STATUS_ID);

        //loadAggrement();

        chkDisagree.setChecked(false);

        chkAgree.setChecked(false);
        chkAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnContinue.setEnabled(true);
                chkAgree.setChecked(true);
                chkDisagree.setChecked(false);
            }
        });

        chkDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnContinue.setEnabled(false);
                chkDisagree.setChecked(true);
                chkAgree.setChecked(false);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RegisterActivity_1.this, RegisterActivity_2.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_1.this);
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

                Intent i = new Intent(RegisterActivity_1.this, WelcomeActivity.class);
                startActivity(i);
                finish();

            }
        });
        builder.show();

    }


    private void loadAggrement() {


        String Text;


        Text="   ";



        TEXT_STATUS_ID.setText(Text);


    }


}
