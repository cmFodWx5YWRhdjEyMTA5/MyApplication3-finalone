package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity_4 extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignUp;
    private EditText editFirstName,editLastName,editNIC,editMail,editAddress,editPW,editCnfrmPW;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    StorageReference storageReferance;
    private TinyDB tinyDB;
    String token="";
    String PicID = "";
    String img_url = "";
    String Uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_4);


        auth = FirebaseAuth.getInstance();
        storageReferance = FirebaseStorage.getInstance().getReference();

        tinyDB = new TinyDB(this);

        token = FirebaseInstanceId.getInstance().getToken();

        Uid = auth.getUid();

        tinyDB.putString("UID",Uid);

        Log.e("TOKEN",token);
        tinyDB.putString("Token",token);
        PicID = tinyDB.getString("PIcID");

        img_url = tinyDB.getString("ImgPath");





        progressDialog=new ProgressDialog(this);

        progressDialog=new ProgressDialog(this);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        editFirstName = (EditText)findViewById(R.id.editFirstName);
        editLastName = (EditText)findViewById(R.id.editLastName);
        editNIC = (EditText)findViewById(R.id.editNIC);
        editMail = (EditText)findViewById(R.id.editEmail);
        editAddress = (EditText)findViewById(R.id.editAddress);
        editPW = (EditText)findViewById(R.id.editPassword);
        editCnfrmPW = (EditText)findViewById(R.id.editPassCnfrm);

        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity_4.this);
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

                auth.getCurrentUser().delete();

                Intent i = new Intent(RegisterActivity_4.this,WelcomeActivity.class);
                startActivity(i);
                finish();

            }
        });
        builder.show();

    }


    public  void checkUser(){
        auth.fetchSignInMethodsForEmail(editMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                Toast.makeText(getApplicationContext(), "Username Exists", Toast.LENGTH_SHORT).show();


            }

        });
    }

    private  void RegUser()
    {
        final String FirstName = editFirstName.getText().toString().trim();
        final String LastName = editLastName.getText().toString().trim();
        final String NIC = editNIC.getText().toString().trim();
        final String Mail = editMail.getText().toString().trim();
        final String Address = editAddress.getText().toString().trim();
        final String Password = editPW.getText().toString().trim();
        final String ConfirmPW = editCnfrmPW.getText().toString().trim();
        final String Type = "Client";

        if (TextUtils.isEmpty(FirstName))
        {
            editFirstName.setError("Enter Your First Name");
            //Toast.makeText(getApplicationContext(),"Enter Your First Name",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(LastName))
        {
            editLastName.setError("Enter Your Last Name");
            //Toast.makeText(getApplicationContext(),"Enter Your Last Name",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(NIC))
        {
            editNIC.setError("Enter Your NIC");
            //Toast.makeText(getApplicationContext(),"Enter Your NIC",Toast.LENGTH_SHORT).show();
            return;
        }
        if (NIC.length()<10|| NIC.length()>12)
        {
            editNIC.setError("Enter A Valid NIC");
            //Toast.makeText(getApplicationContext(),"Enter A Valid NIC",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(Mail))
        {
            editMail.setError("Enter Your Email Address");
            //Toast.makeText(getApplicationContext(),"Enter Your Email",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(Address))
        {
            editAddress.setError("Enter Your Home Address");
            //Toast.makeText(getApplicationContext(),"Enter Your Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (Mail.length()<12)
        {
            editMail.setError("Enter A Valid Email Address");
            //Toast.makeText(getApplicationContext(),"Enter A Valid Email Address",Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(Password))
        {
            editPW.setError("Create Your Password");
            //Toast.makeText(getApplicationContext(),"Enter Your Password",Toast.LENGTH_SHORT).show();
            return;

        }
        if (Password.length()<8)
        {
            editPW.setError("Password Should Contain Atleast 8 Characters");
            //Toast.makeText(getApplicationContext(),"Password Should Contain Atleast 8 Characters",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ConfirmPW))
        {
            editCnfrmPW.setError("Enter Confirm Password");
            //Toast.makeText(getApplicationContext(),"Enter Confirm Password",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ConfirmPW.matches(Password)==false)
        {
            editCnfrmPW.setError("Confirm Password Doesn't Match Your Password");
            //Toast.makeText(getApplicationContext(),"Confirm Password Doesn't Match Your Password",Toast.LENGTH_SHORT).show();
            return;

        }



        progressDialog.setMessage("Signing up, Please Wait..");
        progressDialog.setIcon(R.drawable.sherlock_logo);
        progressDialog.setCancelable(false);
        progressDialog.show();


        auth.createUserWithEmailAndPassword(Mail,Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                try {


                    progressDialog.dismiss();

                    if (task.isSuccessful()) {

                        tinyDB.putString("Password",ConfirmPW);


                        // Toast.makeText(RegisterActivity_4.this, "Success", Toast.LENGTH_SHORT).show();
                       // img_url = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg").getDownloadUrl().toString();


                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        DatabaseReference myRef = database.getReference("mobile").child("user").child(Uid);

//                        String UserKey = myRef.push().getKey();
//                        tinyDB.putString("KEY",UserKey);

//                        Log.e("UID",UserKey);

                        User user = new User(FirstName,LastName,NIC,Mail,Address,Type,Uid,"Active",token,img_url);

                        Log.e(">>",user.FirstName);
                        myRef.setValue(user);

                        if (Uid!=null) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                    if (task.isSuccessful() != true) {
                        checkUser();
                    }

                }catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

        if(v==btnSignUp)
        {
            RegUser();

        }

    }

}
