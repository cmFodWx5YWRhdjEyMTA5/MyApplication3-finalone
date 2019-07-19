package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {


    Button btnLogOut;
    Button btneaditProfile;
    Button btnResetPW;
    FirebaseAuth auth;
    StorageReference storageReferance;
    FirebaseDatabase firebaseDatabase;
    TinyDB tinyDB;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtNIC;
    EditText txtAddress;
    ImageView userImg;
    User user;
    int pick;
    String IMG_PATH = "";
    String PicID = "";
    private Uri filePath;
    String token = "";

    String IMAGE_BASE_64;
    String PATH;
    Bitmap profile_pic;
    File file;
    Boolean isImageSelected;

    String Uid = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        tinyDB = new TinyDB(this);
        token = tinyDB.getString("Token");
        Uid = tinyDB.getString("UID");

        PicID = tinyDB.getString("PIcID");

        storageReferance = FirebaseStorage.getInstance().getReference();

        userImg = findViewById(R.id.userImage);

        txtFirstName = findViewById(R.id.txtFirstName);
        txtFirstName.setEnabled(false);
        txtFirstName.setGravity(Gravity.CENTER);

        txtLastName = findViewById(R.id.txtLastName);
        txtLastName.setEnabled(false);
        txtLastName.setGravity(Gravity.CENTER);

        txtNIC = findViewById(R.id.txtNIC);
        txtNIC.setEnabled(false);
        txtNIC.setGravity(Gravity.CENTER);

        txtAddress = findViewById(R.id.txtAddress);
        txtAddress.setEnabled(false);
        txtAddress.setGravity(Gravity.CENTER);

        btnLogOut = findViewById(R.id.btnLogOut);
        btneaditProfile = findViewById(R.id.btnEditProfile);
        btnResetPW = findViewById(R.id.btnResetPW);

        auth = FirebaseAuth.getInstance();
        user = new User();
        tinyDB = new TinyDB(this);

        String key = tinyDB.getString("KEY");
        String ImageUrl = tinyDB.getString("ImgPath");
        String IMG_PATH= tinyDB.getString("IMG_PATH_CURRENT");


        Log.e("KEY",key);


        getSupportActionBar().setTitle("ACCOUNT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // StorageReference storageRef = storageReferance.child("MobileUser").child("UserImage").child(PicID).child("Images/Scetch.jpg");

        StorageReference storageRef = storageReferance.child("MobileUser").child("UserImage").child(IMG_PATH);

        final  long ONE_MEGABYTE = 1024 * 1024;

        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                userImg.setMinimumHeight(dm.heightPixels);
                userImg.setMinimumWidth(dm.widthPixels);
                userImg.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

//                Toast.makeText(getApplicationContext(),"Could Not Upload the Image",Toast.LENGTH_SHORT).show();

            }
        });

        userImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Selectimage();
                return false;
            }
        });



        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are You Sure You want to LogOut?");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        auth.signOut();
//                        int flag = 1;
//                        finish();
//                        tinyDB.putInt("Logged",flag);
//                        startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));

                        auth.signOut();
                        int flag = 1;
                        Intent i = new Intent(ProfileActivity.this,WelcomeActivity.class);
                        i.setFlags(flag);
                        startActivity(i);
                        finish();

                    }
                });
                builder.show();
            }
        });

        btneaditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),EditProfileActivity.class));
            }
        });

        btnResetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangePWActivity.class));
            }
        });

        // DatabaseReference df = FirebaseDatabase.getInstance().getReference("WebData").child("MobileUser").child("LhH46kISYxTxjUkxKhW");
        // DatabaseReference myRef = database.getReference("WebData").child("MobileUser").child("LhH46kISYxTxjUkxKhW");"
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("mobile").child("user").child(Uid);


        Log.e("Refff",myRef.toString());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                Log.e("DS",dataSnapshot.toString());
                try{
                    txtFirstName.setText(""+user.FirstName);
                    txtLastName.setText(""+user.LastName);
                    txtNIC.setText(""+user.NIC);
                    txtAddress.setText(""+user.Address);

                }catch (NullPointerException e)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = (List<String>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PATH);

            userImg.setImageBitmap(getBitmapFromPath(mPaths.get(0)));

            IMAGE_BASE_64 = encodeToBase64(getBitmapFromPath(mPaths.get(0)), Bitmap.CompressFormat.JPEG, 10);
            profile_pic = getBitmapFromPath(mPaths.get(0));
            PATH = mPaths.get(0);
            isImageSelected = true;
            file = new File(mPaths.get(0));
            filePath = Uri.fromFile(file);

            Log.e("uri >>", filePath+"");

        }


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private Bitmap getBitmapFromPath(String path) {

        File imgFile = new File(path);
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void Selectimage()
    {
        try {
            new ImagePicker.Builder(ProfileActivity.this)
                    .mode(ImagePicker.Mode.CAMERA)
                    .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                    .directory(ImagePicker.Directory.DEFAULT)
                    .extension(ImagePicker.Extension.PNG)
                    .scale(600, 600)
                    .allowMultipleImages(false)
                    .enableDebuggingMode(true)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
