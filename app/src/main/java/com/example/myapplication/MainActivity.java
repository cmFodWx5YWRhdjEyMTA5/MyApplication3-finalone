package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextMessage;
    private static Fragment selectedFragment = null;
    private Intent intent;

private CardView cardEmergency,cardNotification,cardFeedback,cardMyAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        cardMyAct = findViewById(R.id.cardMyAct);
        cardFeedback = findViewById(R.id.cardFeedback);
        cardNotification = findViewById(R.id.cardNotification);
        cardEmergency = findViewById(R.id.cardEmergency);

        cardMyAct.setOnClickListener(this);
        cardFeedback.setOnClickListener(this);
        cardNotification.setOnClickListener(this);
        cardEmergency.setOnClickListener(this);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()){

            case R.id.cardEmergency:i=new Intent(this,activity_emergency.class);startActivity(i);break;
            case R.id.cardFeedback:i=new Intent(this,activity_my_feedbacks.class);startActivity(i);break;
            case R.id.cardNotification:i=new Intent(this,activity_notifications.class);startActivity(i);break;
            case R.id.cardMyAct:i=new Intent(this,ProfileActivity.class);startActivity(i);break;
            default:break;



        }


    }
}
